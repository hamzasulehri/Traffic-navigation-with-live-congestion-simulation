package algorithms;

import graph.*;
import history.Route;

import java.util.*;

public class Dijkstra {

    public static Route getShortestRoute(
            CityGraph graph,
            String sourceName,
            String destinationName) {

        Node source =
                graph.getNode(sourceName);

        Node destination =
                graph.getNode(destinationName);

        if (source == null || destination == null) {
            return null;
        }

        Map<Node, Integer> distance =
                new HashMap<>();

        Map<Node, Node> parent =
                new HashMap<>();

        PriorityQueue<RouteNode> pq =
                new PriorityQueue<>();

        for (Node node :
                graph.getAdjacencyList().keySet()) {

            distance.put(node, Integer.MAX_VALUE);
        }

        distance.put(source, 0);

        pq.add(new RouteNode(source, 0));

        while (!pq.isEmpty()) {

            RouteNode current = pq.poll();

            Node currentNode = current.getNode();

            // Skip outdated entries: a shorter path to this node
            // was already found and processed earlier.
            if (current.getDistance() > distance.get(currentNode)) {
                continue;
            }

            for (Edge edge :
                    graph.getAdjacencyList().get(currentNode)) {

                if (edge.isBlocked()
                        || edge.getWeight() == Integer.MAX_VALUE) {
                    continue;
                }

                Node neighbor =
                        edge.getDestination();

                int newDistance =
                        distance.get(currentNode)
                                + edge.getWeight();

                if (newDistance < distance.get(neighbor)) {

                    distance.put(neighbor, newDistance);
                    parent.put(neighbor, currentNode);

                    pq.add(new RouteNode(neighbor, newDistance));
                }
            }
        }

        if (distance.get(destination) == Integer.MAX_VALUE) {
            return null;
        }

        List<String> path =
                new ArrayList<>();

        Node current = destination;

        while (current != null) {
            path.add(current.getName());
            current = parent.get(current);
        }

        Collections.reverse(path);

        return new Route(path, distance.get(destination));
    }
}