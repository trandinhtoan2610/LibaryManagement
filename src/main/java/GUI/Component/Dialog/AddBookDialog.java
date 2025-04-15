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
    private AuthorTable authorTable;
    private CustomTextField txtAuthor;


    public AddBookDialog(JFrame parent) {
        super(parent, "Thêm Sách", true);
        this.bookBUS = new BookBUS();
        this.categoryBUS = new CategoryBUS();
        this.authorBUS = new AuthorBUS();
        this.publisherBUS = new PublisherBUS();
        initComponent();
        pack();
        setLocationRelativeTo(parent);
        isResizable();
    }

    public void initComponent() {
        getContentPane().setLayout(new BorderLayout(5, 10));
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
        JPanel subPanel = new JPanel();
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

        //tác giả :
        authorTable = new AuthorTable();
        authorTable.removeColumn(authorTable.getColumnModel().getColumn(2));
        txtAuthor = new CustomTextField();
        subPanel.setLayout(new GridLayout(1, 2, 10, 0)); // spacing giữa 2 cột
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


        // Load danh sách danh mục
        List<Category> categories = categoryBUS.getAllCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category.getName());
        }

        // Load danh sách tác giả
//        List<AuthorDTO> authors = authorBUS.getAllAuthors();
//        for (AuthorDTO author : authors) {
//            authorComboBox.addItem(author.getName());
//        }




        // Load danh sách nhà xuất bản
        List<PublisherDTO> publishers = publisherBUS.getAllPublishers();
        for (PublisherDTO publisher : publishers) {
            publisherComboBox.addItem(publisher.getName());
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

            if(isExist) {
                AuthorDTO updateAuthor = authorBUS.getAuthorById(authorID);
                updateAuthor.setProductQuantity(updateAuthor.getProductQuantity()+1); // increase quantity by 1
                authorBUS.updateAuthor(updateAuthor);
            }

            //Nếu chưa tồn tại :
            else{
                int firstSpace = authorName.indexOf(" ");
                String lastName = authorName.substring(0, firstSpace);        // Lấy họ
                String firstName = authorName.substring(firstSpace + 1);
                //Thêm mới tác giả
                AuthorDTO a = new AuthorDTO(authorBUS.getAuthorMaxID()+1,lastName,firstName,1);
                authorBUS.addAuthor(a);
                authorID = a.getId();
            }

            // Tạo đối tượng Book
            Book book = new Book();
            book.setName(name);
            book.setCategoryId(categoryId);
            book.setAuthorId(authorID);
            book.setPublisherId(publisherId);
            book.setQuantity(quantity);
            book.setUnitPrice(unitPrice);
            book.setYearOfPublication(yearOfPublication);

            // Gọi BookBUS để thêm sách
            try {
                Long newBookId = bookBUS.addBook(book);

                    //Update Author table for this panel and AuthorPanel :
                    authorTable.resetTable();
                    AuthorPanel.tblAuthor.resetTable();


                    // Hiển thị thông báo thành công
                    AlertDialog successDialog = new AlertDialog(this, "Thêm sách thành công với ID: " + newBookId);
                    successDialog.setVisible(true);
                    dispose(); // Đóng hộp thoại sau khi thêm thành công
            }catch (Exception e ){
                //ROLLBACK :
                if (!isExist)
                    authorBUS.deleteAuthor(authorID); //delete an author created before
                else {
                    AuthorDTO updateAuthor = authorBUS.getAuthorById(authorID);
                    updateAuthor.setProductQuantity(updateAuthor.getProductQuantity()-1); // rollback author quantity if error.
                    authorBUS.updateAuthor(updateAuthor);
                }
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