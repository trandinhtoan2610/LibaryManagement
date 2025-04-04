package GUI.Component.Panel;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {
    public CardPanel(String urlImage, String title, String description) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);


        ImageIcon icon = new ImageIcon(getClass().getResource(urlImage));
        icon.setImage(icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

        JTextArea descriptionLabel = new JTextArea(description);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setEditable(false);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setBorder(null);

        descriptionLabel.setMaximumSize(new Dimension(180, 50));
        add(Box.createVerticalStrut(10));
        add(imageLabel);
        add(Box.createVerticalStrut(5));
        add(titleLabel);
        add(Box.createVerticalStrut(5));
        add(descriptionLabel);
        add(Box.createVerticalGlue());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bo góc 20px
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
    }
}