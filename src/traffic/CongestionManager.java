package traffic;

import graph.*;

import java.util.*;

public class CongestionManager {

    private CityGraph graph;

    private static final Random RANDOM = new Random();

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

        // The graph stores each road as two directed Edge objects
        // (source -> destination AND destination -> source), so both
        // sides must be blocked or the road stays usable in reverse.
        setBlockedOneDirection(source, destination, true);
        setBlockedOneDirection(destination, source, true);

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

        setBlockedOneDirection(source, destination, false);
        setBlockedOneDirection(destination, source, false);

        System.out.println(
                "Road Restored: "
                        + source
                        + " -> "
                        + destination
        );
    }

    /**
     * Sets the blocked state on the single directed edge from -> to,
     * restoring its original weight when unblocking. Called twice
     * (once per direction) by blockRoad/restoreRoad so the whole
     * physical road is affected, not just one direction of it.
     */
    private void setBlockedOneDirection(
            String from,
            String to,
            boolean blocked) {

        Node src = graph.getNode(from);

        if (src == null) {
            return;
        }

        for (Edge edge :
                graph.getAdjacencyList().get(src)) {

            if (edge.getDestination()
                    .getName()
                    .equals(to)) {

                edge.setBlocked(blocked);

                edge.setWeight(
                        blocked
                                ? Integer.MAX_VALUE
                                : edge.getOriginalWeight()
                );
            }
        }
    }

    /**
     * Picks a random road in the city and increases its weight, simulating
     * a live congestion event (e.g. heavy traffic appearing on that road).
     * Returns a CongestionEvent describing what changed, or null if there
     * are no roads to pick from.
     */
    public CongestionEvent simulateRandomCongestion() {

        List<Node> nodes = new ArrayList<>(graph.getAdjacencyList().keySet());

        if (nodes.isEmpty()) {
            return null;
        }

        // Try a limited number of times to find a road that isn't already blocked
        for (int attempt = 0; attempt < 20; attempt++) {

            Node source = nodes.get(RANDOM.nextInt(nodes.size()));

            List<Edge> edges = graph.getAdjacencyList().get(source);

            if (edges == null || edges.isEmpty()) {
                continue;
            }

            Edge edge = edges.get(RANDOM.nextInt(edges.size()));

            if (edge.isBlocked()) {
                continue;
            }

            int extraWeight = RANDOM.nextInt(5) + 1;

            increaseTraffic(
                    source.getName(),
                    edge.getDestination().getName(),
                    extraWeight
            );

            return new CongestionEvent(
                    "CONGESTION",
                    source.getName(),
                    edge.getDestination().getName()
            );
        }

        return null;
    }

    /**
     * Counts how many UNIQUE physical roads in the city are currently
     * blocked or running above their normal (original) travel time.
     *
     * The graph stores each road as two directed Edge objects (A->B and
     * B->A), so a naive loop over every edge would count one congested
     * road twice. Instead, each road is normalized to a single key
     * (e.g. "A-B", with names sorted so "A-B" and "B-A" collapse to the
     * same key) and stored in a Set, which only keeps unique entries.
     */
    public int getCongestedRoadCount() {

        Set<String> uniqueRoads = new HashSet<>();

        for (Node node : graph.getAdjacencyList().keySet()) {

            for (Edge edge : graph.getAdjacencyList().get(node)) {

                if (edge.isBlocked() || edge.getWeight() > edge.getOriginalWeight()) {

                    String a = node.getName();
                    String b = edge.getDestination().getName();

                    String road =
                            (a.compareTo(b) < 0)
                                    ? a + "-" + b
                                    : b + "-" + a;

                    uniqueRoads.add(road);
                }
            }
        }

        return uniqueRoads.size();
    }

    /**
     * Restores every road in the city to its original, uncongested state.
     * Used by the Reset button.
     */
    public void restoreAll() {

        for (List<Edge> edges : graph.getAdjacencyList().values()) {

            for (Edge edge : edges) {

                edge.setBlocked(false);
                edge.setWeight(edge.getOriginalWeight());
            }
        }
    }

    private void updateRoadWeight(
            String source,
            String destination,
            int extraWeight) {

        // Same two-direction fix as blockRoad/restoreRoad: a road is
        // stored as two directed Edge objects, so congestion has to be
        // applied to both source->destination and destination->source,
        // otherwise only one direction shows as congested.
        addWeightOneDirection(source, destination, extraWeight);
        addWeightOneDirection(destination, source, extraWeight);
    }

    private void addWeightOneDirection(
            String from,
            String to,
            int extraWeight) {

        Node src = graph.getNode(from);

        if (src == null) {
            return;
        }

        for (Edge edge :
                graph.getAdjacencyList().get(src)) {

            if (edge.getDestination()
                    .getName()
                    .equals(to)) {

                edge.setWeight(
                        edge.getWeight()
                                + extraWeight
                );
            }
        }
    }
}