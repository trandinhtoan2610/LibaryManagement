package GUI.Component.Dialog;

import BUS.BorrowDetailBUS;
import GUI.Component.Button.ButtonBack;
import GUI.Component.Button.ButtonChosen;
import GUI.Component.Table.BorrowDetailTable;
import GUI.Component.TextField.CustomTextField;

import javax.swing.*;
import java.awt.*;

public class AddBorrowDetailDialog extends JDialog {
    private BorrowDetailBUS borrowDetailBUS;
    private BorrowDetailTable borrowDetailTable;

    private CustomTextField bookIDField;
    private CustomTextField quantityField;

    private JRadioButton borrowedRadioButton;
    private JRadioButton returnedRadioButton;

    private ButtonGroup statusGroup;

    private ButtonChosen buttonChosenBook;

    public AddBorrowDetailDialog(JFrame parent) {
        super(parent, "Thêm Chi Tiết Phiếu Mượn", true);
        this.borrowDetailTable = borrowDetailTable;
        this.borrowDetailBUS = new BorrowDetailBUS();
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }
    private void initComponents() {
        // Main dialog setup
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 500); // Adjusted size
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

        JLabel titleLabel = new JLabel("Thêm Sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }
    private CustomTextField createTallTextField() {
        CustomTextField field = new CustomTextField();
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 35)); // Taller height
        return field;
    }
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: Book ID Field and ButtonChosen
        JLabel bookIDLabel = createLabel("Mã sách:");
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(bookIDLabel, gbc);
        bookIDField = createTallTextField();
        gbc.gridx = 1;
        panel.add(bookIDField, gbc);

        buttonChosenBook = new ButtonChosen();
        gbc.gridx = 2;
        panel.add(buttonChosenBook, gbc);

        // Row 2: Book Information Display (Placeholder for now)
        JLabel bookInfoLabel = createLabel("Thông tin sách:");
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(bookInfoLabel, gbc);

        JLabel bookInfoValue = new JLabel("Tên sách sẽ hiển thị ở đây");
        bookInfoValue.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(bookInfoValue, gbc);

        // Row 3: Quantity Field
        JLabel quantityLabel = createLabel("Số lượng:");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(quantityLabel, gbc);

        quantityField = createTallTextField();
        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        // Row 4: Borrow Status (Radio Buttons)
        JLabel statusLabel = createLabel("Trạng thái:");
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(statusLabel, gbc);

        borrowedRadioButton = new JRadioButton("Đã mượn");
        returnedRadioButton = new JRadioButton("Đã trả");
        borrowedRadioButton.setSelected(true);
        statusGroup = new ButtonGroup();
        statusGroup.add(borrowedRadioButton);
        statusGroup.add(returnedRadioButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(borrowedRadioButton);
        statusPanel.add(returnedRadioButton);

        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(statusPanel, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton cancelButton = createButton("Hủy bỏ", new Color(218, 42, 0), e -> dispose());
        JButton addButton = createButton("Thêm nhà xuất bản", new Color(0, 120, 215), e -> addBorrowDetails());

        panel.add(cancelButton);
        panel.add(addButton);

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
        label.setPreferredSize(new Dimension(150, 35)); // Match text field height
        return label;
    }
    private void addBorrowDetails() {
        String bookID = bookIDField.getText();
        String quantity = quantityField.getText();
        String status = borrowedRadioButton.isSelected() ? "Đã mượn" : "Đã trả";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = new JFrame();
            AddBorrowDetailDialog dialog = new AddBorrowDetailDialog(parentFrame);
            dialog.setVisible(true);
        });
    }
}
