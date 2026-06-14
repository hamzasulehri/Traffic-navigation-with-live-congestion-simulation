package traffic;

import graph.*;

import java.util.*;

public class CongestionManager {

    private CityGraph graph;

    public CongestionManager(CityGraph graph) {
        this.graph = graph;
    }

    public void increaseTraffic(
            String source,
            String destination,
            int extraWeight) {

        updateRoadWeight(
                source,
                destination,
                extraWeight
        );

        System.out.println(
                "Traffic increased on "
                        + source
                        + " -> "
                        + destination
        );
    }

    public void blockRoad(
            String source,
            String destination) {

        Node src = graph.getNode(source);

        for(Edge edge :
                graph.getAdjacencyList().get(src)) {

            if(edge.getDestination()
                    .getName()
                    .equals(destination)) {

                edge.setBlocked(true);

                edge.setWeight(
                        Integer.MAX_VALUE
                );
            }
        }

        System.out.println(
                "Road Blocked: "
                        + source
                        + " -> "
                        + destination
        );
    }

    public void restoreRoad(
            String source,
            String destination) {

        Node src = graph.getNode(source);

        for(Edge edge :
                graph.getAdjacencyList().get(src)) {

            if(edge.getDestination()
                    .getName()
                    .equals(destination)) {

                edge.setBlocked(false);

                edge.setWeight(
                        edge.getOriginalWeight()
                );
            }
        }

        System.out.println(
                "Road Restored: "
                        + source
                        + " -> "
                        + destination
        );
    }

    private void updateRoadWeight(
            String source,
            String destination,
            int extraWeight) {

        Node src = graph.getNode(source);

        for(Edge edge :
                graph.getAdjacencyList().get(src)) {

            if(edge.getDestination()
                    .getName()
                    .equals(destination)) {

                edge.setWeight(
                        edge.getWeight()
                                + extraWeight
                );
            }
        }
    }
}