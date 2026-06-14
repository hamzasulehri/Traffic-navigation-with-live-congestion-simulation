package history;

import java.util.List;

public class Route {

    private List<String> path;
    private int totalCost;

    public Route(List<String> path, int totalCost) {
        this.path = path;
        this.totalCost = totalCost;
    }

    public List<String> getPath() {
        return path;
    }

    public int getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {

        return "Route: "
                + String.join(" -> ", path)
                + " | Cost = "
                + totalCost;
    }
}