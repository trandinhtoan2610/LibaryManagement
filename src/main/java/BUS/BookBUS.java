package BUS;

import DAL.BookDAL;
import DTO.AuthorDTO;
import DTO.Book;
import DTO.BookViewModel;
import DTO.Category;
import DTO.PublisherDTO;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BookBUS {
    private static final Logger LOGGER = Logger.getLogger(BookBUS.class.getName());
    private final BookDAL bookRepository;
    private final CategoryBUS categoryBUS;
    private final AuthorBUS authorBUS;
    private final PublisherBUS publisherBUS;
    public static final List<BookViewModel> bookViewModels = new ArrayList<>();

    public BookBUS() {
        this.bookRepository = new BookDAL();
        this.categoryBUS = new CategoryBUS();
        this.authorBUS = new AuthorBUS();
        this.publisherBUS = new PublisherBUS();
        refreshBooks();
    }

    public int getCountBook() {
        return bookViewModels.size();
    }

    private void refreshBooks() {
        if (!bookViewModels.isEmpty()) {
            return;
        }
        bookViewModels.clear();
        try {
            List<Book> books = bookRepository.findAll();
            if (books == null) {
                throw new RuntimeException("Không thể lấy danh sách sách từ cơ sở dữ liệu");
            }
            for (Book book : books) {
                bookViewModels.add(convertToBookViewModel(book));
            }
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi làm mới danh sách sách: " + e.getMessage());
            throw new RuntimeException("Không thể làm mới danh sách sách", e);
        }
    }

    public List<BookViewModel> getAllBooksForDisplay() {
        refreshBooks();
        return new ArrayList<>(bookViewModels);
    }

    public List<BookViewModel> searchBooks(String type, String keyword) {
        refreshBooks();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooksForDisplay();
        }
        try {
            return switch (type) {
                case "Tên" -> bookViewModels.stream()
                        .filter(book -> book.getName().toLowerCase().contains(keyword.toLowerCase()))
                        .collect(Collectors.toList());
                case "Thể Loại" -> bookViewModels.stream()
                        .filter(book -> book.getCategoryName().toLowerCase().contains(keyword.toLowerCase()))
                        .collect(Collectors.toList());
                case "Tác Giả" -> bookViewModels.stream()
                        .filter(book -> book.getAuthorName().toLowerCase().contains(keyword.toLowerCase()))
                        .collect(Collectors.toList());
                case "Năm" -> {
                    int year = Integer.parseInt(keyword);
                    yield bookViewModels.stream()
                            .filter(book -> book.getYearOfPublication() != null && book.getYearOfPublication().getValue() == year)
                            .collect(Collectors.toList());
                }
                default -> throw new IllegalArgumentException("Tiêu chí tìm kiếm không hợp lệ: " + type);
            };
        } catch (NumberFormatException e) {
            LOGGER.warning("Từ khóa tìm kiếm năm không hợp lệ: " + keyword);
            throw new IllegalArgumentException("Từ khóa tìm kiếm năm phải là một số hợp lệ", e);
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi tìm kiếm sách với tiêu chí " + type + " và từ khóa " + keyword + ": " + e.getMessage());
            throw new RuntimeException("Không thể thực hiện tìm kiếm sách", e);
        }
    }

    public BookViewModel getBookByIdForDisplay(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        refreshBooks();
        return bookViewModels.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID " + id));
    }

    public List<Book> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();
            if (books == null) {
                throw new RuntimeException("Không thể lấy danh sách sách từ cơ sở dữ liệu");
            }
            return books;
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi lấy danh sách tất cả sách: " + e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách tất cả sách", e);
        }
    }

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
            LOGGER.severe("Lỗi khi lấy thông tin sách với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể lấy thông tin sách với ID " + id, e);
        }
    }

    private BookViewModel convertToBookViewModel(Book book) {
        BookViewModel viewModel = new BookViewModel();
        viewModel.setId(book.getId());
        viewModel.setName(book.getName());
        try {
            viewModel.setCategoryName(categoryBUS.getCategoryById(book.getCategoryId()).getName());
        } catch (Exception e) {
            viewModel.setCategoryName("Không xác định");
            LOGGER.warning("Không lấy được tên danh mục cho sách ID " + book.getId() + ": " + e.getMessage());
        }
        try {
            viewModel.setAuthorName(authorBUS.getAuthorNameById(book.getAuthorId()));
        } catch (Exception e) {
            viewModel.setAuthorName("Không xác định");
            LOGGER.warning("Không lấy được tên tác giả cho sách ID " + book.getId() + ": " + e.getMessage());
        }
        try {
            viewModel.setPublisherName(publisherBUS.getPublisherById(book.getPublisherId()).getName());
        } catch (Exception e) {
            viewModel.setPublisherName("Không xác định");
            LOGGER.warning("Không lấy được tên nhà xuất bản cho sách ID " + book.getId() + ": " + e.getMessage());
        }
        viewModel.setQuantity(book.getQuantity());
        viewModel.setBorrowedQuantity(book.getBorrowedQuantity());
        viewModel.setUnitPrice(book.getUnitPrice());
        viewModel.setYearOfPublication(book.getYearOfPublication());
        return viewModel;
    }

    public Long addBook(Book book) {
        validateBook(book);
        try {
            Long newBookId = bookRepository.create(book);
            if (newBookId == null || newBookId <= 0) {
                throw new RuntimeException("Thêm sách thất bại");
            }
            book.setId(newBookId);
            bookViewModels.add(convertToBookViewModel(book));
            return newBookId;
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi thêm sách: " + e.getMessage());
            throw new RuntimeException("Không thể thêm sách", e);
        }
    }

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
            BookViewModel updatedViewModel = convertToBookViewModel(book);
            bookViewModels.replaceAll(p -> p.getId().equals(book.getId()) ? updatedViewModel : p);
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi cập nhật sách với ID " + book.getId() + ": " + e.getMessage());
            throw new RuntimeException("Không thể cập nhật sách", e);
        }
    }

    public void deleteBook(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        try {
            boolean success = bookRepository.delete(id);
            if (!success) {
                throw new RuntimeException("Xóa sách thất bại");
            }
            bookViewModels.removeIf(book -> book.getId().equals(id));
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi xóa sách với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể xóa sách", e);
        }
    }

    public List<BookViewModel> getBooksByAuthorID(Long authorID) {
        if (authorID == null || authorID <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        refreshBooks();
        return bookViewModels.stream()
                .filter(book -> {
                    Book b = bookRepository.findById(book.getId());
                    return b != null && b.getAuthorId().equals(authorID);
                })
                .collect(Collectors.toList());
    }

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