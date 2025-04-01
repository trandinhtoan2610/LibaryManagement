package BUS;

import DAL.CategoryDAL;
import DAL.Interface.IRepositoryBase;
import DTO.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryBUS {
    private final IRepositoryBase<Category> categoryRepository;

    public CategoryBUS() {
        this.categoryRepository = new CategoryDAL();
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            categories = categoryRepository.findAll();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách danh mục: " + e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách danh mục", e);
        }
        return categories;
    }

    public Category getCategoryById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        try {
            return categoryRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh mục với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể lấy danh mục", e);
        }
    }

    public Long addCategory(Category category) {
        validateCategory(category);
        try {
            return categoryRepository.create(category);
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm danh mục: " + e.getMessage());
            throw new RuntimeException("Không thể thêm danh mục", e);
        }
    }

    public void updateCategory(Category category) {
        if (category.getId() == null || category.getId() <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        validateCategory(category);
        try {
            if (!categoryRepository.update(category)) {
                throw new RuntimeException("Cập nhật danh mục thất bại");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật danh mục: " + e.getMessage());
            throw new RuntimeException("Không thể cập nhật danh mục", e);
        }
    }

    public void deleteCategory(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        try {
            if (!categoryRepository.delete(id)) {
                throw new RuntimeException("Xóa danh mục thất bại");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa danh mục: " + e.getMessage());
            throw new RuntimeException("Không thể xóa danh mục", e);
        }
    }

    private void validateCategory(Category category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
    }
}