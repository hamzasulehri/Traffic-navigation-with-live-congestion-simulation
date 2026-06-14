package algorithms;

import graph.*;

import java.util.*;

public class BFSTraversal {

    public static String bfs(
            CityGraph graph,
            String startName) {

        Node start = graph.getNode(startName);

        if(start == null)
            return "Invalid Node";

        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        StringBuilder result =
                new StringBuilder();

        queue.add(start);
        visited.add(start);

        result.append("BFS: ");

        while(!queue.isEmpty()) {

            Node current = queue.poll();

            result.append(
                    current.getName()
            ).append(" ");

            for(Edge edge :
                    graph.getAdjacencyList()
                            .get(current)) {

                Node neighbor =
                        edge.getDestination();

                if(!visited.contains(neighbor)) {

                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return result.toString();
    }
}