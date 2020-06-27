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
        inputWord = inputWord.toLowerCase();
        if (trie.find(inputWord) != null) return inputWord;
        calculateDeletions(inputWord);
        return similarWord;
    }

    public void calculateDeletions(String inputWord) {
        for (int i = 0; i < inputWord.length(); i++) {
            String testWord = inputWord.substring(0,i).concat(inputWord.substring(i+1));
            Node found = trie.find(testWord);
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
}
