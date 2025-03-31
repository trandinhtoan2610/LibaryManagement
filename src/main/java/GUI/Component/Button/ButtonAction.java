package GUI.Component.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonAction extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressColor;
    private Color currentColor;
    private int borderRadius;

    public ButtonAction() {
        this("", new Color(0, 120, 215), new Color(0, 100, 190), new Color(0, 80, 170), 5);
    }

    public ButtonAction(String text) {
        this(text, new Color(0, 120, 215), new Color(0, 100, 190), new Color(0, 80, 170), 5);
    }

    public ButtonAction(String text, Color normalColor, Color hoverColor, Color pressColor, int borderRadius) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.pressColor = pressColor;
        this.borderRadius = borderRadius;
        this.currentColor = normalColor;

        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setPreferredSize(new Dimension(120, 35));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentColor = normalColor;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                currentColor = pressColor;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền với bo góc
        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

        // Vẽ text
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    // Các phương thức getter/setter
    public Color getNormalColor() { return normalColor; }
    public void setNormalColor(Color normalColor) {
        this.normalColor = normalColor;
        this.currentColor = normalColor;
        repaint();
    }

    public Color getHoverColor() { return hoverColor; }
    public void setHoverColor(Color hoverColor) { this.hoverColor = hoverColor; }

    public Color getPressColor() { return pressColor; }
    public void setPressColor(Color pressColor) { this.pressColor = pressColor; }

    public int getBorderRadius() { return borderRadius; }
    public void setBorderRadius(int borderRadius) { this.borderRadius = borderRadius; }
}