package GUI.Component.Table;

import BUS.BookBUS;
import DTO.BookViewModel;
import DTO.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class BookTable extends JTableCustom {
    private static final String[] HEADER = {
            "ID", "Tên sách", "Thể loại", "Tác giả",
            "Nhà xuất bản", "Số lượng", "Đơn giá", "Năm XB"
    };

    private DefaultTableModel tableModel;
    private List<BookViewModel> books;
    private BookBUS bookBus = new BookBUS();

    public BookTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.books = new ArrayList<>();

        // Cấu hình cơ bản
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);

        // Đặt độ rộng cột
        getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        getColumnModel().getColumn(1).setPreferredWidth(200);  // Tên sách
        getColumnModel().getColumn(2).setPreferredWidth(100);  // Thể loại
        getColumnModel().getColumn(3).setPreferredWidth(100);  // Tác giả
        getColumnModel().getColumn(4).setPreferredWidth(120);  // NXB
        getColumnModel().getColumn(5).setPreferredWidth(80);   // Số lượng
        getColumnModel().getColumn(6).setPreferredWidth(80);   // Đơn giá
        getColumnModel().getColumn(7).setPreferredWidth(80);   // Năm XB
        setAutoCreateRowSorter(true);
    }
    public BookTable(String[] HEADERCUSTOM){
        super(new DefaultTableModel(HEADERCUSTOM, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.books = new ArrayList<>();
        // Cấu hình cơ bản
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);

        setAutoCreateRowSorter(true);
    }
    public void setBooks(List<BookViewModel> books) {
        this.books = books != null ? new ArrayList<>(books) : new ArrayList<>();
        refreshTable();
    }
    public void setBookCustom(List<BookViewModel> books) {
        this.books = books != null ? new ArrayList<>(books) : new ArrayList<>();
        refreshTableCustom();
    }
    public BookViewModel getSelectedBook() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return books.get(modelRow);
        }
        return null;
    }
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (BookViewModel book : books) {
            Object[] rowData = {
                    book.getId(),
                    book.getName(),
                    book.getCategoryName(),  // Hiển thị tên danh mục
                    book.getAuthorName(),    // Hiển thị tên tác giả
                    book.getPublisherName(), // Hiển thị tên nhà xuất bản
                    book.getQuantity(),
                    book.getUnitPrice(),
                    book.getYearOfPublication() != null ? book.getYearOfPublication().toString() : "N/A"
            };
            tableModel.addRow(rowData);
        }
    }
    private void refreshTableCustom() {
        tableModel.setRowCount(0);
        for (BookViewModel book : books) {
            Object[] rowData = {
                    book.getName(),
                    book.getCategoryName(),  // Hiển thị tên danh mục
                    book.getAuthorName(),    // Hiển thị tên tác giả
                    book.getPublisherName(), // Hiển thị tên nhà xuất bản
                    book.getQuantity(),
                    book.getYearOfPublication() != null ? book.getYearOfPublication().toString() : "N/A"
            };
            tableModel.addRow(rowData);
        }
    }
}