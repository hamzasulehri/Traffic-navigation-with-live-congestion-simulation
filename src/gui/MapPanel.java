package gui;

import graph.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class MapPanel extends JPanel {

    private List<String> highlightedPath;

    // Reference to the live city graph so the map can read current
    // congestion / blocked status for every road and color it accordingly.
    private CityGraph graph;

    // Font size for node labels (A, B, C, D) drawn on the map
    private static final int NODE_FONT_SIZE = 20;

    // Font size for the header bar ("City Map")
    private static final int FONT_SIZE_HEADER = 25;

    // Icon size for the header bar icon
    private static final int HEADER_ICON_SIZE = 30;

    private static final int Y_OFFSET = 50;

    // ----- Congestion color legend -----
    // Green  = no congestion, travel is fast
    // Yellow = moderate congestion, some delay
    // Red    = heavy congestion or road blocked
    // Blue   = currently selected route from A to B
    // Grey   = unknown / no graph data available for this road
    private static final Color COLOR_CLEAR     = new Color(46,204,113);
    private static final Color COLOR_CONGESTED = new Color(241,196,15);
    private static final Color COLOR_BLOCKED   = new Color(231,76,60);
    private static final Color COLOR_PATH      = new Color(52,152,219);
    private static final Color COLOR_UNKNOWN   = new Color(120,120,120);


    private static final Font NODE_FONT =
            new Font("Times New Roman", Font.BOLD, NODE_FONT_SIZE);

    private static final Font FONT_HEADER =
            new Font("Times New Roman", Font.BOLD, FONT_SIZE_HEADER);

    public void setPath(List<String> path) {
        this.highlightedPath = path;
        repaint();
    }

    /**
     * Gives the map access to the live CityGraph so road colors can
     * reflect real congestion / blocked state.
     */
    public void setGraph(CityGraph graph) {
        this.graph = graph;
        repaint();
    }

    /**
     * Call this after any congestion change (block, simulate, restore)
     * so the map redraws with the latest road colors.
     */
    public void refreshCongestion() {
        repaint();
    }

    public MapPanel() {

        setBackground(new Color(245,245,245));

        setLayout(
                new BorderLayout()
        );

        // Plain line border around the whole panel (header bar acts as the title)
        setBorder(
                BorderFactory.createLineBorder(
                        new Color(120,120,120), 2
                )
        );

        // ----- HEADER BAR (replaces old TitledBorder text) -----
        JLabel header = UIUtils.createHeader(
                "City Map",
                "citymap.png",
                new Color(52,73,94),
                FONT_HEADER,
                HEADER_ICON_SIZE
        );

        add(header, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawRoad(g2, "A","B",150,100,350,100);
        drawRoad(g2, "B","D",350,100,500,250);
        drawRoad(g2, "A","C",150,100,150,300);
        drawRoad(g2, "C","D",150,300,500,250);

        drawNode(g2,150,100,"A");
        drawNode(g2,350,100,"B");
        drawNode(g2,150,300,"C");
        drawNode(g2,500,250,"D");
    }

    private void drawRoad(Graphics2D g2,
                          String a, String b,
                          int x1,int y1,int x2,int y2) {

        if (highlightedPath != null && isRoadInPath(a, b)) {

            // Selected route is always shown in blue, on top of congestion colors
            g2.setStroke(new BasicStroke(7));
            g2.setColor(COLOR_PATH);

        } else {

            g2.setStroke(new BasicStroke(5));
            g2.setColor(getRoadColor(a, b));
        }

        g2.drawLine(x1, y1 + Y_OFFSET, x2, y2 + Y_OFFSET);
    }

    /**
     * Looks up the live edge for this road and returns the color that
     * represents its current congestion state.
     */
    private Color getRoadColor(String a, String b) {

        Edge edge = findEdge(a, b);

        if (edge == null) {
            return COLOR_UNKNOWN;
        }

        if (edge.isBlocked()) {
            return COLOR_BLOCKED;
        }

        if (edge.getWeight() > edge.getOriginalWeight()) {
            return COLOR_CONGESTED;
        }

        return COLOR_CLEAR;
    }

    /**
     * Finds the Edge object that connects node a to node b (checking
     * both directions, since roads may only be stored one-way).
     */
    private Edge findEdge(String a, String b) {

        if (graph == null) {
            return null;
        }

        Edge edge = findEdgeOneWay(a, b);

        if (edge != null) {
            return edge;
        }

        return findEdgeOneWay(b, a);
    }

    private Edge findEdgeOneWay(String from, String to) {

        Node fromNode = graph.getNode(from);

        if (fromNode == null) {
            return null;
        }

        List<Edge> edges = graph.getAdjacencyList().get(fromNode);

        if (edges == null) {
            return null;
        }

        for (Edge edge : edges) {

            if (edge.getDestination().getName().equals(to)) {
                return edge;
            }
        }

        return null;
    }

    private boolean isRoadInPath(
            String a,
            String b) {

        if(highlightedPath == null)
            return false;

        for(int i = 0;
            i < highlightedPath.size()-1;
            i++) {

            String current =
                    highlightedPath.get(i);

            String next =
                    highlightedPath.get(i+1);

            if(current.equals(a)
                    && next.equals(b))
                return true;

            if(current.equals(b)
                    && next.equals(a))
                return true;
        }

        return false;
    }

    private void drawNode(Graphics2D g2,int x,int y,String name) {

        int drawY = y + Y_OFFSET;

        g2.setColor(
                new Color(
                        52,
                        152,
                        219
                )
        );
        g2.fillOval(x-18, drawY-18, 36, 36);

        g2.setColor(Color.WHITE);
        g2.setFont(NODE_FONT);
        g2.drawString(name, x-5, drawY+5);
    }
}