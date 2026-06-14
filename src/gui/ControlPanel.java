package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPanel extends JPanel {

    public JTextField userNameField;

    public JComboBox<String> sourceBox;
    public JComboBox<String> destinationBox;

    public JButton findRouteBtn;
    public JButton bfsBtn;
    public JButton dfsBtn;
    public JButton blockBtn;
    public JButton resetBtn;

    // =====================================================================
    // SIZE SETTINGS - change these numbers to make text/icons bigger or smaller
    // =====================================================================

    // Font size for normal text (labels, fields, buttons)
    private static final int FONT_SIZE_NORMAL = 20;

    // Font size for the big "Traffic Congestion" heading
    private static final int FONT_SIZE_TITLE = 25;

    // Font size for the panel header bar ("Control Panel")
    private static final int FONT_SIZE_HEADER = 20;

    // Icon size (width and height in pixels) for labels and buttons
    private static final int ICON_SIZE = 30;

    // Icon size for the big "Traffic Congestion" heading icon
    private static final int TITLE_ICON_SIZE = 38;

    // Icon size for the panel header bar icon
    private static final int HEADER_ICON_SIZE = 35;

    // =====================================================================

    private static final Font FONT_NORMAL =
            new Font("Times New Roman", Font.PLAIN, FONT_SIZE_NORMAL);

    private static final Font FONT_TITLE =
            new Font("Times New Roman", Font.BOLD, FONT_SIZE_TITLE);

    private static final Font FONT_HEADER =
            new Font("Times New Roman", Font.BOLD, FONT_SIZE_HEADER);

    public ControlPanel() {

        setPreferredSize(
                new Dimension(280,0)
        );

        setLayout(
                new BorderLayout()
        );

        // Plain line border around the whole panel (header bar acts as the title)
        setBorder(
                BorderFactory.createLineBorder(
                        new Color(52,152,219), 2
                )
        );

        // ----- HEADER BAR (replaces old TitledBorder text) -----
        JLabel header = UIUtils.createHeader(
                "Control Panel",
                "control_panel.png",
                new Color(52,152,219),
                FONT_HEADER,
                HEADER_ICON_SIZE
        );

        add(header, BorderLayout.NORTH);

        // ----- CONTENT AREA -----
        JPanel content = new JPanel(
                new GridLayout(0,1,10,10)
        );

        content.setBackground(
                new Color(35,39,42)
        );

        content.setBorder(
                new EmptyBorder(15,15,15,15)
        );

        JLabel title =
                new JLabel("Traffic Congestion");

        title.setForeground(Color.WHITE);

        title.setFont(FONT_TITLE);

        ImageIcon titleIcon = UIUtils.loadIcon("traffic.png", TITLE_ICON_SIZE, TITLE_ICON_SIZE);

        if (titleIcon != null) {
            title.setIcon(titleIcon);
            title.setIconTextGap(10);
        }

        userNameField =
                new JTextField();

        userNameField.setFont(FONT_NORMAL);

        sourceBox =
                new JComboBox<>();

        sourceBox.setFont(FONT_NORMAL);

        destinationBox =
                new JComboBox<>();

        destinationBox.setFont(FONT_NORMAL);

        findRouteBtn =
                createButton("Find Route", "route.png",
                        new Color(46,204,113), new Color(39,174,96));

        bfsBtn =
                createButton("Show BFS", "bfs.png",
                        new Color(155,89,182), new Color(142,68,173));

        dfsBtn =
                createButton("Show DFS", "dfs.png",
                        new Color(241,196,15), new Color(212,172,13));

        blockBtn =
                createButton("Block Road", "block.png",
                        new Color(231,76,60), new Color(192,57,43));

        resetBtn =
                createButton("Reset", "reset.png",
                        new Color(26,188,156), new Color(22,160,133));

        content.add(title);

        content.add(createIconLabel("User Name", "username.png"));
        content.add(userNameField);

        content.add(createIconLabel("Source", "source.png"));
        content.add(sourceBox);

        content.add(createIconLabel("Destination", "destination.png"));
        content.add(destinationBox);

        content.add(findRouteBtn);
        content.add(bfsBtn);
        content.add(dfsBtn);
        content.add(blockBtn);
        content.add(resetBtn);

        add(content, BorderLayout.CENTER);
    }
    private JLabel createIconLabel(String text, String iconFileName) {

        JLabel label =
                new JLabel(text);

        label.setForeground(
                Color.WHITE
        );

        label.setFont(FONT_NORMAL);

        ImageIcon icon = UIUtils.loadIcon(iconFileName, ICON_SIZE, ICON_SIZE);

        if (icon != null) {
            label.setIcon(icon);
            label.setIconTextGap(8);
        }

        return label;
    }
    private JButton createButton(
            String text, String iconFileName,
            Color baseColor, Color hoverColor) {

        JButton btn =
                new JButton(text);

        btn.setFont(FONT_NORMAL);

        ImageIcon icon = UIUtils.loadIcon(iconFileName, ICON_SIZE, ICON_SIZE);

        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(8);
        }

        btn.setBackground(
                baseColor
        );

        btn.setForeground(
                Color.WHITE
        );

        btn.setFocusPainted(false);

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        btn.setBackground(
                                hoverColor
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        btn.setBackground(
                                baseColor
                        );
                    }
                });

        return btn;
    }

    public void clearFields() {
        userNameField.setText("");
        sourceBox.setSelectedIndex(0);
        destinationBox.setSelectedIndex(0);
    }
}