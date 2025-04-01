package DAL;

import DAL.Interface.IRepositoryBase;
import DTO.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAL implements IRepositoryBase<Category> {

    private Category resultSetToCategory(ResultSet rs) throws SQLException {
        return new Category(rs.getLong("id"), rs.getString("name"));
    }

    @Override
    public Category findById(Long id) {
        String query = "SELECT * FROM Category WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetToCategory(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể lấy danh mục", e);
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Category";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(resultSetToCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể lấy danh sách danh mục", e);
        }
        return categories;
    }

    @Override
    public Long create(Category category) {
        String query = "INSERT INTO Category (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getName());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Thêm danh mục thất bại");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Không thể lấy ID danh mục");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể thêm danh mục", e);
        }
    }

    @Override
    public boolean update(Category category) {
        String query = "UPDATE Category SET name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.setLong(2, category.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Không thể cập nhật danh mục", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM Category WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Không thể xóa danh mục", e);
        }
    }
}
