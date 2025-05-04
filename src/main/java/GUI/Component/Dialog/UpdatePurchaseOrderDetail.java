package GUI.Component.Dialog;

import BUS.BookBUS;
import BUS.PurchaseOrderDetailBUS;
import DTO.BookViewModel;
import DTO.BorrowDetailDTO;
import DTO.PurchaseOrderDTO;
import DTO.PurchaseOrderDetailDTO;
import GUI.Component.Button.ButtonBack;
import GUI.Component.Button.ButtonChosen;
import GUI.Component.TextField.CustomTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;

public class UpdatePurchaseOrderDetail extends JDialog{
    private final PurchaseOrderDetailBUS purchaseOrderDetailsBUS = new PurchaseOrderDetailBUS();
    private final BookBUS bookBUS = new BookBUS();
    private BookViewModel currentBook;
    private PurchaseOrderDetailDTO currentOrderDetail;

    private final CustomTextField bookIDField = new CustomTextField();
    private final CustomTextField quantityField = new CustomTextField();
    private final CustomTextField unitPriceField = new CustomTextField();
    private final ButtonChosen buttonChosenBook = new ButtonChosen();

    private final JLabel titleLabel = new JLabel("Tên sách:             ");
    private final JLabel authorLabel = new JLabel("Tác giả:             ");
    private final JLabel categoryLabel = new JLabel("Thể loại:             ");
    private final JLabel publisherLabel = new JLabel("NXB:             ");
    private final JLabel yearLabel = new JLabel("Năm:             ");
    private final JLabel subTotalLabel = new JLabel("Thành tiền:             ");

    private PurchaseOrderDetailDTO purchaseOrderDetailToUpdate;
    private JDialog parentDialog;
    public UpdatePurchaseOrderDetail(JDialog parentDialog, PurchaseOrderDetailDTO purchaseOrderDetailToUpdate ) {
        super(parentDialog, "Cập Nhật Chi Tiết Phiếu Nhập", true);
        this.parentDialog = parentDialog;
        this.purchaseOrderDetailToUpdate = purchaseOrderDetailToUpdate;
        initComponents();
        setSize(650, 500);
        setLocationRelativeTo(parentDialog);
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
        bookIDField.setEditable(false);
        bookIDField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updateBookInfo(); }
            @Override public void removeUpdate(DocumentEvent e) { updateBookInfo(); }
            @Override public void changedUpdate(DocumentEvent e) { updateBookInfo(); }
        });
        gbc.gridx = 1;
        panel.add(bookIDField, gbc);
        buttonChosenBook.setEnabled(false);
        buttonChosenBook.addActionListener(e -> chooseBook());
        gbc.gridx = 2;
        panel.add(buttonChosenBook, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        panel.add(createSeparator(), gbc);

        // Book info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 25, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 16);
        titleLabel.setFont(infoFont);
        authorLabel.setFont(infoFont);
        categoryLabel.setFont(infoFont);
        publisherLabel.setFont(infoFont);
        yearLabel.setFont(infoFont);
        subTotalLabel.setFont(infoFont);

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(publisherLabel);
        infoPanel.add(yearLabel);
        infoPanel.add(subTotalLabel);

        gbc.gridy = 2; gbc.gridwidth = 3;
        panel.add(infoPanel, gbc);

        // Quantity row
        gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(new JLabel("Số lượng:"), gbc);

        quantityField.setPreferredSize(new Dimension(80, 30));
        quantityField.setText(new BigDecimal(purchaseOrderDetailToUpdate.getQuantity()).toString());
        quantityField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { calculateSubTotal(); }
            @Override public void removeUpdate(DocumentEvent e) { calculateSubTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { calculateSubTotal(); }
        });
        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        // Unit price row
        gbc.gridy = 4; gbc.gridx = 0;
        panel.add(new JLabel("Đơn giá:"), gbc);

        unitPriceField.setPreferredSize(new Dimension(120, 30));
        unitPriceField.setText(new BigDecimal(String.valueOf(purchaseOrderDetailToUpdate.getUnitPrice())).toString());
        unitPriceField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { calculateSubTotal(); }
            @Override public void removeUpdate(DocumentEvent e) { calculateSubTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { calculateSubTotal(); }
        });
        gbc.gridx = 1;
        panel.add(unitPriceField, gbc);

        return panel;
    }

    private void updateBookInfo() {
        String bookIDText = purchaseOrderDetailToUpdate.getBookId().toString();
        if (bookIDText.isEmpty()) {
            currentBook = null;
            clearBookInfo();
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
            } else {
                clearBookInfo();
            }
        } catch (NumberFormatException e) {
            clearBookInfo();
        }
    }

    private void clearBookInfo() {
        titleLabel.setText("Tên sách:            ");
        authorLabel.setText("Tác giả:            ");
        categoryLabel.setText("Thể loại:            ");
        publisherLabel.setText("NXB:            ");
        yearLabel.setText("Năm:            ");
        subTotalLabel.setText("Thành tiền:            ");
    }

    private void calculateSubTotal() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            BigDecimal unitPrice = new BigDecimal(unitPriceField.getText());
            BigDecimal subTotal = unitPrice.multiply(new BigDecimal(quantity));
            subTotalLabel.setText("Thành tiền: " + subTotal.toString());
        } catch (NumberFormatException e) {
            subTotalLabel.setText("Thành tiền:            ");
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

        JButton addButton = new JButton("Cập nhật");
        addButton.setPreferredSize(new Dimension(120, 30));
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> updatePurchaseOrderDetails());

        panel.add(cancelButton);
        panel.add(addButton);

        return panel;
    }

    private boolean isValidInput() {
        if (currentBook == null || bookIDField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sách hoặc chọn sách!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (quantityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (unitPriceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            BigDecimal unitPrice = new BigDecimal(unitPriceField.getText());
            if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
    public PurchaseOrderDetailDTO getCurrentOrderDetail() {
        return currentOrderDetail;
    }
    private void updatePurchaseOrderDetails(){
        if (!isValidInput()) {
            return;
        }
        int quantity = Integer.parseInt(quantityField.getText());
        BigDecimal unitPrice = new BigDecimal(unitPriceField.getText());

        PurchaseOrderDetailDTO purchaseOrderDetailDTO = new PurchaseOrderDetailDTO();
        purchaseOrderDetailDTO.setPurchaseOrderId(purchaseOrderDetailToUpdate.getPurchaseOrderId());
        purchaseOrderDetailDTO.setBookId(purchaseOrderDetailToUpdate.getBookId());
        purchaseOrderDetailDTO.setQuantity(quantity);
        purchaseOrderDetailDTO.setUnitPrice(unitPrice);
        purchaseOrderDetailDTO.setSubTotal(unitPrice.multiply(new BigDecimal(quantity)));

        if (purchaseOrderDetailsBUS.updatePurchaseOrderDetail(purchaseOrderDetailDTO)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            parentDialog.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }
}
