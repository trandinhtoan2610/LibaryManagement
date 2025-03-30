package GUI.Component.Panel.Components;

import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.net.URL;

public class AccountNameLabel extends JPanel {
    public AccountNameLabel(String FullName, String Role) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.WHITE);
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240,240,240),5),
            BorderFactory.createLineBorder(Color.black, 1)
        );
        this.setBorder(compoundBorder);
        this.setPreferredSize(new Dimension(252, 100)); // Adjusted size

        JSVGCanvas svgCanvas = createIcon();
        JPanel textPanel = createTextPanel(FullName, Role);

        this.add(Box.createHorizontalStrut(10));
        this.add(svgCanvas);
        this.add(Box.createHorizontalStrut(5)); // Space between icon and text
        this.add(textPanel);
        this.add(Box.createHorizontalStrut(5)); // Add spacing on the right
    }

    private JSVGCanvas createIcon() {
        URL url = getClass().getResource("/icons/account.svg");
        if (url == null) {
            throw new RuntimeException("Icon not found");
        }
        JSVGCanvas svgCanvas = new JSVGCanvas();


        svgCanvas.setPreferredSize(new Dimension(30, 30)); // Ví dụ: 30x30 pixels
        svgCanvas.setMaximumSize(new Dimension(30, 30));
        svgCanvas.setMinimumSize(new Dimension(30, 30));
        svgCanvas.setRecenterOnResize(false);

        svgCanvas.setURI(url.toString());


        // Đặt kích thước cố định

//        svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
//        svgCanvas.setRecenterOnResize(false);
//        svgCanvas.setRenderingTransform(null);

        return svgCanvas;
    }

    private JPanel createTextPanel(String FullName, String Role) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use vertical BoxLayout
        panel.setBackground(Color.WHITE);

        JLabel fname = new JLabel(FullName);
        fname.setFont(new Font("Verdana", Font.PLAIN, 17));
        fname.setForeground(Color.BLACK);
        fname.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel job = new JLabel(Role);
        job.setFont(new Font("Verdana", Font.PLAIN, 14));
        job.setForeground(Color.GRAY);
        job.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(fname);
        panel.add(Box.createVerticalStrut(5)); // Add space between name and job
        panel.add(job);

        return panel;
    }
}
