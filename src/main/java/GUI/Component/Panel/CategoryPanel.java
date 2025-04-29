package GUI.Component.Panel;

import BUS.CategoryBUS;
import DTO.Category;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddCategoryDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeleteCategoryDialog;
import GUI.Component.Dialog.EditCategoryDialog;
import GUI.Component.Panel.Components.CategorySearchBar;

import GUI.Component.Table.CategoryTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryPanel extends JPanel {
    private final CategoryBUS categoryBUS = new CategoryBUS();
    private final CategoryTable categoryTable = new CategoryTable();
    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private CategorySearchBar searchBar;
    private JFrame parentFrame;

    public CategoryPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(0, 17));
        this.add(emptyPanel, BorderLayout.SOUTH);
        loadData();
    }

    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);

        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddCategoryDialog addCategoryDialog = new AddCategoryDialog(parentFrame);
                addCategoryDialog.setVisible(true);
                loadData();
            }
        });

        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = categoryTable.getSelectedRow();
                if (selectedRow == -1) {
                    AlertDialog alertDialog = new AlertDialog(parentFrame, "Vui lòng chọn một danh mục để xóa");
                    alertDialog.setVisible(true);
                    return;
                }
                Long categoryId = (Long) categoryTable.getValueAt(selectedRow, 0);
                DeleteCategoryDialog deleteCategoryDialog = new DeleteCategoryDialog(parentFrame, categoryId);
                deleteCategoryDialog.setVisible(true);
                loadData();
            }
        });

        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = categoryTable.getSelectedRow();
                if (selectedRow == -1) {
                    AlertDialog alertDialog = new AlertDialog(parentFrame, "Vui lòng chọn một danh mục để chỉnh sửa");
                    alertDialog.setVisible(true);
                    return;
                }
                Long categoryId = (Long) categoryTable.getValueAt(selectedRow, 0);
                Category selectedCategory = categoryBUS.getCategoryById(categoryId);
                if (selectedCategory == null) {
                    AlertDialog alertDialog = new AlertDialog(parentFrame, "Không tìm thấy danh mục với ID: " + categoryId);
                    alertDialog.setVisible(true);
                    return;
                }
                EditCategoryDialog editCategoryDialog = new EditCategoryDialog(parentFrame, selectedCategory);
                editCategoryDialog.setVisible(true);
                loadData();
            }
        });

        searchBar = new CategorySearchBar();
        searchBar.setSearchListener(e -> {
            String keyword = searchBar.getSearchKeyword();
            try {
                List<Category> searchResults = searchCategories(keyword);
                categoryTable.setCategories(searchResults);
            } catch (Exception ex) {
                AlertDialog alertDialog = new AlertDialog(parentFrame, "Lỗi tìm kiếm: " + ex.getMessage());
                alertDialog.setVisible(true);
                loadData();
            }
        });

        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(searchBar);
        return buttonPanel;
    }

    private void loadData() {
        List<Category> categories = categoryBUS.getAllCategories();
        if (categories != null) {
            categoryTable.setCategories(categories);
        } else {
            System.out.println("Không có dữ liệu danh mục");
        }
    }

    private List<Category> searchCategories(String keyword) {
        List<Category> allCategories = categoryBUS.getAllCategories();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allCategories;
        }
        return allCategories.stream()
                .filter(category -> category.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}