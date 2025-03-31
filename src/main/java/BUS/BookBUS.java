package BUS;

import DAL.BookDAL;
import DAL.Interface.IRepositoryBase;
import DTO.Book;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class BookBUS {
    private final IRepositoryBase<Book> bookRepository;

    public BookBUS() {
        this.bookRepository = new BookDAL();
    }

    // Lấy danh sách tất cả sách
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            books = bookRepository.findAll();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách tất cả sách: " + e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách tất cả sách", e);
        }
        return books;
    }

    // Lấy sách theo ID
    public Book getBookById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        try {
            Book book = bookRepository.findById(id);
            if (book == null) {
                throw new RuntimeException("Không tìm thấy sách với ID " + id);
            }
            return book;
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin sách với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể lấy thông tin sách với ID " + id, e);
        }
    }

    // Thêm sách mới và trả về ID của sách vừa thêm
    public Long addBook(Book book) {
        validateBook(book);
        try {
            Long newBookId = bookRepository.create(book);
            if (newBookId == null || newBookId <= 0) {
                throw new RuntimeException("Thêm sách thất bại");
            }
            return newBookId;
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm sách: " + e.getMessage());
            throw new RuntimeException("Không thể thêm sách", e);
        }
    }

    // Cập nhật thông tin sách
    public void updateBook(Book book) {
        if (book.getId() == null || book.getId() <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        validateBook(book);
        try {
            boolean success = bookRepository.update(book);
            if (!success) {
                throw new RuntimeException("Cập nhật sách thất bại");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật sách với ID " + book.getId() + ": " + e.getMessage());
            throw new RuntimeException("Không thể cập nhật sách", e);
        }
    }

    // Xóa sách
    public void deleteBook(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        try {
            boolean success = bookRepository.delete(id);
            if (!success) {
                throw new RuntimeException("Xóa sách thất bại");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa sách với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể xóa sách", e);
        }
    }

    // Kiểm tra tính hợp lệ của sách
    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Sách không được để trống");
        }
        if (book.getName() == null || book.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sách không được để trống");
        }
        if (book.getQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        if (book.getUnitPrice() == null || book.getUnitPrice() < 0) {
            throw new IllegalArgumentException("Đơn giá không được âm");
        }
        if (book.getYearOfPublication() != null && book.getYearOfPublication().isAfter(Year.now())) {
            throw new IllegalArgumentException("Năm xuất bản không được lớn hơn năm hiện tại");
        }
        if (book.getCategoryId() == null || book.getCategoryId() <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        if (book.getAuthorId() == null || book.getAuthorId() <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        if (book.getPublisherId() == null || book.getPublisherId() <= 0) {
            throw new IllegalArgumentException("ID nhà xuất bản không hợp lệ");
        }
    }
}