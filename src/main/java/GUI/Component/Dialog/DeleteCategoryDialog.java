package GUI.Component.Dialog;

import BUS.CategoryBUS;
import GUI.Component.Button.ButtonBack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeleteCategoryDialog extends JDialog {
    private final CategoryBUS categoryBUS;
    private final Long categoryId;

    public DeleteCategoryDialog(JFrame parent, Long categoryId) {
        super(parent, "Xóa Danh Mục", true);
        this.categoryBUS = new CategoryBUS();
        this.categoryId = categoryId;
        initComponent();
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
        setSize(400, 200);
        setResizable(false);
    }

    private JPanel setTitle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel txt = new JLabel("Xóa Danh Mục");
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
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

        JLabel confirmLabel = new JLabel("Bạn có chắc muốn xóa danh mục này?");
        confirmLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(confirmLabel);

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

        JButton deleteButton = new JButton("Xóa danh mục");
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deleteButton.setPreferredSize(new Dimension(140, 35));
        deleteButton.setBackground(new Color(0, 120, 215));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteCategory());

        bottomPanel.add(cancelButton);
        bottomPanel.add(deleteButton);

        return bottomPanel;
    }

    private void deleteCategory() {
        try {
            categoryBUS.deleteCategory(categoryId);
            AlertDialog successDialog = new AlertDialog(this, "Xóa danh mục thành công");
            successDialog.setVisible(true);
            dispose();
        } catch (Exception e) {
            AlertDialog errorDialog = new AlertDialog(this, "Lỗi khi xóa danh mục: " + e.getMessage());
            errorDialog.setVisible(true);
        }
    }
}