package history;

import java.util.Stack;

public class RouteHistory {

    private Stack<Route> history;

    public RouteHistory() {
        history = new Stack<>();
    }

    public void saveRoute(Route route) {

        history.push(route);

        System.out.println(
                "Route Saved: "
                        + route
        );
    }

    public Route undoRoute() {

        if(history.size() <= 1) {

            System.out.println(
                    "No previous route available!"
            );

            return history.isEmpty()
                    ? null
                    : history.peek();
        }

        history.pop();

        Route previous =
                history.peek();

        System.out.println(
                "Reverted To: "
                        + previous
        );

        return previous;
    }

    public Route getCurrentRoute() {

        if(history.isEmpty())
            return null;

        return history.peek();
    }

    public void showHistory() {

        System.out.println(
                "\nRoute History:"
        );

        for(Route route : history) {

            System.out.println(route);
        }
    }
}