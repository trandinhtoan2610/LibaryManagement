package GUI.Component.Dialog;

import GUI.Component.Button.ButtonAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlertDialog extends JDialog {
    public AlertDialog(JDialog parent, String title) {
        super(parent, "Thông báo", true);
        initComponents(title);
        pack();
        setLocationRelativeTo(parent);
    }
    
     public AlertDialog(Window parent, String title) {
        super(parent, "Thông báo", ModalityType.APPLICATION_MODAL);
        initComponents(title);
        pack();
        setLocationRelativeTo(parent);
    }

    
    public AlertDialog(JFrame parent, String title) {
        super(parent, "Thông báo", true);
        initComponents(title);
        pack();
        setLocationRelativeTo(parent);
    }
    private void initComponents(String title) {
        setLayout(new BorderLayout(20, 10));
        setSize(400, 200);
        setResizable(false);
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 120, 215));
        JLabel titleLabel = new JLabel("THÔNG BÁO !!!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel nội dung
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel messageLabel = new JLabel(title);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(messageLabel, BorderLayout.CENTER);

        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        // Nút Hủy bỏ (màu đỏ)
        ButtonAction noButton = new ButtonAction("OK!!",
                new Color(220, 80, 80),   // normal
                new Color(200, 60, 60),   // hover
                new Color(180, 40, 40),   // press
                5);
        noButton.addActionListener(e -> dispose());
        buttonPanel.add(noButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}
