package graph;

public class Edge {

    private Node destination;

    private int weight;
    private int originalWeight;

    private boolean blocked;

    public Edge(Node destination, int weight) {

        this.destination = destination;

        this.weight = weight;
        this.originalWeight = weight;

        this.blocked = false;
    }

    public Node getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public int getOriginalWeight() {
        return originalWeight;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}