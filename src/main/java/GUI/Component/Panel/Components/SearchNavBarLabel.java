package GUI.Component.Panel.Components;

import GUI.Component.TextField.RoundedTextField;
import com.kitfox.svg.app.beans.SVGIcon;

import javax.swing.*;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class SearchNavBarLabel extends JPanel {
    public SearchNavBarLabel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JPanel panel = SearchNavBarLabelChild();
        panel.setBackground(Color.WHITE);
        this.add(panel, BorderLayout.CENTER);
    }

    public JPanel SearchNavBarLabelChild() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        String type[] = {"Tên", "Thể Loại", "Tác Giả", "Năm"};
        JComboBox searchtype = new JComboBox(type);
        searchtype.setSelectedIndex(0);
        searchtype.setEditable(false);
        searchtype.setPreferredSize(new Dimension(120, 40));
        searchtype.setBackground(Color.WHITE);
        searchtype.setForeground(new Color(50, 50, 50));
        searchtype.setFont(new Font("Verdana", Font.PLAIN, 18));
        searchtype.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 3),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        panel.add(searchtype);

        RoundedTextField searchfield = new RoundedTextField(12, 15, 15);
        searchfield.setPlaceholder("Từ khóa tìm kiếm....");
        searchfield.setBackground(new Color(245, 245, 245));
        searchfield.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchfield.setBorderColor(new Color(200, 200, 200));
        searchfield.setFocusBorderColor(new Color(0, 120, 215));
        panel.add(searchfield);

        URL refreshurl = getClass().getResource("/icons/refresh.svg");
        if (refreshurl == null) {
            throw new RuntimeException("Refresh Icon Not Found");
        }
        SVGIcon refreshIcon = new SVGIcon();
        refreshIcon.setSvgURI(URI.create(refreshurl.toString()));
        refreshIcon.setPreferredSize(new Dimension(20, 30));
        JButton refreshButton = new JButton("Làm mới", refreshIcon);
        refreshButton.setPreferredSize(new Dimension(120, 40));
        refreshButton.setBackground(new Color(211, 163, 131));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Verdana", Font.PLAIN, 16));
        refreshButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(refreshButton);
        return panel;
    }
}
