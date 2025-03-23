package GUI.Component;

import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class AccountNameLabel extends JPanel {
    public AccountNameLabel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setPreferredSize(new Dimension(242, 90)); // Adjusted size

        JSVGCanvas svgCanvas = createIcon();
        JPanel textPanel = createTextPanel();

        this.add(Box.createHorizontalStrut(25));
        this.add(svgCanvas);
        this.add(Box.createHorizontalStrut(30)); // Space between icon and text
        this.add(textPanel);
        this.add(Box.createHorizontalStrut(10)); // Add spacing on the right
    }

    private JSVGCanvas createIcon() {
        URL url = getClass().getResource("/icons/account.svg");
        if (url == null) {
            throw new RuntimeException("Icon not found");
        }
        JSVGCanvas svgCanvas = new JSVGCanvas();
        svgCanvas.setURI(url.toString());
        svgCanvas.setPreferredSize(new Dimension(50, 50)); // Set size for the icon
        return svgCanvas;
    }

    private JPanel createTextPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use vertical BoxLayout
        panel.setBackground(Color.WHITE);

        JLabel fname = new JLabel("Hoàng Quý");
        fname.setFont(new Font("Verdana", Font.PLAIN, 18));
        fname.setForeground(Color.BLACK);
        fname.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel job = new JLabel("Arsenal");
        job.setFont(new Font("Verdana", Font.PLAIN, 14));
        job.setForeground(Color.GRAY);
        job.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(fname);
        panel.add(Box.createVerticalStrut(5)); // Add space between name and job
        panel.add(job);

        return panel;
    }
}
