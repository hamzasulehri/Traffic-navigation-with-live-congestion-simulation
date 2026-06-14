package algorithms;

import graph.Node;

public class RouteNode implements Comparable<RouteNode> {

    private Node node;
    private int distance;

    public RouteNode(Node node, int distance) {
        this.node = node;
        this.distance = distance;
    }

    public Node getNode() {
        return node;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int compareTo(RouteNode other) {
        return Integer.compare(this.distance, other.distance);
    }
}