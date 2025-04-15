package GUI.Component.Button;

import com.kitfox.svg.app.beans.SVGIcon;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;

public class ButtonAdd extends JLabel {
    // Màu sắc chuyên nghiệp
    private Color TEXT_DEFAULT = new Color(51, 51, 51);    // #333333
    private Color TEXT_HOVER = new Color(26, 115, 232);   // #1A73E8
    private Color BG_DEFAULT = new Color(255, 255, 255);  // #FFFFFF
    private Color BG_HOVER = new Color(245, 245, 245);    // #F5F5F5
    private Color BORDER_COLOR = new Color(224, 224, 224); // #E0E0E0

    // Kích thước và padding
    private int ICON_SIZE = 30;
    private int HORIZONTAL_PADDING = 10;
    private int VERTICAL_PADDING = 6;
    private int BORDER_RADIUS = 8;

    public ButtonAdd() {
        try {
            setPreferredSize(new Dimension(110,100));
            URL url = getClass().getResource("/icons/add.svg");
            if (url == null) throw new RuntimeException("Icon not found");
            SVGIcon icon = new SVGIcon();
            icon.setSvgURI(URI.create(url.toString()));
            icon.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));

            setIcon(icon);
            setText("Thêm");
            setFont(new Font("Verdana", Font.PLAIN, 16));
            setOpaque(true);
            setBackground(BG_DEFAULT);
            setForeground(TEXT_DEFAULT);

            // Căn giữa nội dung
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setIconTextGap(8);

            // Border với bo góc
            setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(BORDER_COLOR, 1, BORDER_RADIUS),
                    BorderFactory.createEmptyBorder(VERTICAL_PADDING, HORIZONTAL_PADDING,
                            VERTICAL_PADDING, HORIZONTAL_PADDING)
            ));

            // Hiệu ứng hover
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(BG_HOVER);
                    setForeground(TEXT_HOVER);
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(BG_DEFAULT);
                    setForeground(TEXT_DEFAULT);
                    setCursor(Cursor.getDefaultCursor());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            setText("Error loading icon");
        }
    }

    // Lớp hỗ trợ border bo góc
    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y,
                                int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }
    }

//    public static void main(String[] args) {
//
//        Frame frame = new Frame("ButtonAdd");
//        frame.setSize(800, 600);
//        frame.setLayout(new FlowLayout());
//        frame.add(new ButtonAdd());
//        frame.add(new ButtonUpdate());
//        frame.add(new ButtonDelete());
//        frame.add(new ButtonDetails());
//        frame.add(new ButtonExportExcel());
//        frame.add(new ButtonImportExcel());
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//
//    }
}