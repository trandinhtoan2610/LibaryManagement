package GUI.Component.Panel.Components;

import GUI.Component.TextField.RoundedTextField;
import com.kitfox.svg.app.beans.SVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URL;

public class SearchNavBarLabel extends JPanel {
    private JComboBox<String> searchType;
    private RoundedTextField searchField;
    private JButton refreshButton;
    private ActionListener searchListener; // Listener để thông báo khi tìm kiếm

    public SearchNavBarLabel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JPanel panel = searchNavBarLabelChild();
        panel.setBackground(Color.WHITE);
        this.add(panel, BorderLayout.CENTER);
        // Thiết lập kích thước cho SearchNavBarLabel
        this.setPreferredSize(new Dimension(400, 50));
    }

    public JPanel searchNavBarLabelChild() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        String[] type = {"Tên", "Thể Loại", "Tác Giả", "Năm"};
        searchType = new JComboBox<>(type);
        searchType.setSelectedIndex(0);
        searchType.setEditable(false);
        searchType.setPreferredSize(new Dimension(120, 40));
        searchType.setBackground(Color.WHITE);
        searchType.setForeground(new Color(50, 50, 50));
        searchType.setFont(new Font("Verdana", Font.PLAIN, 18));
        searchType.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 3),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        panel.add(searchType);

        searchField = new RoundedTextField(12, 15, 15);
        searchField.setPlaceholder("Từ khóa tìm kiếm....");
        searchField.setBackground(new Color(245, 245, 245));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorderColor(new Color(200, 200, 200));
        searchField.setFocusBorderColor(new Color(0, 120, 215));
        // Thiết lập kích thước cho RoundedTextField
        searchField.setPreferredSize(new Dimension(200, 40));
        // Thêm sự kiện khi nhấn Enter để tìm kiếm
        searchField.addActionListener(e -> performSearch());
        panel.add(searchField);

        URL refreshUrl = getClass().getResource("/icons/filter.svg");
        if (refreshUrl == null) {
            throw new RuntimeException("Refresh Icon Not Found");
        }
        SVGIcon refreshIcon = new SVGIcon();
        refreshIcon.setSvgURI(URI.create(refreshUrl.toString()));
        refreshIcon.setPreferredSize(new Dimension(20, 30));
        refreshButton = new JButton(refreshIcon);
        refreshButton.setPreferredSize(new Dimension(60, 40));
        refreshButton.setBackground(new Color(211, 163, 131));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Verdana", Font.PLAIN, 16));
        refreshButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Thêm sự kiện làm mới
        refreshButton.addActionListener(e -> {
            searchField.setText(""); // Xóa từ khóa tìm kiếm
            performSearch(); // Gọi tìm kiếm để làm mới bảng
        });
        panel.add(refreshButton);

        return panel;
    }

    // Phương thức để thiết lập listener từ BookPanel
    public void setSearchListener(ActionListener listener) {
        this.searchListener = listener;
    }

    // Phương thức để lấy tiêu chí tìm kiếm
    public String getSearchType() {
        return (String) searchType.getSelectedItem();
    }

    // Phương thức để lấy từ khóa tìm kiếm
    public String getSearchKeyword() {
        return searchField.getText();
    }

    // Phương thức thực hiện tìm kiếm
    private void performSearch() {
        if (searchListener != null) {
            searchListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "search"));
        }
    }
}