package GUI.Component.Table;

import BUS.CategoryBUS;
import DTO.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryTable extends JTableCustom {
    private static final String[] HEADER = {
            "ID", "Tên danh mục"
    };

    private DefaultTableModel tableModel;
    private List<Category> categories;
    private CategoryBUS categoryBus = new CategoryBUS();

    public CategoryTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.categories = new ArrayList<>();

        // Cấu hình cơ bản
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);

        // Đặt độ rộng cột
        getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        getColumnModel().getColumn(1).setPreferredWidth(200);  // Tên danh mục
        setAutoCreateRowSorter(true);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories != null ? new ArrayList<>(categories) : new ArrayList<>();
        refreshTable();
    }

    public Category getSelectedCategory() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return categories.get(modelRow);
        }
        return null;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Category category : categories) {
            Object[] rowData = {
                    category.getId(),
                    category.getName()
            };
            tableModel.addRow(rowData);
        }
    }
}