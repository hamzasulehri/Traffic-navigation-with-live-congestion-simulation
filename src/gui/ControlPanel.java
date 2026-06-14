package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private final Color BTN_COLOR =
            new Color(52,152,219);

    private final Color HOVER_COLOR =
            new Color(41,128,185);

    public ControlPanel() {

        setPreferredSize(
                new Dimension(280,0)
        );

        setBackground(
                new Color(35,39,42)
        );

        setLayout(
                new GridLayout(0,1,10,10)
        );

        setBorder(
                new EmptyBorder(20,20,20,20)
        );

        JLabel title =
                new JLabel("Traffic Control");

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        userNameField =
                new JTextField();

        sourceBox =
                new JComboBox<>();

        destinationBox =
                new JComboBox<>();

        findRouteBtn =
                createButton("Find Route");

        bfsBtn =
                createButton("Show BFS");

        dfsBtn =
                createButton("Show DFS");

        blockBtn =
                createButton("Block Road");

        resetBtn =
                createButton("Reset");

        add(title);

        add(createLabel("User Name"));
        add(userNameField);

        add(createLabel("Source"));
        add(sourceBox);

        add(createLabel("Destination"));
        add(destinationBox);

        add(findRouteBtn);
        add(bfsBtn);
        add(dfsBtn);
        add(blockBtn);
        add(resetBtn);
    }

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setForeground(
                Color.WHITE
        );

        return label;
    }

    private JButton createButton(
            String text) {

        JButton btn =
                new JButton(text);

        btn.setBackground(
                BTN_COLOR
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
                                HOVER_COLOR
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        btn.setBackground(
                                BTN_COLOR
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