package Controller;

import algorithms.Dijkstra;
import graph.CityGraph;
import history.Route;
import history.RouteHistory;
import traffic.CongestionManager;
import gui.MainFrame;

public class AppController {

    private CityGraph graph;
    private RouteHistory routeHistory;
    private CongestionManager congestionManager;

    private MainFrame frame;

    public AppController(
            CityGraph graph,
            MainFrame frame) {

        this.graph = graph;
        this.frame = frame;

        routeHistory = new RouteHistory();

        congestionManager =
                new CongestionManager(graph);

        initializeEvents();
    }

    private void initializeEvents() {

        frame.getControlPanel()
                .findRouteBtn
                .addActionListener(e -> {

                    String user =
                            frame.getControlPanel()
                                    .userNameField
                                    .getText();

                    String source =
                            frame.getControlPanel()
                                    .sourceBox
                                    .getSelectedItem()
                                    .toString();

                    String destination =
                            frame.getControlPanel()
                                    .destinationBox
                                    .getSelectedItem()
                                    .toString();

                    Route route =
                            Dijkstra.getShortestRoute(
                                    graph,
                                    source,
                                    destination
                            );

                    if(route == null) {

                        frame.getInfoPanel()
                                .setStatus(
                                        "No Route Found"
                                );

                        return;
                    }

                    routeHistory.saveRoute(route);

                    frame.getInfoPanel()
                            .setUser(user);

                    frame.getInfoPanel()
                            .setRoute(
                                    String.join(
                                            " -> ",
                                            route.getPath()
                                    )
                            );

                    frame.getInfoPanel()
                            .setCost(
                                    route.getTotalCost()
                            );

                    frame.getInfoPanel()
                            .setStatus(
                                    "Route Found"
                            );
                });

        frame.getControlPanel()
                .resetBtn
                .addActionListener(e -> {

                    Route route =
                            routeHistory.undoRoute();

                    if(route == null)
                        return;

                    frame.getInfoPanel()
                            .setRoute(
                                    String.join(
                                            " -> ",
                                            route.getPath()
                                    )
                            );

                    frame.getInfoPanel()
                            .setCost(
                                    route.getTotalCost()
                            );

                    frame.getInfoPanel()
                            .setStatus(
                                    "Undo Successful"
                            );
                });

        frame.getControlPanel()
                .blockBtn
                .addActionListener(e -> {

                    String source =
                            frame.getControlPanel()
                                    .sourceBox
                                    .getSelectedItem()
                                    .toString();

                    String destination =
                            frame.getControlPanel()
                                    .destinationBox
                                    .getSelectedItem()
                                    .toString();

                    congestionManager.blockRoad(
                            source,
                            destination
                    );

                    frame.getInfoPanel()
                            .setStatus(
                                    "Road Blocked"
                            );
                });

        frame.getControlPanel()
                .resetBtn
                .addActionListener(e -> {

                    frame.getInfoPanel()
                            .setStatus(
                                    "System Reset"
                            );
                });
    }
}