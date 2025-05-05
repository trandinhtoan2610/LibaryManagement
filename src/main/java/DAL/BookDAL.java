package DAL;

import DAL.Interface.IRepositoryBase;
import DTO.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class BookDAL implements IRepositoryBase<Book> {

    // Chuyển đổi ResultSet thành đối tượng Book
    private Book ResultSettoBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setName(rs.getString("name"));
        book.setCategoryId(rs.getLong("categoryId"));
        book.setAuthorId(rs.getLong("authorId"));
        book.setPublisherId(rs.getLong("publisherId"));
        book.setQuantity(rs.getInt("quantity"));
        book.setBorrowedQuantity(rs.getInt("borrowedQuantity"));
        book.setUnitPrice(rs.getLong("unitprice"));
        int year = rs.getInt("yearOfpublication");
        book.setYearOfPublication(rs.wasNull() ? null : Year.of(year));
        return book;
    }

    @Override
    public Book findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        String query = "SELECT * FROM Book WHERE id = ? AND isActive = 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return ResultSettoBook(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy thông tin sách với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể lấy thông tin sách với ID " + id, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return null;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM Book WHERE isActive = 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = ResultSettoBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách tất cả sách: " + e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách tất cả sách", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return books;
    }

    @Override
    public Long create(Book book) {
        String query = "INSERT INTO Book (name, categoryId, authorId, publisherId, quantity, borrowedQuantity, unitprice, yearOfpublication) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, book.getName());
            stmt.setLong(2, book.getCategoryId());
            stmt.setLong(3, book.getAuthorId());
            stmt.setLong(4, book.getPublisherId());
            stmt.setInt(5, book.getQuantity());
            stmt.setInt(6, book.getBorrowedQuantity());
            stmt.setLong(7, book.getUnitPrice());
            stmt.setObject(8, book.getYearOfPublication() != null ? book.getYearOfPublication().getValue() : null);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Thêm sách thất bại, không có hàng nào được thêm");
            }
            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1); // Trả về ID của sách vừa thêm
            } else {
                throw new SQLException("Thêm sách thất bại, không lấy được ID");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm sách: " + e.getMessage());
            throw new RuntimeException("Không thể thêm sách", e);
        } finally {
            closeResources(generatedKeys, stmt, conn);
        }
    }

    @Override
    public boolean update(Book book) {
        String query = "UPDATE Book SET name = ?, categoryId = ?, authorId = ?, publisherId = ?, quantity = ?,borrowedQuantity = ?, unitprice = ?, yearOfpublication = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, book.getName());
            stmt.setLong(2, book.getCategoryId());
            stmt.setLong(3, book.getAuthorId());
            stmt.setLong(4, book.getPublisherId());
            stmt.setInt(5, book.getQuantity());
            stmt.setInt(6, book.getBorrowedQuantity());
            stmt.setLong(7, book.getUnitPrice());
            stmt.setObject(8, book.getYearOfPublication() != null ? book.getYearOfPublication().getValue() : null);
            stmt.setLong(9, book.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật sách với ID " + book.getId() + ": " + e.getMessage());
            throw new RuntimeException("Không thể cập nhật sách", e);
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public boolean delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        String query = "UPDATE Book SET isActive = 0 WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa sách với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể xóa sách", e);
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    // Đóng tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng PreparedStatement: " + e.getMessage());
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng Connection: " + e.getMessage());
        }
    }

    public List<Book> findBooksByAuthorID(Long authorID){
        String sql = "SELECT * FROM book WHERE authorID = ?";
        List<Book> list = new ArrayList<>();
        try (
            Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql); )
        {
            pst.setLong(1,authorID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                list.add(ResultSettoBook(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("ERROR when query books by authour :D ");
        }
        return list;
    }
}