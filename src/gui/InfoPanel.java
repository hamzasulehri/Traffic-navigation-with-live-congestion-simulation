package gui;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    private JLabel userLabel;
    private JLabel routeLabel;
    private JLabel costLabel;
    private JLabel statusLabel;

    private JTextArea logArea;

    public InfoPanel() {

        setPreferredSize(
                new Dimension(320,0)
        );

        setLayout(
                new BorderLayout()
        );

        JPanel topPanel =
                new JPanel(
                        new GridLayout(4,1)
                );

        userLabel =
                new JLabel("User:");

        routeLabel =
                new JLabel("Route:");

        costLabel =
                new JLabel("Cost:");

        statusLabel =
                new JLabel("Status:");

        topPanel.add(userLabel);
        topPanel.add(routeLabel);
        topPanel.add(costLabel);
        topPanel.add(statusLabel);

        logArea =
                new JTextArea();

        logArea.setEditable(false);

        add(
                topPanel,
                BorderLayout.NORTH
        );

        add(
                new JScrollPane(logArea),
                BorderLayout.CENTER
        );
    }

    public void setUser(String user) {
        userLabel.setText(
                "User: " + user
        );
    }

    public void setRoute(String route) {
        routeLabel.setText(
                "Route: " + route
        );
    }

    public void setCost(int cost) {
        costLabel.setText(
                "Cost: " + cost
        );
    }

    public void setStatus(String status) {
        statusLabel.setText(
                "Status: " + status
        );
    }

    public void log(String text) {
        logArea.append(text + "\n");
    }

    public void clear() {
        logArea.setText("");
    }
}