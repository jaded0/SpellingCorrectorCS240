package spell;

import spell.ISpellCorrector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector {
    private Trie trie = new Trie();
    private String similarWord;
    private int similarWordScore = 0;
    private ArrayList<String> editDist1 = new ArrayList<>();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner in = new Scanner(new File(dictionaryFileName));
        while (in.hasNextLine()){
            String[] tokens = in.nextLine().toLowerCase().split(" ");
            for (String token :
                    tokens) {
                trie.add(token);
            }
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        // reset the result values
        similarWord = null; similarWordScore = 0;
        inputWord = inputWord.toLowerCase();
        if (trie.find(inputWord) != null) return inputWord;
        calculateDeletions(inputWord, true);
        calculateTranspositions(inputWord, true);
        calculateAlterations(inputWord, true);
        calculateInsertions(inputWord, true);
        if(similarWord != null) return similarWord;
        int dist1Size = editDist1.size();
        for (String s : editDist1) {
            calculateDeletions(s, false);
            calculateTranspositions(s, false);
            calculateAlterations(s, false);
            calculateInsertions(s, false);
        }
        return similarWord;
    }

    public void calculateDeletions(String inputWord, boolean firstTime) {
        for (int i = 0; i < inputWord.length(); i++) {
            // calculate deletion option
            String testWord = inputWord.substring(0,i).concat(inputWord.substring(i+1));
            Node found = trie.find(testWord);
            // prep for edit dist 2
            if (firstTime) editDist1.add(testWord);
            // if no other test words have matched yet, it wins!
            if (found != null && similarWord == null) {
                similarWord = testWord;
                similarWordScore = found.getValue();
            }
            // if this test word is more common, or lower in alphabetical order, it wins
            else if (found != null && found.getValue() >= similarWordScore && testWord.compareTo(similarWord) < 0) {
                similarWord = testWord;
                similarWordScore = found.getValue();
            }
        }
    }

    public void calculateTranspositions(String inputWord, boolean firstTime) {
        for (int i = 0; i < inputWord.length()-1; i++) {
            // calculate the deletion option
            StringBuilder testWord = new StringBuilder(inputWord);
            testWord.deleteCharAt(i);
            testWord.insert(i+1, inputWord.charAt(i));
            Node found = trie.find(testWord.toString());
            // prep for edit dist 2
            if (firstTime) editDist1.add(testWord.toString());
            // if no other test words have matched yet, it wins!
            if (found != null && similarWord == null) {
                similarWord = testWord.toString();
                similarWordScore = found.getValue();
            }
            // if this test word is more common, or lower in alphabetical order, it wins
            else if (found != null && found.getValue() >= similarWordScore && testWord.toString().compareTo(similarWord) < 0) {
                similarWord = testWord.toString();
                similarWordScore = found.getValue();
            }
        }
    }

    public void calculateAlterations(String inputWord, boolean firstTime) {
        for (int i = 0; i < inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                // calculate the alteration option
                StringBuilder testWord = new StringBuilder(inputWord);
                testWord.deleteCharAt(i);
                testWord.insert(i, Character.toChars('a'+j));
                // prep for edit dist 2
                if (firstTime) editDist1.add(testWord.toString());

                // if no other test words have matched yet, it wins!
                Node found = trie.find(testWord.toString());
                if (found != null && similarWord == null) {
                    similarWord = testWord.toString();
                    similarWordScore = found.getValue();
                }
                // if this test word is more common, or lower in alphabetical order, it wins
                else if (found != null && found.getValue() > similarWordScore) {
                    similarWord = testWord.toString();
                    similarWordScore = found.getValue();
                } else if (found != null && found.getValue() == similarWordScore && testWord.toString().compareTo(similarWord) < 0) {
                    similarWord = testWord.toString();
                    similarWordScore = found.getValue();
                }
            }
        }
    }

    public void calculateInsertions(String inputWord, boolean firstTime) {
        for (int i = 0; i < inputWord.length() + 1; i++) {
            for (int j = 0; j < 26; j++) {
                // calculate the alteration option
                StringBuilder testWord = new StringBuilder(inputWord);
                testWord.insert(i, Character.toChars('a'+j));
                Node found = trie.find(testWord.toString());
                // prep for edit dist 2
                if (firstTime) editDist1.add(testWord.toString());
                // if no other test words have matched yet, it wins!
                if (found != null && similarWord == null) {
                    similarWord = testWord.toString();
                    similarWordScore = found.getValue();
                }
                // if this test word is more common, or lower in alphabetical order, it wins
                else if (found != null && found.getValue() >= similarWordScore && testWord.toString().compareTo(similarWord) < 0) {
                    similarWord = testWord.toString();
                    similarWordScore = found.getValue();
                }
            }
        }
    }
}
