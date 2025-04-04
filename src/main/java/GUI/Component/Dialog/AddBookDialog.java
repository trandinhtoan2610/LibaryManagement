package GUI.Component.Dialog;

import BUS.BookBUS;
import BUS.CategoryBUS;
import BUS.AuthorBUS;
import BUS.PublisherBUS;
import DTO.Book;
import DTO.Category;
import DTO.AuthorDTO;
import DTO.Publisher;
import GUI.Component.Button.ButtonBack;
import GUI.Component.Combobox.CustomComboBox;
import GUI.Component.TextField.CustomTextField;

import javax.swing.*;
import java.awt.*;
import java.time.Year;
import java.util.List;

public class AddBookDialog extends JDialog {
    private final BookBUS bookBUS;
    private final CategoryBUS categoryBUS;
    private final AuthorBUS authorBUS;
    private final PublisherBUS publisherBUS;
    private CustomTextField nameField;
    private CustomComboBox categoryComboBox;
    private CustomComboBox authorComboBox;
    private CustomComboBox publisherComboBox;
    private CustomTextField quantityField;
    private CustomTextField unitPriceField;
    private CustomTextField yearOfPublicationField;

    public AddBookDialog(JFrame parent) {
        super(parent, "Thêm Sách", true);
        this.bookBUS = new BookBUS();
        this.categoryBUS = new CategoryBUS();
        this.authorBUS = new AuthorBUS();
        this.publisherBUS = new PublisherBUS();
        initComponent();
        pack();
        setLocationRelativeTo(parent);
    }

    public void initComponent() {
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel titlePanel = settitle();
        getContentPane().add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = setContentPanel();
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = setbuttonPanel();
        getContentPane().add(buttonPanel, BorderLayout.WEST);

        JPanel bottomPanel = setBottomPanel();
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
    }

    private JPanel settitle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel txt = new JLabel("Thêm Sách");
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

    private JPanel setbuttonPanel() {
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
        JLabel categoryLabel = setJLabel("Danh Mục");
        JLabel authorLabel = setJLabel("Tác Giả");
        JLabel publisherLabel = setJLabel("Nhà Xuất Bản");
        JLabel quantityLabel = setJLabel("Số Lượng");
        JLabel unitPriceLabel = setJLabel("Đơn Giá");
        JLabel yearOfPublicationLabel = setJLabel("Năm Xuất Bản");

        nameField = new CustomTextField();
        categoryComboBox = new CustomComboBox();
        authorComboBox = new CustomComboBox();
        publisherComboBox = new CustomComboBox();
        quantityField = new CustomTextField();
        unitPriceField = new CustomTextField();
        yearOfPublicationField = new CustomTextField();

        // Load danh sách danh mục
        List<Category> categories = categoryBUS.getAllCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category.getName());
        }

        // Load danh sách tác giả
        List<AuthorDTO> authors = authorBUS.getAllAuthors();
        for (AuthorDTO author : authors) {
            authorComboBox.addItem(author.getName());
        }

        // Load danh sách nhà xuất bản
        List<Publisher> publishers = publisherBUS.getAllPublishers();
        for (Publisher publisher : publishers) {
            publisherComboBox.addItem(publisher.getName());
        }

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(categoryLabel);
        panel.add(categoryComboBox);
        panel.add(authorLabel);
        panel.add(authorComboBox);
        panel.add(publisherLabel);
        panel.add(publisherComboBox);
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

        JButton addButton = new JButton("Thêm sách");
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addButton.setPreferredSize(new Dimension(180, 40));
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addBook());

        bottomPanel.add(cancelButton);
        bottomPanel.add(addButton);

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

    private void addBook() {
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            String name = nameField.getText().trim();
            int categoryIndex = categoryComboBox.getSelectedIndex();
            int authorIndex = authorComboBox.getSelectedIndex();
            int publisherIndex = publisherComboBox.getSelectedIndex();
            String quantityStr = quantityField.getText().trim();
            String unitPriceStr = unitPriceField.getText().trim();
            String yearOfPublicationStr = yearOfPublicationField.getText().trim();

            // Kiểm tra dữ liệu
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Tên sách không được để trống");
            }

            List<Category> categories = categoryBUS.getAllCategories();
            Long categoryId = categories.get(categoryIndex).getId();
            if (categoryId <= 0) {
                throw new IllegalArgumentException("Danh mục không hợp lệ");
            }

            List<AuthorDTO> authors = authorBUS.getAllAuthors();
            Long authorId = authors.get(authorIndex).getId();
            if (authorId <= 0) {
                throw new IllegalArgumentException("Tác giả không hợp lệ");
            }

            List<Publisher> publishers = publisherBUS.getAllPublishers();
            Long publisherId = publishers.get(publisherIndex).getId();
            if (publisherId <= 0) {
                throw new IllegalArgumentException("Nhà xuất bản không hợp lệ");
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

            // Tạo đối tượng Book
            Book book = new Book();
            book.setName(name);
            book.setCategoryId(categoryId);
            book.setAuthorId(authorId);
            book.setPublisherId(publisherId);
            book.setQuantity(quantity);
            book.setUnitPrice(unitPrice);
            book.setYearOfPublication(yearOfPublication);

            // Gọi BookBUS để thêm sách
            Long newBookId = bookBUS.addBook(book);
            if (newBookId != null && newBookId > 0) {
                // Hiển thị thông báo thành công
                AlertDialog successDialog = new AlertDialog(this, "Thêm sách thành công với ID: " + newBookId);
                successDialog.setVisible(true);
                dispose(); // Đóng hộp thoại sau khi thêm thành công
            } else {
                throw new RuntimeException("Thêm sách thất bại");
            }
        } catch (NumberFormatException e) {
            AlertDialog errorDialog = new AlertDialog(this, "Vui lòng nhập số hợp lệ cho các trường số lượng, đơn giá, năm xuất bản");
            errorDialog.setVisible(true);
        } catch (IllegalArgumentException e) {
            AlertDialog errorDialog = new AlertDialog(this, e.getMessage());
            errorDialog.setVisible(true);
        } catch (Exception e) {
            AlertDialog errorDialog = new AlertDialog(this, "Lỗi khi thêm sách: " + e.getMessage());
            errorDialog.setVisible(true);
        }
    }
}