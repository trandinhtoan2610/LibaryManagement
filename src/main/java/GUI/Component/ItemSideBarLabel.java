package GUI.Component;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URL;

public class ItemSideBarLabel extends JPanel {
    private String st[][] = {
            {"Trang chủ", "/icons/homepage.svg"},
            {"Sách", "/icons/book.svg"},
            {"Độc giả", "/icons/reader.svg"},
            {"Tác giả", "/icons/author.svg"},
            {"Nhà xuất bản", "/icons/publisher.svg"},
            {"Nhân viên", "/icons/employee.svg"},
            {"Phiếu mượn", "/icons/borrow.svg"},
            {"Phiếu nhập", "/icons/enter.svg"},
            {"Nhà cung cấp", "/icons/supplier.svg"},
            {"Thống kê", "/icons/statistics.svg"},
            {"Đăng xuất", "/icons/signout.svg"}
    };

    public ItemSideBarLabel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setBackground(new Color(240, 240, 240));
        this.setPreferredSize(new Dimension(250, 680));
        for (String item[] : st) {
            JPanel panel = createItem(item[0], item[1]);
            panel.setBorder(new LineBorder(Color.BLACK));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
            add(panel);
            add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    private JPanel createItem(String text, String iconPath) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        panel.setBorder(new LineBorder(Color.BLACK));
        panel.setBackground(Color.WHITE);
        URL url = getClass().getResource(iconPath);
        if (url == null) {
            throw new RuntimeException("Icon not found: " + iconPath);
        }

        JSVGCanvas svgCanvas = new JSVGCanvas();
        svgCanvas.setURI(url.toString());
        svgCanvas.setPreferredSize(new Dimension(30, 30));

        panel.add(svgCanvas);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 20));
        panel.add(label);

        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return panel;
    }
}