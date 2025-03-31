package GUI.Component.Button;

import com.kitfox.svg.app.beans.SVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URI;

public class ButtonBack extends JButton {
    private Color normalColor = new Color(230, 242, 255);
    private Color hoverColor = new Color(210, 230, 255);
    private Color pressedColor = new Color(190, 220, 255);

    public ButtonBack() {
        setCustomStyle();
        addHoverEffect();
        setSVGIcon("icons/back.svg");
    }
    private void setSVGIcon(String resourcePath) {
        try {
            SVGIcon icon = new SVGIcon();
            URI uri = getClass().getClassLoader().getResource(resourcePath).toURI();
            icon.setSvgURI(uri);
            icon.setAntiAlias(true);
            icon.setScaleToFit(true);
            icon.setPreferredSize(new Dimension(25, 25));
            setIcon(icon);
            setText("");
        } catch (Exception e) {
            e.printStackTrace();
            setText("Back");
        }
    }
    private void setCustomStyle() {
        setSize(100, 100);
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setBorder(new EmptyBorder(5, 15, 5, 15));
        setContentAreaFilled(false);
        setFont(getFont().deriveFont(Font.BOLD));
        setFocusPainted(false);
    }

    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor); // Đổi màu khi hover
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground().darker());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.dispose();
    }
}