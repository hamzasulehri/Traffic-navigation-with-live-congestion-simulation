package gui;

import algorithms.*;
import graph.*;
import history.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
    private JTextField nameField;
    private MapPanel mapPanel;
    private ControlPanel controlPanel;
    private InfoPanel infoPanel;

    private CityGraph graph;

    public MainFrame() {

        setTitle("🚦 Smart Traffic Navigation System");
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

        mapPanel = new MapPanel();
        controlPanel = new ControlPanel();
        infoPanel = new InfoPanel();

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

                    String user =
                            controlPanel
                                    .userNameField
                                    .getText();

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

                    Route route =
                            Dijkstra.getShortestRoute(
                                    graph,
                                    source,
                                    destination
                            );

                    if(route == null) {

                        infoPanel.setStatus(
                                "⚠ No Route Found"
                        );

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
                            "✅ Route Found"
                    );

                    infoPanel.log(
                            "🧭 Route Found for "
                                    + user
                    );

                    mapPanel.setPath(
                            route.getPath()
                    );
                });

        // BFS
        controlPanel.bfsBtn.addActionListener(e -> {

            String s = (String) controlPanel.sourceBox.getSelectedItem();

            String result = BFSTraversal.bfs(graph, s);

            infoPanel.log("🔍 " + result);
        });

        controlPanel.dfsBtn.addActionListener(e -> {

            String s = (String) controlPanel.sourceBox.getSelectedItem();

            String result = DFSTraversal.dfs(graph, s);

            infoPanel.log("📡 " + result);
        });
        // BLOCK ROAD
        controlPanel.blockBtn.addActionListener(e -> {

            infoPanel.log("🚧 Road Block Activated!");
        });

        // RESET
        controlPanel.resetBtn.addActionListener(e -> {

            infoPanel.clear();
            mapPanel.setPath(null);
            controlPanel.clearFields();
        });

        add(controlPanel,BorderLayout.WEST);
        add(mapPanel,BorderLayout.CENTER);
        add(infoPanel,BorderLayout.EAST);

        setVisible(true);
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