package spell;

public class Node implements INode {

    private int value = 0;
    private Node[] children =  new Node[26];

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void incrementValue() {
        value++;
    }

    @Override
    public Node[] getChildren() {
        return children;
    }
}
