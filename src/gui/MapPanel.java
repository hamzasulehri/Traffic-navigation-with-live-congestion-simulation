package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapPanel extends JPanel {

    private List<String> highlightedPath;

    public void setPath(List<String> path) {
        this.highlightedPath = path;
        repaint();
    }

    public MapPanel() {
        setBackground(new Color(245,245,245));
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

        g2.drawLine(x1,y1,x2,y2);
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

        g2.setColor(
                new Color(
                        52,
                        152,
                        219
                )
        );
        g2.fillOval(x-18,y-18,36,36);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.drawString(name,x-5,y+5);
    }
}