package spell;

public class Trie implements ITrie {

    private int nodeCount = 1;
    private int wordCount = 0;
    private Node root = new Node();
    private int hash = 1;

    @Override
    public void add(String word) {
        Node currentNode = root; // start with the root
        char[] chars = word.toCharArray(); // make the string an easy array
        boolean newWord = false;
        for (char c : chars) { // for each letter in the word
//            c -= 97; // convert value to 0-26
            int index = (int)c - (int)'a';
            Node parentNode = currentNode;
            currentNode = currentNode.getChildren()[index];
            if(currentNode == null) {
                currentNode = new Node(); // check whether the node exists
                parentNode.getChildren()[index] = currentNode;
                nodeCount++;
            }
        }
        hash += word.hashCode(); //add to the hash value
        if (currentNode.getValue() == 0) wordCount++;
        currentNode.incrementValue();
    }

    @Override
    public Node find(String word) {
        char[] chars = word.toCharArray(); // make the string an easy array
        Node result = root;
        for (char c :
                chars) {
            int index = (int)c - (int)'a';
            if (result == null) break;
            result = result.getChildren()[index];
        }
        if (result != null && result.getValue() == 0) result = null;
        return result;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {

        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toString_Helper(root, curWord, output);

        return output.toString();
    }

    private void toString_Helper(Node n, StringBuilder curWord, StringBuilder output) {
        if (n.getValue() > 0) {
            output.append(curWord.toString() + "\n");
        }
        for (int i = 0; i < n.getChildren().length; i++) {
            Node child = n.getChildren()[i];

            if (child != null) {
                char childLetter = (char) ('a' + i);
                curWord.append(childLetter);

                toString_Helper(child, curWord, output);
                curWord.deleteCharAt(curWord.length() -1);
            }
        }
    }

    @Override
    public int hashCode() {
        return 97 * hash * wordCount;
    }

    public Node getRoot() {
        return root;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Trie.class) return false;
        if (((Trie) o).getNodeCount() != this.nodeCount || ((Trie) o).getWordCount() != this.wordCount) return false;

        return equalsHelper(this.root, ((Trie) o).getRoot());
    }

    public boolean equalsHelper(Node ours, Node other) {
        if(ours == null && other == null) return true;
        else if (ours == null ^ other == null) return false;
        else if (ours.getValue() != other.getValue()) return false;
        boolean result = true;
        for (int i = 0; i < 26; i++) {
            if (!equalsHelper(ours.getChildren()[i], other.getChildren()[i])) result = false;
        }
        return result;
    }
}
