package graph;

import trie.LocationTrie;

import java.util.*;

public class CityGraph {

    private Map<String, Node> nodes;
    private Map<Node, List<Edge>> adjacencyList;
    private LocationTrie trie;

    public CityGraph() {
        nodes = new HashMap<>();
        adjacencyList = new HashMap<>();
        trie = new LocationTrie();
    }

    public void addNode(String name) {

        Node node = new Node(name);

        trie.insert(name);
        nodes.put(name, node);
        adjacencyList.put(node, new LinkedList<>());
    }

    public LocationTrie getTrie() {
        return trie;
    }

    public void addEdge(String sourceName,
                        String destinationName,
                        int weight) {

        Node source = nodes.get(sourceName);
        Node destination = nodes.get(destinationName);

        if(source == null || destination == null) {
            System.out.println("Node not found!");
            return;
        }

        adjacencyList.get(source)
                .add(new Edge(destination, weight));

        adjacencyList.get(destination)
                .add(new Edge(source, weight));
    }

    public void printGraph() {

        for(Node node : adjacencyList.keySet()) {

            System.out.print(node.getName() + " -> ");

            for(Edge edge : adjacencyList.get(node)) {

                System.out.print(
                        edge.getDestination().getName()
                                + "("
                                + edge.getWeight()
                                + ") "
                );
            }

            System.out.println();
        }
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    public Map<Node, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }
}