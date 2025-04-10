package GUI.Component.TextField;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomTextField extends JTextField {
    private Color borderColor = new Color(200, 200, 200); // Màu viền mặc định (xám nhạt)
    private Color focusColor = new Color(0, 120, 215);    // Màu viền khi focus (xanh dương)
    private int borderRadius = 2;                          // Vuông góc (đặt > 0 để bo tròn)

    public CustomTextField() {
        setCustomStyle();
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        new RoundBorder(focusColor, 1, borderRadius),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        new RoundBorder(borderColor, 1, borderRadius),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });
    }

    private void setCustomStyle() {
        setPreferredSize(new Dimension(250, 40));
        setFont(new Font("Segoe UI", Font.PLAIN, 16));
        setForeground(new Color(50, 50, 50)); // Màu chữ xám đậm

        // Viền và padding
        setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(borderColor, 1, borderRadius),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Background
        setBackground(Color.WHITE);
        setOpaque(true);
    }
    private static class RoundBorder implements Border {
        private Color color;
        private int thickness;
        private int radius;

        public RoundBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}