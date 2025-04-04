package DAL;

import DAL.Interface.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDAL {
    private final DatabaseConnection dbConnection;

    public GenericDAL() {
        this.dbConnection = new DatabaseConnection();
//        this.dbConnection = DatabaseConnection.getInstance();
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... params) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rowMapper.mapRow(rs) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi truy vấn dữ liệu", e);
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(rowMapper.mapRow(rs));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn danh sách", e);
        }
        return results;
    }

    public Boolean update(String sql, Object... params) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật dữ liệu", e);
        }
    }

    public Long insert(String sql, Object... params) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm mới dữ liệu", e);
        }
    }
    public Boolean delete(String sql, Object... params) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa dữ liệu", e);
        }
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    public long getMaxID(String sql) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                long maxId = rs.getLong(1);
                return rs.wasNull() ? 0 : maxId;
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy ID từ cơ sở dữ liệu", e);
        }
    }
}
