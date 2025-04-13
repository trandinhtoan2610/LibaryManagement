package GUI.Component.Button;

import com.kitfox.svg.app.beans.SVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;

public class ButtonIcon extends JButton {
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public ButtonIcon(String svgIconPath) {
        this(svgIconPath, 24, 24); // Kích thước mặc định: 24x24
    }

    public ButtonIcon(String svgIconPath, int iconWidth, int iconHeight) {
        // Tải icon SVG
        URL url = getClass().getResource(svgIconPath);
        if (url == null) {
            throw new RuntimeException("Icon not found: " + svgIconPath);
        }

        SVGIcon icon = new SVGIcon();
        icon.setSvgURI(URI.create(url.toString()));
        icon.setPreferredSize(new Dimension(iconWidth, iconHeight));

        // Cấu hình nút
        setIcon(icon);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
        setContentAreaFilled(false); // Ẩn nền mặc định
        setFocusPainted(false); // Ẩn viền focus
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Biểu tượng bàn tay khi hover

        // Màu sắc mặc định (có thể tuỳ chỉnh)
        hoverBackgroundColor = new Color(240, 240, 240);
        pressedBackgroundColor = new Color(200, 200, 200);

        // Hiệu ứng hover và click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
                setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(null);
                setContentAreaFilled(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackgroundColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }
        });
    }

    // Tuỳ chỉnh màu hover/pressed
    public void setHoverBackgroundColor(Color color) {
        this.hoverBackgroundColor = color;
    }

    public void setPressedBackgroundColor(Color color) {
        this.pressedBackgroundColor = color;
    }

    // Bo góc (tuỳ chọn)
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(pressedBackgroundColor);
        } else if (getModel().isRollover()) {
            g2.setColor(hoverBackgroundColor);
        } else {
            g2.setColor(getBackground());
        }

        // Vẽ nền bo góc
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Ẩn border gốc, vẽ border bo góc (tuỳ chọn)
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 200, 200));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        g2.dispose();
    }
}

