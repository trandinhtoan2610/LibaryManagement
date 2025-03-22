package GUI.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

import com.kitfox.svg.app.beans.SVGIcon;
public class IconNavBarLabel extends JPanel {

    private String[][] st = {
            {"Thêm", "/icons/add.svg"},
            {"Cập Nhật", "/icons/edit.svg"},
            {"Xóa", "/icons/delete.svg"},
            {"Chi Tiết", "/icons/detail.svg"}
    };

    public IconNavBarLabel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        for (String[] item : st) {
            JLabel label = createLabel(item[0], item[1]);
            add(label);
        }
    }

    private JLabel createLabel(String text, String iconPath) {
        JLabel label = new JLabel(text);
        URL url = getClass().getResource(iconPath);
        if(url == null){
            throw new RuntimeException(iconPath + " not found");
        }
        SVGIcon icon = new SVGIcon();
        icon.setSvgURI(URI.create(url.toString()));
        icon.setPreferredSize(new Dimension(32, 32));
        label.setIcon(icon);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setIconTextGap(5);
        label.setFont(new Font("Verdana", Font.PLAIN, 24));
        return label;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        IconNavBarLabel navBar = new IconNavBarLabel();
        frame.add(navBar);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}