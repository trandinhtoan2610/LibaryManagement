package GUI.Component.Panel.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CategorySearchBar extends JPanel {
    private JTextField searchField;
    private JButton searchButton;

    public CategorySearchBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setBackground(Color.WHITE);

        // Ô nhập từ khóa tìm kiếm
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Nút tìm kiếm
        searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchButton.setBackground(new Color(0, 120, 215));
        searchButton.setForeground(Color.WHITE);
        searchButton.setPreferredSize(new Dimension(100, 30));

        add(searchField);
        add(searchButton);
    }

    // Lấy từ khóa tìm kiếm
    public String getSearchKeyword() {
        return searchField.getText().trim();
    }

    // Đặt ActionListener cho nút tìm kiếm
    public void setSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    // Xóa nội dung ô tìm kiếm
    public void clearSearch() {
        searchField.setText("");
    }
}