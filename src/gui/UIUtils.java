package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Shared helper methods used across all panels.
 */
public class UIUtils {

    /**
     * Loads an icon image from resources/icons and scales it to the given size.
     * Returns null if the image cannot be found, so the app won't crash
     * if an icon file is missing (it just prints which file is missing).
     *
     * Place icon files inside: src/resources/icons/
     */
    public static ImageIcon loadIcon(String fileName, int width, int height) {

        java.net.URL imgUrl =
                UIUtils.class.getResource("/icons/" + fileName);

        if (imgUrl == null) {
            System.out.println("Icon not found: /icons/" + fileName);
            return null;
        }

        ImageIcon rawIcon = new ImageIcon(imgUrl);

        Image scaledImg = rawIcon.getImage().getScaledInstance(
                width, height, Image.SCALE_SMOOTH
        );

        return new ImageIcon(scaledImg);
    }
    public static JLabel createHeader(String text, String iconFileName,
                                      Color bgColor, Font font, int iconSize) {

        JLabel header = new JLabel(text);

        header.setOpaque(true);
        header.setBackground(bgColor);
        header.setForeground(Color.WHITE);
        header.setFont(font);
        header.setBorder(new EmptyBorder(8, 12, 8, 12));

        ImageIcon icon = loadIcon(iconFileName, iconSize, iconSize);

        if (icon != null) {
            header.setIcon(icon);
            header.setIconTextGap(8);
        }

        return header;
    }
}