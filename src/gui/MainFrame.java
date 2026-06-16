package gui;

import algorithms.*;
import graph.*;
import history.Route;
import traffic.CongestionEvent;
import traffic.CongestionManager;
import traffic.CongestionQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainFrame extends JFrame {
    private JTextField nameField;
    private MapPanel mapPanel;
    private ControlPanel controlPanel;
    private InfoPanel infoPanel;

    private CityGraph graph;

    // Handles blocking/unblocking roads and tracking congestion levels
    private CongestionManager congestionManager;

    // Holds incoming congestion events (block road, simulated traffic)
    // and processes them in FIFO order, as described in the project design
    private CongestionQueue congestionQueue;

    public MainFrame() {

        setTitle(" Smart Traffic Navigation System");
        setSize(1300,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set a simple generated window icon (no external image file needed)
        setIconImage(createAppIcon());

        // GRAPH
        graph = new CityGraph();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");

        graph.addEdge("A","B",2);
        graph.addEdge("A","C",3);
        graph.addEdge("B","D",4);
        graph.addEdge("C","D",1);

        congestionManager = new CongestionManager(graph);
        congestionQueue = new CongestionQueue();

        mapPanel = new MapPanel();
        controlPanel = new ControlPanel();
        infoPanel = new InfoPanel();

        // Let the map read live congestion / blocked state from the graph
        mapPanel.setGraph(graph);

        for(Node node :
                graph.getAdjacencyList()
                        .keySet()) {

            controlPanel.sourceBox
                    .addItem(
                            node.getName()
                    );

            controlPanel.destinationBox
                    .addItem(
                            node.getName()
                    );
        }

        // ROUTE (DIJKSTRA)
        controlPanel.findRouteBtn
                .addActionListener(e -> {

                    try {

                        if (isTextFieldEmpty(controlPanel.userNameField, "User Name")) {
                            return;
                        }

                        if (isSelectionEmpty(controlPanel.sourceBox, "Source")) {
                            return;
                        }

                        if (isSelectionEmpty(controlPanel.destinationBox, "Destination")) {
                            return;
                        }

                        String user =
                                controlPanel
                                        .userNameField
                                        .getText()
                                        .trim();

                        String source =
                                (String)
                                        controlPanel
                                                .sourceBox
                                                .getSelectedItem();

                        String destination =
                                (String)
                                        controlPanel
                                                .destinationBox
                                                .getSelectedItem();

                        if (source.equals(destination)) {

                            showInputError("Source and Destination cannot be the same.");
                            infoPanel.setStatus("Error: Source and Destination are the same");
                            return;
                        }

                        Route route =
                                Dijkstra.getShortestRoute(
                                        graph,
                                        source,
                                        destination
                                );

                        if(route == null) {

                            infoPanel.setStatus(
                                    " No Route Found"
                            );

                            infoPanel.log(
                                    "No route found from "
                                            + source
                                            + " to "
                                            + destination
                            );

                            mapPanel.setPath(null);

                            return;
                        }

                        infoPanel.setUser(user);

                        infoPanel.setRoute(
                                String.join(
                                        " -> ",
                                        route.getPath()
                                )
                        );

                        infoPanel.setCost(
                                route.getTotalCost()
                        );

                        infoPanel.setStatus(
                                "Route Found"
                        );

                        infoPanel.log(
                                " Route Found for "
                                        + user
                        );

                        mapPanel.setPath(
                                route.getPath()
                        );

                    } catch (Exception ex) {

                        showError("An error occurred while finding the route: " + ex.getMessage());

                        infoPanel.setStatus("Error finding route");
                        infoPanel.log("Error: " + ex.getMessage());
                    }
                });

        // BFS
        controlPanel.bfsBtn.addActionListener(e -> {

            try {

                if (isTextFieldEmpty(controlPanel.userNameField, "User Name")) {
                    return;
                }

                if (isSelectionEmpty(controlPanel.sourceBox, "Source")) {
                    return;
                }

                String s = (String) controlPanel.sourceBox.getSelectedItem();

                String result = BFSTraversal.bfs(graph, s);

                infoPanel.setStatus("BFS Running");

                infoPanel.log("🔍 " + result);

            } catch (Exception ex) {

                showError("An error occurred while running BFS: " + ex.getMessage());

                infoPanel.setStatus("Error running BFS");
            }
        });

        controlPanel.dfsBtn.addActionListener(e -> {

            try {

                if (isTextFieldEmpty(controlPanel.userNameField, "User Name")) {
                    return;
                }

                if (isSelectionEmpty(controlPanel.sourceBox, "Source")) {
                    return;
                }

                String s = (String) controlPanel.sourceBox.getSelectedItem();

                String result = DFSTraversal.dfs(graph, s);

                infoPanel.setStatus("DFS Running");

                infoPanel.log(" " + result);

            } catch (Exception ex) {

                showError("An error occurred while running DFS: " + ex.getMessage());

                infoPanel.setStatus("Error running DFS");
            }
        });

        // SIMULATE CONGESTION
        controlPanel.simulateBtn.addActionListener(e -> {

            try {

                if (isTextFieldEmpty(controlPanel.userNameField, "User Name")) {
                    return;
                }

                CongestionEvent event = congestionManager.simulateRandomCongestion();

                if (event == null) {

                    infoPanel.log("No roads available to simulate congestion on.");

                    return;
                }

                congestionQueue.addEvent(event);

                CongestionEvent processed = congestionQueue.processEvent();

                infoPanel.log(processed.toString());

                infoPanel.setStatus("Congestion Simulated");

                infoPanel.setCongestedRoads(
                        congestionManager.getCongestedRoadCount()
                );

                // Redraw the map so the affected road turns yellow
                mapPanel.refreshCongestion();

            } catch (Exception ex) {

                showError("An error occurred while simulating congestion: " + ex.getMessage());

                infoPanel.setStatus("Error simulating congestion");
            }
        });

        // BLOCK ROAD
        controlPanel.blockBtn.addActionListener(e -> {

            try {

                if (isTextFieldEmpty(controlPanel.userNameField, "User Name")) {
                    return;
                }

                if (isSelectionEmpty(controlPanel.sourceBox, "Source")) {
                    return;
                }

                if (isSelectionEmpty(controlPanel.destinationBox, "Destination")) {
                    return;
                }

                String source =
                        (String) controlPanel.sourceBox.getSelectedItem();

                String destination =
                        (String) controlPanel.destinationBox.getSelectedItem();

                if (source.equals(destination)) {

                    showInputError("Source and Destination cannot be the same.");
                    infoPanel.setStatus("Error: Source and Destination are the same");
                    return;
                }

                congestionManager.blockRoad(source, destination);

                CongestionEvent event =
                        new CongestionEvent("BLOCKED", source, destination);

                congestionQueue.addEvent(event);

                CongestionEvent processed = congestionQueue.processEvent();

                infoPanel.log(processed.toString());

                infoPanel.setStatus("Road Blocked");

                infoPanel.setCongestedRoads(
                        congestionManager.getCongestedRoadCount()
                );

                // Redraw the map so the blocked road turns red
                mapPanel.refreshCongestion();

                // If the currently displayed route used this road, it is no
                // longer valid, so clear the highlighted path on the map.
                mapPanel.setPath(null);

            } catch (Exception ex) {

                showError("An error occurred while blocking the road: " + ex.getMessage());

                infoPanel.setStatus("Error blocking road");
            }
        });

        // RESET
        controlPanel.resetBtn.addActionListener(e -> {

            try {

                // Clear all congestion / blocked roads back to normal
                congestionManager.restoreAll();

                while (!congestionQueue.isEmpty()) {
                    congestionQueue.processEvent();
                }

                infoPanel.clear();
                mapPanel.setPath(null);
                mapPanel.refreshCongestion();
                controlPanel.clearFields();

            } catch (Exception ex) {

                showError("An error occurred while resetting: " + ex.getMessage());
            }
        });

        // SAVE LOG
        controlPanel.saveLogBtn.addActionListener(e -> {

            try {

                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setDialogTitle("Save Event Log");

                fileChooser.setSelectedFile(
                        new File("traffic_log.txt")
                );

                int userChoice = fileChooser.showSaveDialog(this);

                if (userChoice != JFileChooser.APPROVE_OPTION) {

                    // User clicked Cancel, so do nothing
                    return;
                }

                File fileToSave = fileChooser.getSelectedFile();

                // Make sure the file always ends in .txt
                if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {

                    fileToSave = new File(
                            fileToSave.getParentFile(),
                            fileToSave.getName() + ".txt"
                    );
                }

                try (FileWriter writer = new FileWriter(fileToSave)) {

                    writer.write("Smart Traffic Navigation System - Saved Report\n");
                    writer.write("================================================\n\n");

                    writer.write(infoPanel.getSummaryText());

                    writer.write("\nEvent Log\n");
                    writer.write("---------\n");

                    writer.write(infoPanel.getLogText());
                }

                infoPanel.log("Log saved to " + fileToSave.getAbsolutePath());

                JOptionPane.showMessageDialog(
                        this,
                        "Log saved to:\n" + fileToSave.getAbsolutePath(),
                        "Saved",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IOException ex) {

                showError("Could not save the log file: " + ex.getMessage());

            } catch (Exception ex) {

                showError("An error occurred while saving the log: " + ex.getMessage());
            }
        });

        add(controlPanel,BorderLayout.WEST);
        add(mapPanel,BorderLayout.CENTER);
        add(infoPanel,BorderLayout.EAST);

        setVisible(true);
    }

    /**
     * Checks that a text field is not empty (or whitespace-only).
     * If it is empty, shows a warning dialog and returns true so the
     * calling action listener can stop early.
     */
    private boolean isTextFieldEmpty(JTextField field, String fieldLabel) {

        if (field.getText() == null || field.getText().trim().isEmpty()) {

            showInputError(fieldLabel + " cannot be empty.");

            infoPanel.setStatus("Error: " + fieldLabel + " is empty");

            return true;
        }

        return false;
    }

    /**
     * Checks that a combo box has a selected value.
     * If nothing is selected, shows a warning dialog and returns true so
     * the calling action listener can stop early.
     */
    private boolean isSelectionEmpty(JComboBox<String> box, String fieldLabel) {

        if (box.getSelectedItem() == null) {

            showInputError("Please select a " + fieldLabel + ".");

            infoPanel.setStatus("Error: " + fieldLabel + " not selected");

            return true;
        }

        return false;
    }

    private void showInputError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private Image createAppIcon() {

        BufferedImage icon =
                new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = icon.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Background circle (road sign style)
        g2.setColor(new Color(52,152,219));
        g2.fillOval(0,0,32,32);

        // Draw a simple "road" line through the circle
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        g2.drawLine(6,22,26,10);

        // Small dot for a "node" on the road
        g2.setColor(new Color(46,204,113));
        g2.fillOval(20,8,6,6);

        g2.dispose();

        return icon;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

    public CityGraph getGraph() {
        return graph;
    }
}