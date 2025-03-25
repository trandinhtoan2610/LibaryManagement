package GUI.Component.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedTextField extends JTextField {
    private final int arcWidth;
    private final int arcHeight;
    private boolean hasFocus;
    private String placeholder = "";
    private Color borderColor = new Color(200, 200, 200);
    private Color focusBorderColor = new Color(0, 120, 215);
    private Color placeholderColor = new Color(160, 160, 160);
    private float focusBorderThickness = 1.5f;

    public RoundedTextField(int columns, int arcWidth, int arcHeight) {
        super(columns);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setupField();
    }

    private void setupField() {
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcWidth, arcHeight));

        // Draw placeholder
        if (getText().isEmpty() && !hasFocus && !placeholder.isEmpty()) {
            drawPlaceholder(g2);
        }

        super.paintComponent(g2);
        g2.dispose();
    }

    private void drawPlaceholder(Graphics2D g2) {
        g2.setColor(placeholderColor);
        g2.setFont(getFont().deriveFont(Font.ITALIC));
        FontMetrics fm = g2.getFontMetrics();
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(placeholder, getInsets().left, y);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw border
        g2.setColor(hasFocus ? focusBorderColor : borderColor);
        float borderThickness = 1f;
        g2.setStroke(new BasicStroke(hasFocus ? focusBorderThickness : borderThickness));
        g2.draw(new RoundRectangle2D.Float(
                hasFocus ? focusBorderThickness / 2 : borderThickness / 2,
                hasFocus ? focusBorderThickness / 2 : borderThickness / 2,
                getWidth() - (hasFocus ? focusBorderThickness : borderThickness),
                getHeight() - (hasFocus ? focusBorderThickness : borderThickness),
                arcWidth,
                arcHeight
        ));
        g2.dispose();
    }

    // Custom setters
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setFocusBorderColor(Color color) {
        this.focusBorderColor = color;
        repaint();
    }

    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }


    // Optional shadow effect
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