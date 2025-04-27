package GUI.Component.Panel.Statistics.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class BoxDashBoard extends JPanel {
    private int cornerRadius = 15; // Độ bo góc
    private JLabel valueLabel;

    public BoxDashBoard(String header, String value) {
        setPreferredSize(new Dimension(200, 150));
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header panel with solid color background
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(200, 230, 255)); // Light blue

        // Header title
        JLabel titleLabel = new JLabel(header, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        titleLabel.setForeground(new Color(0, 0, 80)); // Dark blue
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Value label
        valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(new Color(71, 71, 71)); // Dark green for value
        valueLabel.setBorder(BorderFactory.createEmptyBorder(-10, 0, 0, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
        setBackground(new Color(240, 240, 240));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Rounded rectangle for the panel background
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Subtle shadow border
        g2d.setColor(new Color(200, 200, 200)); // Slightly darker gray for shadow
        g2d.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius));

        g2d.dispose();

        super.paintComponent(g);
    }
    public void setValue(String value){
        this.valueLabel.setText(value);
        this.repaint();
        this.revalidate();
    }
}
