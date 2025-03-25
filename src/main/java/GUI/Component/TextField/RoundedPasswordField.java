package GUI.Component.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedPasswordField extends JPasswordField {
    private final int arcWidth;
    private final int arcHeight;
    private boolean hasFocus;
    private String placeholder = "";
    private final Color borderColor = new Color(200, 200, 200);
    private final Color focusBorderColor = new Color(0, 120, 215);
    private final Color placeholderColor = new Color(160, 160, 160);

    public RoundedPasswordField(int columns, int arcWidth, int arcHeight) {
        super(columns);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setSelectionColor(new Color(0, 120, 215, 100));
        setMinimumSize(new Dimension(200, 40));
        setPreferredSize(new Dimension(250, 40));
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                hasFocus = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                hasFocus = false;
                repaint();
            }
        });
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcWidth, arcHeight));

        // Draw placeholder text
        if (getPassword().length == 0 && !hasFocus && !placeholder.isEmpty()) {
            g2.setColor(placeholderColor);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, 12, y);
        }

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw border
        g2.setColor(hasFocus ? focusBorderColor : borderColor);
        g2.setStroke(new BasicStroke(hasFocus ? 2f : 1f));
        g2.draw(new RoundRectangle2D.Float(
                hasFocus ? 1 : 0.5f,
                hasFocus ? 1 : 0.5f,
                getWidth() - (hasFocus ? 2 : 1),
                getHeight() - (hasFocus ? 2 : 1),
                arcWidth,
                arcHeight
        ));

        g2.dispose();
    }


    // Thêm hiệu ứng đổ bóng (tuỳ chọn)
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (hasFocus) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
            g2.setColor(focusBorderColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            g2.dispose();
        }
    }
}