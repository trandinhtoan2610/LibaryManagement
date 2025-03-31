package GUI.Component.Dialog;

import GUI.Component.Button.ButtonAction;
import javax.swing.*;
import java.awt.*;

public class LogOutDialog extends JDialog {
    private boolean confirmed = false;

    public LogOutDialog(JFrame parent) {
        super(parent, "Đăng Xuất", true);
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 10));
        setSize(400, 200);
        setResizable(false);

        // Panel tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 120, 215));
        JLabel titleLabel = new JLabel("ĐĂNG XUẤT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel nội dung
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel messageLabel = new JLabel("Bạn có chắc chắn muốn đăng xuất không?");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(messageLabel, BorderLayout.CENTER);

        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Nút Đồng ý (màu xanh)
        ButtonAction yesButton = new ButtonAction("Đồng ý",
                new Color(0, 120, 215),  // normal
                new Color(0, 100, 190),   // hover
                new Color(0, 80, 170),    // press
                5);
        yesButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        // Nút Hủy bỏ (màu đỏ)
        ButtonAction noButton = new ButtonAction("Hủy bỏ",
                new Color(220, 80, 80),   // normal
                new Color(200, 60, 60),   // hover
                new Color(180, 40, 40),   // press
                5);
        noButton.addActionListener(e -> dispose());

        buttonPanel.add(noButton);  // Để nút Hủy bỏ trước
        buttonPanel.add(yesButton); // Nút Đồng ý sau

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        DeleteEmployeeDialog dialog = new DeleteEmployeeDialog(frame);
//        dialog.setVisible(true);
//
//        if (dialog.isConfirmed()) {
//            System.out.println("Người dùng đã đồng ý xóa");
//        } else {
//            System.out.println("Người dùng đã hủy bỏ");
//        }
//    }
}