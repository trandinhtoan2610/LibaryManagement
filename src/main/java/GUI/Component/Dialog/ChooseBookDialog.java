package GUI.Component.Dialog;

import BUS.BookBUS;
import DTO.BookViewModel;
import GUI.Component.Table.BookTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChooseBookDialog extends JDialog {
    private static final String HEADER[] = {
            "Tên sách", "Thể loại", "Tác giả",
            "Nhà xuất bản", "Số lượng", "Năm XB"
    };
    private final BookTable bookTable = new BookTable(HEADER);
    private final BookBUS bookBUS = new BookBUS();
    private BookViewModel selectedBook;
    private JDialog parentDialog;
    public ChooseBookDialog(JDialog parentDialog) {
        super(parentDialog, "Chọn sách", true);
        this.parentDialog = parentDialog;
        setSize(500, 300);
        setLocationRelativeTo(parentDialog);
        if (parentDialog != null) {
            Point location = this.getLocation();
            this.setLocation(location.x +50, location.y + 90);
        }
        setLayout(new BorderLayout(10, 10));
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
                    int selectedRow = bookTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedBook = bookTable.getSelectedBook();
                        dispose();
                    }
                }
            }
        });
        loadData();
    }

    public BookViewModel getSelectedBook() {
        return selectedBook;
    }

    private void loadData() {
        List<BookViewModel> books = bookBUS.getAllBooksForDisplay();
        if (books != null) {
            bookTable.setBookCustom(books);
        } else {
            System.out.println("Không có dữ liệu");
        }
    }
}