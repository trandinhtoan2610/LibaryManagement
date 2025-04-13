package GUI.Component.Dialog;

import BUS.BookBUS;
import BUS.BorrowDetailBUS;
import DTO.BookViewModel;
import DTO.BorrowDetailDTO;
import DTO.Enum.SubStatus;
import GUI.Component.Button.ButtonBack;
import GUI.Component.Button.ButtonChosen;
import GUI.Component.TextField.CustomTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class UpdateBorrowDetailDialog extends JDialog {
    private final BorrowDetailBUS borrowDetailBUS = new BorrowDetailBUS();
    private final BookBUS bookBUS = new BookBUS();
    private BookViewModel currentBook;
    private BorrowDetailDTO currentBorrowDetail;

    private final CustomTextField bookIDField = new CustomTextField();
    private final CustomTextField quantityField = new CustomTextField();
    private final ButtonChosen buttonChosenBook = new ButtonChosen();
    private final JRadioButton borrowedRadioButton = new JRadioButton("Đã mượn", true);
    private final JRadioButton returnedRadioButton = new JRadioButton("Đã trả");

    private final JLabel titleLabel = new JLabel("Tên sách:             ");
    private final JLabel authorLabel = new JLabel("Tác giả:             ");
    private final JLabel categoryLabel = new JLabel("Thể loại:             ");
    private final JLabel publisherLabel = new JLabel("NXB:             ");
    private final JLabel yearLabel = new JLabel("Năm:             ");
    private final JLabel quantityLabel = new JLabel("Còn:             ");

    private Long borrowSheetId;

    private JDialog parentDialog;

    private BorrowDetailDTO borrowDetailToEdit;

    public UpdateBorrowDetailDialog(JDialog parentDialog, long borrowSheetId, BorrowDetailDTO borrowDetailToEdit) {
        super(parentDialog, "Cập Nhật Chi Tiết Mượn", true);
        this.borrowSheetId = borrowSheetId;
        this.borrowDetailToEdit = borrowDetailToEdit;
        initComponents();
        setSize(600, 450);
        setLocationRelativeTo(parentDialog);
        if (parentDialog != null) {
            Point location = this.getLocation();
            this.setLocation(location.x + 50, location.y + 90);
        }
        setFields();
    }
    private void setFields() {
        bookIDField.setText(borrowDetailToEdit.getBookId().toString());
        quantityField.setText(String.valueOf(borrowDetailToEdit.getQuantity()));
        if (borrowDetailToEdit.getStatus() == SubStatus.Đang_Mượn) {
            borrowedRadioButton.setSelected(true);
        } else {
            returnedRadioButton.setSelected(true);
        }
    }
    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        ButtonBack backButton = new ButtonBack();
        backButton.addActionListener(e -> dispose());
        panel.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel("Cập Nhật Sách");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Book ID row
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mã sách:"), gbc);

        bookIDField.setPreferredSize(new Dimension(120, 30));
        bookIDField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateBookInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateBookInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateBookInfo();
            }
        });
        gbc.gridx = 1;
        panel.add(bookIDField, gbc);
        buttonChosenBook.addActionListener(e -> chooseBook());
        gbc.gridx = 2;
        panel.add(buttonChosenBook, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(createSeparator(), gbc);

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 25, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 16);
        titleLabel.setFont(infoFont);
        authorLabel.setFont(infoFont);
        categoryLabel.setFont(infoFont);
        publisherLabel.setFont(infoFont);
        yearLabel.setFont(infoFont);
        quantityLabel.setFont(infoFont);

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(publisherLabel);
        infoPanel.add(yearLabel);
        infoPanel.add(quantityLabel);

        gbc.gridy = 2;
        panel.add(infoPanel, gbc);

        // Quantity row
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Số lượng mượn:"), gbc);

        quantityField.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        // Status row
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Trạng thái:"), gbc);

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(borrowedRadioButton);
        statusGroup.add(returnedRadioButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusPanel.add(borrowedRadioButton);
        statusPanel.add(returnedRadioButton);

        gbc.gridx = 1;
        panel.add(statusPanel, gbc);

        return panel;
    }

    private void updateBookInfo() {
        String bookIDText = bookIDField.getText();
        if (bookIDText.isEmpty()) {
            currentBook = null;
            titleLabel.setText("Tên sách: ");
            authorLabel.setText("Tác giả: ");
            categoryLabel.setText("Thể loại: ");
            publisherLabel.setText("NXB: ");
            yearLabel.setText("Năm: ");
            quantityLabel.setText("Còn: ");
            return;
        }

        try {
            long bookID = Long.parseLong(bookIDText);
            currentBook = bookBUS.getBookByIdForDisplay(bookID);
            if (currentBook != null) {
                titleLabel.setText("Tên sách: " + currentBook.getName());
                authorLabel.setText("Tác giả: " + currentBook.getAuthorName());
                categoryLabel.setText("Thể loại: " + currentBook.getCategoryName());
                publisherLabel.setText("NXB: " + currentBook.getPublisherName());
                yearLabel.setText("Năm: " + currentBook.getYearOfPublication());
                quantityLabel.setText("Còn: " + currentBook.getQuantity());
            } else {
                titleLabel.setText("Tên sách: ");
                authorLabel.setText("Tác giả: ");
                categoryLabel.setText("Thể loại: ");
                publisherLabel.setText("NXB: ");
                yearLabel.setText("Năm: ");
                quantityLabel.setText("Còn: ");
            }
        } catch (NumberFormatException e) {
            // Handle invalid input
        }
    }

    private void chooseBook() {
        ChooseBookDialog dialog = new ChooseBookDialog(this);
        dialog.setVisible(true);
        currentBook = dialog.getSelectedBook();
        if (currentBook != null) {
            bookIDField.setText(currentBook.getId().toString());
            titleLabel.setText("Tên sách: " + currentBook.getName());
            authorLabel.setText("Tác giả: " + currentBook.getAuthorName());
            categoryLabel.setText("Thể loại: " + currentBook.getCategoryName());
            publisherLabel.setText("NXB: " + currentBook.getPublisherName());
            yearLabel.setText("Năm: " + currentBook.getYearOfPublication());
            quantityLabel.setText("Còn: " + currentBook.getQuantity());
        }
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        return separator;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setPreferredSize(new Dimension(120, 30));
        cancelButton.addActionListener(e -> dispose());

        JButton addButton = new JButton("Cập Nhật");
        addButton.setPreferredSize(new Dimension(120, 30));
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> updateBorrowDetails());

        panel.add(cancelButton);
        panel.add(addButton);

        return panel;
    }

    private boolean isValidInput() {
        String bookIDText = bookIDField.getText();
        if (currentBook == null || bookIDText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sách hoặc chọn sách!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String quantityText = quantityField.getText();
        if (quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (currentBook != null && quantity > currentBook.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Số lượng mượn không được lớn hơn số lượng còn lại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    public BorrowDetailDTO getCurrentBorrowDetail() {
        return currentBorrowDetail;
    }
    private void updateBorrowDetails() {
        if (isValidInput()) {
            long bookID = Long.parseLong(bookIDField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            boolean isBorrowed = borrowedRadioButton.isSelected();
            SubStatus subStatus = isBorrowed ? SubStatus.Đang_Mượn : SubStatus.Đã_Trả;
            currentBorrowDetail = new BorrowDetailDTO(bookID, borrowSheetId, quantity, subStatus);
            if (borrowSheetId > 0) {
                borrowDetailBUS.updateBorrowDetail(currentBorrowDetail);
            }
            dispose();
        }
    }
}
