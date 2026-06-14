package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class MapPanel extends JPanel {

    private List<String> highlightedPath;

    // =====================================================================
    // SIZE SETTINGS - change these numbers to make text/icons bigger or smaller
    // =====================================================================

    // Font size for node labels (A, B, C, D) drawn on the map
    private static final int NODE_FONT_SIZE = 20;

    // Font size for the header bar ("City Map")
    private static final int FONT_SIZE_HEADER = 25;

    // Icon size for the header bar icon
    private static final int HEADER_ICON_SIZE = 30;

    // Vertical space reserved for the header bar so the map drawing
    // doesn't overlap it. Increase this if you increase header font/icon size.
    private static final int Y_OFFSET = 50;

    // =====================================================================

    private static final Font NODE_FONT =
            new Font("Times New Roman", Font.BOLD, NODE_FONT_SIZE);

    private static final Font FONT_HEADER =
            new Font("Times New Roman", Font.BOLD, FONT_SIZE_HEADER);

    public void setPath(List<String> path) {
        this.highlightedPath = path;
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

        // draw roads
        g2.setStroke(new BasicStroke(5));

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

        if (highlightedPath != null &&
                highlightedPath.contains(a) &&
                highlightedPath.contains(b)) {
            g2.setColor(new Color(
                    46,
                    204,
                    113
            ));
        } else {
            g2.setColor(new Color(
                    120,
                    120,
                    120
            ));
        }

        g2.drawLine(x1, y1 + Y_OFFSET, x2, y2 + Y_OFFSET);
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