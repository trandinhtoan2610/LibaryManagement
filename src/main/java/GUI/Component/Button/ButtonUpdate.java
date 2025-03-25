package GUI.Component.Button;

import com.kitfox.svg.app.beans.SVGIcon;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class ButtonUpdate extends JLabel {
    public ButtonUpdate() {
        URL url = getClass().getResource("/icons/edit.svg");
        if (url == null) {
            throw new RuntimeException("edit.svg not found");
        }
        SVGIcon icon = new SVGIcon();
        icon.setSvgURI(URI.create(url.toString()));
        icon.setPreferredSize(new Dimension(32, 32));
        this.setIcon(icon);
        this.setText("Thêm");
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setIconTextGap(5);
        this.setFont(new Font("Verdana", Font.PLAIN, 24));
    }
}
