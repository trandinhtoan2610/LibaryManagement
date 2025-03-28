package GUI.Component.Combobox;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomComboBox extends JComboBox<String> {
    private Color borderColor = new Color(200, 200, 200); // Màu viền mặc định
    private Color focusColor = new Color(0, 120, 215);    // Màu viền khi focus
    private int borderRadius = 0;                         // Vuông góc

    public CustomComboBox() {
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
        setPreferredSize(new Dimension(100, 50));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(50, 50, 50));

        // Background và viền
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(borderColor, 1, borderRadius),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Tùy chỉnh dropdown arrow
        UIManager.put("ComboBox.buttonBackground", Color.WHITE);
        UIManager.put("ComboBox.buttonShadow", borderColor);
        UIManager.put("ComboBox.buttonDarkShadow", borderColor);
        UIManager.put("ComboBox.buttonHighlight", Color.WHITE);
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼"); // Unicode arrow
                button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                button.setContentAreaFilled(false); // Ẩn nền
                button.setBorder(BorderFactory.createEmptyBorder()); // Ẩn viền
                button.setFocusPainted(false); // Ẩn hiệu ứng focus
                return button;
            }
        });
    }

    // Custom Border (giống TextField)
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
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Custom ComboBox Demo");
//        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
//        frame.add(Box.createVerticalGlue());
//        CustomComboBox comboBox = new CustomComboBox();
//        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Option 1", "Option 2", "Option 3"}));
//        comboBox.setPreferredSize(new Dimension(200, 30));
//        frame.add(comboBox);
//
//        frame.setSize(300, 150);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//    }
}