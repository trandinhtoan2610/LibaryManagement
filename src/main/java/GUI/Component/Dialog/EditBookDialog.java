package GUI.Component.Dialog;

import BUS.BookBUS;
import BUS.CategoryBUS;
import BUS.AuthorBUS;
import BUS.PublisherBUS;
import DTO.Book;
import DTO.Category;
import DTO.AuthorDTO;
import DTO.PublisherDTO;
import GUI.Component.Button.ButtonBack;
import GUI.Component.Combobox.CustomComboBox;
import GUI.Component.Panel.AuthorPanel;
import GUI.Component.Table.AuthorTable;
import GUI.Component.TextField.CustomTextField;
import GUI.Controller.Controller;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.util.List;

public class EditBookDialog extends JDialog {
    private final BookBUS bookBUS;
    private final CategoryBUS categoryBUS;
    private final AuthorBUS authorBUS;
    private final PublisherBUS publisherBUS;
    private final Book book; // Sách cần chỉnh sửa
    private CustomTextField nameField;
    private CustomComboBox categoryComboBox;
    private CustomComboBox authorComboBox;
    private CustomComboBox publisherComboBox;
    private CustomTextField quantityField;
    private CustomTextField unitPriceField;
    private CustomTextField yearOfPublicationField;
    private AuthorTable authorTable;
    private CustomTextField txtAuthor;


    public EditBookDialog(JFrame parent, Book book) {
        super(parent, "Chỉnh Sửa Sách", true);
        this.bookBUS = new BookBUS();
        this.categoryBUS = new CategoryBUS();
        this.authorBUS = new AuthorBUS();
        this.publisherBUS = new PublisherBUS();
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
        JPanel subPanel = new JPanel(); //Panel for author input
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
        nameField.setText(book.getName());
        categoryComboBox = new CustomComboBox();
//        authorComboBox = new CustomComboBox();
        publisherComboBox = new CustomComboBox();
        quantityField = new CustomTextField();
        quantityField.setText(String.valueOf(book.getQuantity()));
        unitPriceField = new CustomTextField();
        unitPriceField.setText(String.valueOf(book.getUnitPrice()));
        yearOfPublicationField = new CustomTextField();
        yearOfPublicationField.setText(book.getYearOfPublication() != null ? String.valueOf(book.getYearOfPublication().getValue()) : "");

        //tác giả :
        authorTable = new AuthorTable();
        authorTable.removeColumn(authorTable.getColumnModel().getColumn(2));
        txtAuthor = new CustomTextField();
        String authorName = book.getAuthor().getLastName() + " " + book.getAuthor().getFirstName();
        txtAuthor.setText(authorName);
        authorTable.filterTable(authorName);
        subPanel.setLayout(new GridLayout(1, 2, 10, 0));
        JScrollPane scrollPane = new JScrollPane(authorTable);
        scrollPane.setPreferredSize(new Dimension(200, 40));
        subPanel.add(txtAuthor);
        subPanel.add(scrollPane);

        //Lọc table theo input trong txtfield :
        txtAuthor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                authorTable.filterTable(txtAuthor.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                authorTable.filterTable(txtAuthor.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                authorTable.filterTable(txtAuthor.getText());
            }
        });

        //Chọn tác giả trong table :
        authorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AuthorDTO a = authorTable.getSelectedAuthor();
                if(a != null){
                    txtAuthor.setText(Controller.formatFullName(a.getName()));
                }
            }
        });

        // Load danh sách danh mục và chọn danh mục hiện tại
        List<Category> categories = categoryBUS.getAllCategories();
        int selectedCategoryIndex = -1;
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            categoryComboBox.addItem(category.getName());
            if (category.getId().equals(book.getCategoryId())) {
                selectedCategoryIndex = i;
            }
        }
        if (selectedCategoryIndex != -1) {
            categoryComboBox.setSelectedIndex(selectedCategoryIndex);
        }

//        // Load danh sách tác giả và chọn tác giả hiện tại
//        List<AuthorDTO> authors = authorBUS.getAllAuthors();
//        int selectedAuthorIndex = -1;
//        for (int i = 0; i < authors.size(); i++) {
//            AuthorDTO author = authors.get(i);
//            authorComboBox.addItem(author.getName());
//            if (author.getId() == book.getAuthorId()) {
//                selectedAuthorIndex = i;
//            }
//        }
//        if (selectedAuthorIndex != -1) {
//            authorComboBox.setSelectedIndex(selectedAuthorIndex);
//        }

        // Load danh sách nhà xuất bản và chọn nhà xuất bản hiện tại
        List<PublisherDTO> publishers = publisherBUS.getAllPublishers();
        int selectedPublisherIndex = -1;
        for (int i = 0; i < publishers.size(); i++) {
            PublisherDTO publisher = publishers.get(i);
            publisherComboBox.addItem(publisher.getName());
            if (publisher.getId() == (book.getPublisherId())) {
                selectedPublisherIndex = i;
            }
        }
        if (selectedPublisherIndex != -1) {
            publisherComboBox.setSelectedIndex(selectedPublisherIndex);
        }

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(categoryLabel);
        panel.add(categoryComboBox);
        panel.add(authorLabel);
        panel.add(subPanel);
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
        boolean isSucces = true;
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            String name = nameField.getText().trim();
            int categoryIndex = categoryComboBox.getSelectedIndex();

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

