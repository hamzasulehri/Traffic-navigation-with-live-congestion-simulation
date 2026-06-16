package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    private JLabel userLabel;
    private JLabel routeLabel;
    private JLabel costLabel;
    private JLabel statusLabel;
    private JLabel congestionLabel;

    private JTextArea logArea;


    // Font size for normal text (User, Route, Cost, Status, log text)
    private static final int FONT_SIZE_NORMAL = 20;

    // Font size for header bars ("Info Panel", "Event Log")
    private static final int FONT_SIZE_HEADER = 25;

    // Icon size (width and height in pixels) for User/Route/Cost/Status icons
    private static final int ICON_SIZE = 30;

    // Icon size for header bar icons
    private static final int HEADER_ICON_SIZE = 35;



    private static final Font FONT_NORMAL =
            new Font("Times New Roman", Font.PLAIN, FONT_SIZE_NORMAL);

    private static final Font FONT_HEADER =
            new Font("Times New Roman", Font.BOLD, FONT_SIZE_HEADER);

    public InfoPanel() {

        setPreferredSize(
                new Dimension(320,0)
        );

        setLayout(
                new BorderLayout()
        );

        setBorder(
                BorderFactory.createLineBorder(
                        new Color(46,204,113), 2
                )
        );

        // HEADER BAR (replaces old TitledBorder text)
        JLabel header = UIUtils.createHeader(
                "Info Panel",
                "info_panel.png",
                new Color(46,204,113),
                FONT_HEADER,
                HEADER_ICON_SIZE
        );

        add(header, BorderLayout.NORTH);

        // ----- CONTENT AREA -----
        JPanel content = new JPanel(
                new BorderLayout()
        );

        JPanel topPanel =
                new JPanel(
                        new GridLayout(5,1,5,5)
                );

        topPanel.setBorder(
                new EmptyBorder(10,10,10,10)
        );

        userLabel =
                new JLabel("User:");

        routeLabel =
                new JLabel("Route:");

        costLabel =
                new JLabel("Cost:");

        statusLabel =
                new JLabel("Status:");

        congestionLabel =
                new JLabel("Congested Roads: 0");

        userLabel.setFont(FONT_NORMAL);
        routeLabel.setFont(FONT_NORMAL);
        costLabel.setFont(FONT_NORMAL);
        statusLabel.setFont(FONT_NORMAL);
        congestionLabel.setFont(FONT_NORMAL);


        setLabelIcon(userLabel, "user.png");
        setLabelIcon(routeLabel, "route.png");
        setLabelIcon(costLabel, "cost.png");
        setLabelIcon(statusLabel, "status.png");
        setLabelIcon(congestionLabel, "congestion.png");

        topPanel.add(userLabel);
        topPanel.add(routeLabel);
        topPanel.add(costLabel);
        topPanel.add(statusLabel);
        topPanel.add(congestionLabel);

        content.add(topPanel, BorderLayout.NORTH);

        JPanel logWrapper = new JPanel(
                new BorderLayout()
        );

        logWrapper.setBorder(
                BorderFactory.createLineBorder(
                        new Color(180,180,180), 1
                )
        );

        JLabel logHeader = UIUtils.createHeader(
                "Event Log",
                "event_log.png",
                new Color(149,165,166),
                FONT_HEADER,
                HEADER_ICON_SIZE
        );

        logWrapper.add(logHeader, BorderLayout.NORTH);

        logArea =
                new JTextArea();

        logArea.setEditable(false);

        logArea.setFont(FONT_NORMAL);

        logWrapper.add(
                new JScrollPane(logArea),
                BorderLayout.CENTER
        );

        content.add(logWrapper, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
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

    public void setCongestedRoads(int count) {
        congestionLabel.setText(
                "Congested Roads: " + count
        );
    }

    public void log(String text) {
        logArea.append(text + "\n");
    }

    /**
     * Returns the full Event Log text exactly as shown on screen,
     * used by the Save Log button to write it out to a .txt file.
     */
    public String getLogText() {
        return logArea.getText();
    }

    /**
     * Returns the current User / Route / Cost / Status / Congested Roads
     * summary lines, used by the Save Log button so the saved file
     * captures the full snapshot, not just the event history.
     */
    public String getSummaryText() {

        return userLabel.getText() + "\n"
                + routeLabel.getText() + "\n"
                + costLabel.getText() + "\n"
                + statusLabel.getText() + "\n"
                + congestionLabel.getText() + "\n";
    }

    public void clear() {
        userLabel.setText("User:");
        routeLabel.setText("Route:");
        costLabel.setText("Cost:");
        statusLabel.setText("Status:");
        congestionLabel.setText("Congested Roads: 0");
        logArea.setText("");
    }


    private void setLabelIcon(JLabel label, String iconFileName) {

        ImageIcon icon = UIUtils.loadIcon(iconFileName, ICON_SIZE, ICON_SIZE);

        if (icon != null) {
            label.setIcon(icon);
            label.setIconTextGap(8);
        }
    }
}