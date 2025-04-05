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
}