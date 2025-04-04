package GUI.Component.Dialog;

import BUS.PublisherBUS;
import DTO.PublisherDTO;
import GUI.Component.Button.ButtonBack;
import GUI.Component.Panel.PublisherPanel;
import GUI.Component.TextField.CustomTextField;
import GUI.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class UpdatePublisherDialog extends JDialog {
    private Long currentID;
    private CustomTextField nameField;
    private CustomTextField phoneField;
    private CustomTextField addressField;
    private PublisherDTO publisherDTOtoUpdate;
    private PublisherPanel publisherPanel;
    private PublisherBUS publisherBUS;

    public UpdatePublisherDialog(JFrame parent, PublisherPanel publisherPanel, PublisherDTO publisherDTO) {
        super(parent, "Cập nhật nhà xuất bản", true);
        this.publisherPanel = publisherPanel;
        this.publisherDTOtoUpdate = publisherDTO;
        this.publisherBUS = new PublisherBUS();
        initComponents();
        if (publisherDTOtoUpdate != null) {
            fillPublisherData();
        }
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        // Main dialog setup
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600); // Consistent size with Add dialog
        setResizable(false);

        // Add components
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Add back button to title panel
        ButtonBack backButton = new ButtonBack();
        backButton.addActionListener(e -> dispose());
        panel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Cập nhật Nhà Xuất Bản");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Form panel with label-field pairs
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 15, 15));

        // Initialize fields with larger height
        nameField = createTallTextField();
        phoneField = createTallTextField();
        addressField = createTallTextField();

        // Add label-field pairs
        formPanel.add(createLabel("Tên Nhà Xuất Bản *"));
        formPanel.add(nameField);

        formPanel.add(createLabel("Số Điện Thoại *"));
        formPanel.add(phoneField);

        formPanel.add(createLabel("Địa Chỉ *"));
        formPanel.add(addressField);

        // Add form to content panel
        contentPanel.add(formPanel);

        return contentPanel;
    }

    private CustomTextField createTallTextField() {
        CustomTextField field = new CustomTextField();
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 35));
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton cancelButton = createButton("Hủy bỏ", new Color(218, 42, 0), e -> dispose());
        JButton updateButton = createButton("Cập nhật nhà xuất bản", new Color(0, 120, 215), e -> updatePublisher());

        panel.add(cancelButton);
        panel.add(updateButton);

        return panel;
    }

    private JButton createButton(String text, Color bgColor, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(150, 35));
        return label;
    }

    private void fillPublisherData() {
        if (publisherDTOtoUpdate != null) {
            nameField.setText(publisherDTOtoUpdate.getName());
            phoneField.setText(publisherDTOtoUpdate.getPhone());
            addressField.setText(publisherDTOtoUpdate.getAddress());
        }
    }

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tên nhà xuất bản!", nameField);
            return false;
        }

        if (!Controller.checkValidName(nameField.getText())) {
            showError("Tên nhà xuất bản không hợp lệ!", nameField);
            return false;
        }

        if (phoneField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập số điện thoại!", phoneField);
            return false;
        }

        if (!Controller.checkValidPhone(phoneField.getText())) {
            showError("Số điện thoại không hợp lệ!", phoneField);
            return false;
        }

        if (addressField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập địa chỉ!", addressField);
            return false;
        }

        return true;
    }

    private void showError(String message, JComponent focusComponent) {
        AlertDialog alert = new AlertDialog(this, message);
        alert.setVisible(true);
        focusComponent.requestFocus();
    }

    private void setCurrentID() {
        try {
            if (publisherDTOtoUpdate != null) {
                currentID = publisherDTOtoUpdate.getId();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy nhà xuất bản cần cập nhật",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy ID nhà xuất bản: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePublisher() {
        String oldName = nameField.getText();
        String oldPhone = phoneField.getText();
        String oldAddress = addressField.getText();

        setCurrentID();

        if (!validateFields()) {
            return;
        }

        PublisherDTO updatePublisherDTO = new PublisherDTO(
                currentID,
                nameField.getText().trim(),
                phoneField.getText().trim(),
                addressField.getText().trim()
        );

        try {
            boolean success = publisherBUS.updatePublisher(updatePublisherDTO);
            if (success) {
                publisherPanel.updatePublisher(updatePublisherDTO);
                AlertDialog successDialog = new AlertDialog(this, "Cập nhật nhà xuất bản thành công!");
                successDialog.setVisible(true);
                dispose();
            } else {
                // Restore old values if update failed
                nameField.setText(oldName);
                phoneField.setText(oldPhone);
                addressField.setText(oldAddress);
                JOptionPane.showMessageDialog(this,
                        "Cập nhật nhà xuất bản thất bại",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi cập nhật nhà xuất bản: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}