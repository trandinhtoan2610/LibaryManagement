package GUI.Component.Dialog;

import BUS.CategoryBUS;
import DTO.Category;
import GUI.Component.Button.ButtonBack;
import GUI.Component.TextField.CustomTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditCategoryDialog extends JDialog {
    private final CategoryBUS categoryBUS;
    private final Category category;
    private CustomTextField nameField;

    public EditCategoryDialog(JFrame parent, Category category) {
        super(parent, "Chỉnh Sửa Danh Mục", true);
        this.categoryBUS = new CategoryBUS();
        this.category = category;
        initComponent();
        loadCategoryData();
        pack();
        setLocationRelativeTo(parent);
    }

    public void initComponent() {
        getContentPane().setLayout(new BorderLayout(5, 10));
        JPanel titlePanel = setTitle();
        getContentPane().add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = setContentPanel();
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = setButtonPanel();
        getContentPane().add(buttonPanel, BorderLayout.WEST);

        JPanel bottomPanel = setBottomPanel();
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setResizable(false);
    }

    private JPanel setTitle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel txt = new JLabel("Chỉnh Sửa Danh Mục");
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        txt.setHorizontalAlignment(SwingConstants.CENTER);
        txt.setVerticalAlignment(SwingConstants.CENTER);
        txt.setForeground(Color.WHITE);
        panel.add(txt, BorderLayout.CENTER);
        panel.setForeground(Color.WHITE);
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 7, 2, 7),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2)
        ));
        return panel;
    }

    private JPanel setButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ButtonBack buttonBack = new ButtonBack();
        buttonBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        panel.add(buttonBack);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel setContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, -2, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

        JLabel nameLabel = setJLabel("Tên Danh Mục");
        nameField = new CustomTextField();

        panel.add(nameLabel);
        panel.add(nameField);

        return panel;
    }

    private JPanel setBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setBackground(new Color(218, 42, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());

        JButton saveButton = new JButton("Lưu thay đổi");
        saveButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        saveButton.setPreferredSize(new Dimension(140, 35));
        saveButton.setBackground(new Color(0, 120, 215));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> updateCategory());

        bottomPanel.add(cancelButton);
        bottomPanel.add(saveButton);

        return bottomPanel;
    }

    private JLabel setJLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 14));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.WHITE);
        return label;
    }

    private void loadCategoryData() {
        nameField.setText(category.getName());
    }

    private void updateCategory() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Tên danh mục không được để trống");
            }

            category.setName(name);
            categoryBUS.updateCategory(category);
            AlertDialog successDialog = new AlertDialog(this, "Chỉnh sửa danh mục thành công");
            successDialog.setVisible(true);
            dispose();
        } catch (IllegalArgumentException e) {
            AlertDialog errorDialog = new AlertDialog(this, e.getMessage());
            errorDialog.setVisible(true);
        } catch (Exception e) {
            AlertDialog errorDialog = new AlertDialog(this, "Lỗi khi chỉnh sửa danh mục: " + e.getMessage());
            errorDialog.setVisible(true);
        }
    }
}