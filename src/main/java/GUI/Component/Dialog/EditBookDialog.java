package GUI.Component.Dialog;

import BUS.BookBUS;
import DTO.Book;
import GUI.Component.Button.ButtonBack;
import GUI.Component.TextField.CustomTextField;

import javax.swing.*;
import java.awt.*;
import java.time.Year;

public class EditBookDialog extends JDialog {
    private final BookBUS bookBUS;
    private final Book book;
    private CustomTextField nameField;
    private CustomTextField categoryIdField;
    private CustomTextField authorIdField;
    private CustomTextField publisherIdField;
    private CustomTextField quantityField;
    private CustomTextField unitPriceField;
    private CustomTextField yearOfPublicationField;

    public EditBookDialog(JFrame parent, Book book) {
        super(parent, "Chỉnh Sửa Sách", true);
        this.bookBUS = new BookBUS();
        this.book = book;
        initComponent();
        pack();
        setLocationRelativeTo(parent);
    }

    public void initComponent() {
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel titlePanel = setTitle();
        getContentPane().add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = setContentPanel();
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = setButtonPanel();
        getContentPane().add(buttonPanel, BorderLayout.WEST);

        JPanel bottomPanel = setBottomPanel();
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
    }

    private JPanel setTitle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel txt = new JLabel("Chỉnh Sửa Sách");
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 36));
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
        panel.add(buttonBack);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel setContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, -2, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

        JLabel nameLabel = setJLabel("Tên Sách");
        JLabel categoryIdLabel = setJLabel("ID Danh Mục");
        JLabel authorIdLabel = setJLabel("ID Tác Giả");
        JLabel publisherIdLabel = setJLabel("ID Nhà Xuất Bản");
        JLabel quantityLabel = setJLabel("Số Lượng");
        JLabel unitPriceLabel = setJLabel("Đơn Giá");
        JLabel yearOfPublicationLabel = setJLabel("Năm Xuất Bản");

        nameField = new CustomTextField();
        nameField.setText(book.getName());
        categoryIdField = new CustomTextField();
        categoryIdField.setText(String.valueOf(book.getCategoryId()));
        authorIdField = new CustomTextField();
        authorIdField.setText(String.valueOf(book.getAuthorId()));
        publisherIdField = new CustomTextField();
        publisherIdField.setText(String.valueOf(book.getPublisherId()));
        quantityField = new CustomTextField();
        quantityField.setText(String.valueOf(book.getQuantity()));
        unitPriceField = new CustomTextField();
        unitPriceField.setText(String.valueOf(book.getUnitPrice()));
        yearOfPublicationField = new CustomTextField();
        yearOfPublicationField.setText(book.getYearOfPublication() != null ? String.valueOf(book.getYearOfPublication().getValue()) : "");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(categoryIdLabel);
        panel.add(categoryIdField);
        panel.add(authorIdLabel);
        panel.add(authorIdField);
        panel.add(publisherIdLabel);
        panel.add(publisherIdField);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(unitPriceLabel);
        panel.add(unitPriceField);
        panel.add(yearOfPublicationLabel);
        panel.add(yearOfPublicationField);

        return panel;
    }

    private JPanel setBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setBackground(new Color(218, 42, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dispose());

        JButton updateButton = new JButton("Cập nhật sách");
        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        updateButton.setPreferredSize(new Dimension(180, 40));
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> updateBook());

        bottomPanel.add(cancelButton);
        bottomPanel.add(updateButton);

        return bottomPanel;
    }

    private JLabel setJLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.WHITE);
        return label;
    }

    private void updateBook() {
        try {
            String name = nameField.getText().trim();
            String categoryIdStr = categoryIdField.getText().trim();
            String authorIdStr = authorIdField.getText().trim();
            String publisherIdStr = publisherIdField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String unitPriceStr = unitPriceField.getText().trim();
            String yearOfPublicationStr = yearOfPublicationField.getText().trim();

            // Kiểm tra dữ liệu
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Tên sách không được để trống");
            }

            Long categoryId = Long.parseLong(categoryIdStr);
            if (categoryId <= 0) {
                throw new IllegalArgumentException("ID danh mục không hợp lệ");
            }

            Long authorId = Long.parseLong(authorIdStr);
            if (authorId <= 0) {
                throw new IllegalArgumentException("ID tác giả không hợp lệ");
            }

            Long publisherId = Long.parseLong(publisherIdStr);
            if (publisherId <= 0) {
                throw new IllegalArgumentException("ID nhà xuất bản không hợp lệ");
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                throw new IllegalArgumentException("Số lượng không được âm");
            }

            Long unitPrice = Long.parseLong(unitPriceStr);
            if (unitPrice < 0) {
                throw new IllegalArgumentException("Đơn giá không được âm");
            }

            Year yearOfPublication = null;
            if (!yearOfPublicationStr.isEmpty()) {
                int year = Integer.parseInt(yearOfPublicationStr);
                if (year > Year.now().getValue()) {
                    throw new IllegalArgumentException("Năm xuất bản không được lớn hơn năm hiện tại");
                }
                yearOfPublication = Year.of(year);
            }

            // Cập nhật thông tin sách
            book.setName(name);
            book.setCategoryId(categoryId);
            book.setAuthorId(authorId);
            book.setPublisherId(publisherId);
            book.setQuantity(quantity);
            book.setUnitPrice(unitPrice);
            book.setYearOfPublication(yearOfPublication);

            // Gọi BookBUS để cập nhật sách
            bookBUS.updateBook(book);

            // Hiển thị thông báo thành công
            AlertDialog successDialog = new AlertDialog(this, "Cập nhật sách thành công với ID: " + book.getId());
            successDialog.setVisible(true);
            dispose(); // Đóng hộp thoại sau khi cập nhật thành công
        } catch (NumberFormatException e) {
            AlertDialog errorDialog = new AlertDialog(this, "Vui lòng nhập số hợp lệ cho các trường ID, số lượng, đơn giá, năm xuất bản");
            errorDialog.setVisible(true);
        } catch (IllegalArgumentException e) {
            AlertDialog errorDialog = new AlertDialog(this, e.getMessage());
            errorDialog.setVisible(true);
        } catch (Exception e) {
            AlertDialog errorDialog = new AlertDialog(this, "Lỗi khi cập nhật sách: " + e.getMessage());
            errorDialog.setVisible(true);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Book book1 = new Book();
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EditBookDialog dialog = new EditBookDialog(frame,book1);
        dialog.setVisible(true);
    }
}