package algorithms;

import graph.*;

import history.Route;

import java.util.*;

public class Dijkstra {



    public static void findShortestPath(
            CityGraph graph,
            String sourceName,
            String destinationName) {

        Node source = graph.getNode(sourceName);
        Node destination = graph.getNode(destinationName);

        if(source == null || destination == null) {
            System.out.println("Invalid source/destination.");
            return;
        }

        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> parent = new HashMap<>();

        PriorityQueue<RouteNode> pq = new PriorityQueue<>();

        for(Node node : graph.getAdjacencyList().keySet()) {
            distance.put(node, Integer.MAX_VALUE);
        }

        distance.put(source, 0);

        pq.add(new RouteNode(source, 0));

        while(!pq.isEmpty()) {

            RouteNode current = pq.poll();

            Node currentNode = current.getNode();
            if(current.getDistance() > distance.get(currentNode)) {
                continue;
            }

            for(Edge edge :
                    graph.getAdjacencyList().get(currentNode)) {

                if(edge.isBlocked())
                    continue;

                Node neighbor = edge.getDestination();

                if(distance.get(currentNode) == Integer.MAX_VALUE)
                    continue;

                if(edge.getWeight() == Integer.MAX_VALUE)
                    continue;

                int newDistance =
                        distance.get(currentNode)
                                + edge.getWeight();

                if(newDistance < distance.get(neighbor)) {

                    distance.put(neighbor, newDistance);

                    parent.put(neighbor, currentNode);

                    pq.add(
                            new RouteNode(
                                    neighbor,
                                    newDistance
                            )
                    );
                }
            }
        }

        printPath(parent, source, destination);

        System.out.println(
                "\nTotal Cost = "
                        + distance.get(destination)
        );
    }

    private static void printPath(
            Map<Node, Node> parent,
            Node source,
            Node destination) {

        List<Node> path = new ArrayList<>();

        Set<Node> visited = new HashSet<>();

        Node current = destination;

        while(current != null) {

            if(visited.contains(current)) {
                System.out.println(
                        "ERROR: Cycle detected in parent map!"
                );
                return;
            }

            visited.add(current);

            path.add(current);

            current = parent.get(current);
        }

        Collections.reverse(path);

        System.out.println("\nShortest Path:");

        for(int i = 0; i < path.size(); i++) {

            System.out.print(path.get(i).getName());

            if(i < path.size() - 1)
                System.out.print(" -> ");
        }

        System.out.println("\nParent Map:");

        for(Map.Entry<Node, Node> entry : parent.entrySet()) {

            System.out.println(
                    entry.getKey().getName()
                            + " <- "
                            + entry.getValue().getName()
            );
        }
    }

    public static Route getShortestRoute(
            CityGraph graph,
            String sourceName,
            String destinationName) {

        Node source =
                graph.getNode(sourceName);

        Node destination =
                graph.getNode(destinationName);

        Map<Node, Integer> distance =
                new HashMap<>();

        Map<Node, Node> parent =
                new HashMap<>();

        PriorityQueue<RouteNode> pq =
                new PriorityQueue<>();

        for(Node node :
                graph.getAdjacencyList().keySet()) {

            distance.put(
                    node,
                    Integer.MAX_VALUE
            );
        }

        distance.put(source, 0);

        pq.add(
                new RouteNode(source, 0)
        );

        while(!pq.isEmpty()) {

            RouteNode current =
                    pq.poll();

            Node currentNode =
                    current.getNode();

            for(Edge edge :
                    graph.getAdjacencyList()
                            .get(currentNode)) {

                if(edge.isBlocked())
                    continue;

                Node neighbor =
                        edge.getDestination();

                int newDistance =
                        distance.get(currentNode)
                                + edge.getWeight();

                if(newDistance
                        < distance.get(neighbor)) {

                    distance.put(
                            neighbor,
                            newDistance
                    );

                    parent.put(
                            neighbor,
                            currentNode
                    );

                    pq.add(
                            new RouteNode(
                                    neighbor,
                                    newDistance
                            )
                    );
                }
            }
        }

        if(distance.get(destination)
                == Integer.MAX_VALUE) {

            return null;
        }

        List<String> path =
                new ArrayList<>();

        Node current =
                destination;

        while(current != null) {

            path.add(current.getName());

            current =
                    parent.get(current);
        }

        Collections.reverse(path);

        return new Route(
                path,
                distance.get(destination)
        );
    }

}