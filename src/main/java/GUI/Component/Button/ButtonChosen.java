package GUI.Component.Button;

import com.kitfox.svg.app.beans.SVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;

public class ButtonChosen extends JButton {
    private Color hoverBackground = new Color(240, 240, 240);
    private Color pressedBackground = new Color(220, 220, 220);

    public ButtonChosen() {
        try {
            URL iconUrl = getClass().getResource("/icons/dot.svg");
            if (iconUrl != null) {
                SVGIcon icon = new SVGIcon();
                icon.setSvgURI(URI.create(iconUrl.toString()));
                icon.setPreferredSize(new Dimension(30, 30));
                this.setIcon(icon);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        // Basic setup
        this.setPreferredSize(new Dimension(55, 40));
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add listeners
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackground);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackground);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Draw border
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);

        super.paintComponent(g2);
        g2.dispose();
    }
}
