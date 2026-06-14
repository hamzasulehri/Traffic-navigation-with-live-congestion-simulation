package algorithms;

import graph.*;

import java.util.*;

public class DFSTraversal {

    public static String dfs(CityGraph graph, String startName) {

        Node start = graph.getNode(startName);
        if (start == null) return "";

        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();

        StringBuilder result = new StringBuilder();

        stack.push(start);
        result.append("DFS: ");

        while (!stack.isEmpty()) {

            Node current = stack.pop();

            if (visited.contains(current))
                continue;

            visited.add(current);
            result.append(current.getName()).append(" ");

            for (Edge edge : graph.getAdjacencyList().get(current)) {

                Node neighbor = edge.getDestination();

                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }

        return result.toString();
    }
}