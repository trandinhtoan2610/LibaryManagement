package GUI.Component.Panel;

import BUS.BookBUS;
import BUS.EmployeeBUS;
import DTO.Book;
import DTO.BookViewModel;
import DTO.Employee;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddBookDialog;
import GUI.Component.Dialog.AddEmployeeDialog;
import GUI.Component.Dialog.DeleteBookDialog;
import GUI.Component.Dialog.EditBookDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Panel.Components.SearchNavBarLabel;
import GUI.Component.Table.BookTable;
import GUI.Component.Table.EmployeeTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BookPanel extends JPanel {
    private final BookBUS bookBUS = new BookBUS();
    private final BookTable bookTable = new BookTable();
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private SearchNavBarLabel searchNavBarLabel;
    private EmployeeBUS employeeBUS;
    private JFrame parentFrame;

    public BookPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(0, 17));
        this.add(emptyPanel, BorderLayout.SOUTH);
        loadData();
    }

    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);

        ButtonAdd buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddBookDialog addBookDialog = new AddBookDialog(parentFrame);
                addBookDialog.setVisible(true);
                loadData(); // Làm mới bảng sau khi thêm sách
            }
        });

        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lấy hàng được chọn từ bảng
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow == -1) {
                    // Nếu không có hàng nào được chọn, hiển thị thông báo
                    AlertDialog alertDialog = new AlertDialog(parentFrame, "Vui lòng chọn một cuốn sách để xóa");
                    alertDialog.setVisible(true);
                    return;
                }

                // Lấy ID của sách từ cột đầu tiên (cột ID)
                Long bookId = (Long) bookTable.getValueAt(selectedRow, 0);

                // Mở DeleteBookDialog với ID đã chọn
                DeleteBookDialog deleteBookDialog = new DeleteBookDialog(parentFrame, bookId);
                deleteBookDialog.setVisible(true);
                loadData(); // Làm mới bảng sau khi xóa
            }
        });

        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lấy hàng được chọn từ bảng
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow == -1) {
                    // Nếu không có hàng nào được chọn, hiển thị thông báo
                    AlertDialog alertDialog = new AlertDialog(parentFrame, "Vui lòng chọn một cuốn sách để chỉnh sửa");
                    alertDialog.setVisible(true);
                    return;
                }

                // Lấy ID của sách từ cột đầu tiên (cột ID)
                Long bookId = (Long) bookTable.getValueAt(selectedRow, 0);

                // Lấy đối tượng Book từ BookBUS
                Book selectedBook = bookBUS.getBookById(bookId);
                if (selectedBook == null) {
                    AlertDialog alertDialog = new AlertDialog(parentFrame, "Không tìm thấy sách với ID: " + bookId);
                    alertDialog.setVisible(true);
                    return;
                }

                // Mở EditBookDialog với đối tượng Book đã chọn
                EditBookDialog editBookDialog = new EditBookDialog(parentFrame, selectedBook);
                editBookDialog.setVisible(true);
                loadData(); // Làm mới bảng sau khi chỉnh sửa
            }
        });

        buttonExportExcel = new ButtonExportExcel();
        buttonImportExcel = new ButtonImportExcel();
        searchNavBarLabel = new SearchNavBarLabel();
        // Thêm listener cho tìm kiếm
        searchNavBarLabel.setSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = searchNavBarLabel.getSearchType();
                String keyword = searchNavBarLabel.getSearchKeyword();
                try {
                    List<BookViewModel> searchResults = bookBUS.searchBooks(type, keyword);
                    bookTable.setBooks(searchResults);
                } catch (Exception ex) {
                    AlertDialog alertDialog = new AlertDialog(parentFrame, "Lỗi tìm kiếm: " + ex.getMessage());
                    alertDialog.setVisible(true);
                    loadData(); // Làm mới bảng nếu có lỗi
                }
            }
        });
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonExportExcel);
        buttonPanel.add(buttonImportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private void loadData() {
        List<BookViewModel> books = bookBUS.getAllBooksForDisplay();
        if (books != null) {
            bookTable.setBooks(books);
        } else {
            System.out.println("Không có dữ liệu");
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý sách");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null); // căn giữa màn hình

            BookPanel bookPanel = new BookPanel(frame);
            frame.add(bookPanel);

            frame.setVisible(true);
        });
    }
}