//            Long authorId = authorBUS.authorDTOList.get(authorIndex-1).getId();
//            if (authorId <= 0) {
//                throw new IllegalArgumentException("Tác giả không hợp lệ");
//            }

            List<PublisherDTO> publishers = publisherBUS.getAllPublishers();
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

            //Xử lý tác giả :
            Long authorID = null;
            boolean isExist = false;
            boolean isChange = false;
            String authorName = Controller.formatFullName(txtAuthor.getText());

            for (AuthorDTO a : AuthorBUS.authorDTOList){
                String aName = a.getLastName() + " " + a.getFirstName();
                //Nếu đã tồn tại tác giả trong database :
                if(aName.equals(authorName)){
                    authorID = a.getId();
                    isExist = true;
                    break;
                }
            }

            //Nếu tác giả có trong database :
            if(isExist){
                isChange = authorID != book.getAuthorId() ? true : false;
                if(isChange) { //Nếu tác giả bị thay đổi
                    // -> Cập nhật lại sl tác phẩm cho tác giả mới
                    AuthorDTO newAuthor = authorBUS.getAuthorById(authorID);
                    newAuthor.setProductQuantity(newAuthor.getProductQuantity()+1);
                    authorBUS.updateAuthor(newAuthor);
                }
            }

            //Nếu chưa tồn tại :
            else{
                int firstSpace = authorName.indexOf(" ");
                String lastName = authorName.substring(0, firstSpace);        // Lấy họ
                String firstName = authorName.substring(firstSpace + 1);
                //Thêm tác giả mới
                AuthorDTO a = new AuthorDTO(authorBUS.getAuthorMaxID()+1,lastName,firstName,1);
                authorBUS.addAuthor(a);
                authorID = a.getId();
            }

            //Cập nhật lại sl tác phẩm cho tác giả cũ
            if(isChange || !isExist) {
                AuthorDTO oldAuthor = authorBUS.getAuthorById(book.getAuthorId());
                oldAuthor.setProductQuantity(oldAuthor.getProductQuantity() - 1);
                authorBUS.updateAuthor(oldAuthor);
            }

            Long prevID = book.getAuthorId(); // ID của tác giả cũ -> dùng cho rollback data nếu có sự cố

            // Gọi BookBUS để cập nhật sách
            try {
                // Cập nhật thông tin sách
                book.setName(name);
                book.setCategoryId(categoryId);
                book.setAuthorId(authorID);
                book.setPublisherId(publisherId);
                book.setQuantity(quantity);
                book.setUnitPrice(unitPrice);
                book.setYearOfPublication(yearOfPublication);
                bookBUS.updateBook(book);

                // Hiển thị thông báo thành công
                AlertDialog successDialog = new AlertDialog(this, "Cập nhật sách thành công với ID: " + book.getId());
                successDialog.setVisible(true);
                dispose(); // Đóng hộp thoại sau khi cập nhật thành công
            }catch (Exception e){

                AlertDialog updateFailAlert = new AlertDialog(this, "Có lỗi khi cập nhật sách !");
                //ROLLBACK :
                isSucces = false;
                if(!isExist){
                    authorBUS.deleteAuthor(authorID);  //xóa tác giả vừa thêm
                }

                if(isChange){  // Cập nhật lại sl tác phẩm của tác giả mới
                    AuthorDTO newAuthor = authorBUS.getAuthorById(authorID);
                    newAuthor.setProductQuantity(newAuthor.getProductQuantity()-1);
                    authorBUS.updateAuthor(newAuthor);
                }

                //Cập nhật lại sl tác phẩm cho tác giả cũ
                if(isChange || !isExist) {
                    AuthorDTO oldAuthor = authorBUS.getAuthorById(prevID);
                    oldAuthor.setProductQuantity(oldAuthor.getProductQuantity() + 1);
                    authorBUS.updateAuthor(oldAuthor);
                }
                AuthorPanel.tblAuthor.resetTable();
                authorTable.resetTable();
                updateFailAlert.setVisible(true);
            }

        } catch (NumberFormatException e) {
            AlertDialog errorDialog = new AlertDialog(this, "Vui lòng nhập số hợp lệ cho các trường số lượng, đơn giá, năm xuất bản");
            errorDialog.setVisible(true);
        } catch (IllegalArgumentException e) {
            AlertDialog errorDialog = new AlertDialog(this, e.getMessage());
            errorDialog.setVisible(true);
        } catch (Exception e) {
            AlertDialog errorDialog = new AlertDialog(this, "Lỗi khi cập nhật sách: " + e.getMessage());
            errorDialog.setVisible(true);
        }

        if (isSucces) {
            authorBUS.deleteUnusedAuthor();
            AuthorPanel.tblAuthor.resetTable();
            authorTable.resetTable();
        }
    }
}