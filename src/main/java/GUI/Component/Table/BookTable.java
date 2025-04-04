/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author DELL
 */



package GUI.Component.Table;

import DTO.Book;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class BookTable extends JTableCustom {
    private static final String[] HEADER = {
        "ID", "Tên sách", "Thể loại", "Tác giả",
        "Nhà xuất bản", "Kệ sách", "Số lượng", "Năm XB"
    };

    private final DefaultTableModel tableModel;
    private List<Book> books;

    public BookTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.books = new ArrayList<>();

        // Cấu hình cơ bản
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Đặt độ rộng cột
        getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        getColumnModel().getColumn(1).setPreferredWidth(200);  // Tên sách
        getColumnModel().getColumn(2).setPreferredWidth(100);  // Thể loại
        getColumnModel().getColumn(3).setPreferredWidth(100);  // Tác giả
        getColumnModel().getColumn(4).setPreferredWidth(120);  // NXB
        getColumnModel().getColumn(5).setPreferredWidth(80);   // Kệ sách
        getColumnModel().getColumn(6).setPreferredWidth(80);   // Số lượng
        getColumnModel().getColumn(7).setPreferredWidth(80);   // Năm XB

        // Bật sắp xếp theo cột
        setAutoCreateRowSorter(true);
    }

    public void setBooks(List<Book> books) {
        this.books = books != null ? new ArrayList<>(books) : new ArrayList<>();
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Book book : books) {
            Object[] rowData = {
                book.getId(),
                book.getName(),
                book.getCategoryId(),
                book.getAuthorId(),
                book.getPublisherId(),
                book.getUnitPrice(),
                book.getQuantity(),
                book.getYearOfPublication() != null ? book.getYearOfPublication().toString() : "N/A"
            };
            tableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        // Tạo dữ liệu mẫu
        List<Book> sampleBooks = new ArrayList<>();
        
        // Thêm sách 1
        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("Đắc Nhân Tâm");
        book1.setCategoryId(3L);
        book1.setAuthorId(10L);
        book1.setPublisherId(5L);
        book1.setUnitPrice(2L);
        book1.setQuantity(50);
        book1.setYearOfPublication(Year.of(2023));
        sampleBooks.add(book1);

        // Thêm sách 2
        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Nhà Giả Kim");
        book2.setCategoryId(3L);
        book2.setAuthorId(15L);
        book2.setPublisherId(7L);
        book2.setUnitPrice(1L);
        book2.setQuantity(30);
        book2.setYearOfPublication(Year.of(2020));
        sampleBooks.add(book2);

        // Hiển thị bảng
        JFrame frame = new JFrame("Quản lý Sách");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 300);

        BookTable table = new BookTable();
        table.setBooks(sampleBooks);

        frame.add(new JScrollPane(table));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}