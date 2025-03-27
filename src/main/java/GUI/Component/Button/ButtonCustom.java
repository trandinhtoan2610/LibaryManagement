import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonCustom {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Rounded Button Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        // Tạo button bo góc
        JButton roundedButton = new JButton("Click Me") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ nền bo góc
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bo góc 20px

                // Vẽ chữ
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Không vẽ viền mặc định
            }
        };

        // Thiết lập style
        roundedButton.setBackground(new Color(52, 152, 219)); // Màu xanh dương
        roundedButton.setForeground(Color.WHITE); // Chữ trắng
        roundedButton.setFont(new Font("Arial", Font.BOLD, 14));
        roundedButton.setFocusPainted(false);
        roundedButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        roundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Biểu tượng bàn tay khi hover

        // Hiệu ứng hover
        roundedButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                roundedButton.setBackground(new Color(41, 128, 185)); // Màu đậm hơn khi hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                roundedButton.setBackground(new Color(52, 152, 219)); // Trở lại màu ban đầu
            }
        });

        // Sự kiện click
        roundedButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Button Clicked!");
        });

        frame.add(roundedButton);
        frame.setVisible(true);
    }
}