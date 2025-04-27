package BUS;

import DAL.BookDAL;
import DAL.Interface.IRepositoryBase;
import DTO.Book;
import DTO.BookViewModel;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookBUS {
    private final BookDAL bookRepository;
    private final CategoryBUS categoryBUS;
    private final AuthorBUS authorBUS;
    private final PublisherBUS publisherBUS;
    public static List<BookViewModel> bookViewModels; // Danh sách nội bộ để lưu trữ

    public BookBUS() {
        this.bookRepository = new BookDAL();
        this.categoryBUS = new CategoryBUS();
        this.authorBUS = new AuthorBUS();
        this.publisherBUS = new PublisherBUS();
        this.bookViewModels = new ArrayList<>();
        refreshBooks();
    }

    public int getCountBook() {
        return bookViewModels.size();
    }
    // tải lại danh sách từ csdl
    private void refreshBooks() {
        if (!bookViewModels.isEmpty()) {
            return;
        }
        bookViewModels.clear();
        try {
            List<Book> books = bookRepository.findAll();
            for (Book book : books) {
                BookViewModel viewModel = new BookViewModel();
                viewModel.setId(book.getId());
                viewModel.setName(book.getName());
                viewModel.setCategoryName(categoryBUS.getCategoryById(book.getCategoryId()).getName());
                viewModel.setAuthorName(authorBUS.getAuthorNameById(book.getAuthorId()));
                viewModel.setPublisherName(publisherBUS.getPublisherById(book.getPublisherId()).getName());
                viewModel.setQuantity(book.getQuantity());
                viewModel.setBorrowedQuantity(book.getBorrowedQuantity());
                viewModel.setUnitPrice(book.getUnitPrice());
                viewModel.setYearOfPublication(book.getYearOfPublication());
                bookViewModels.add(viewModel);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi làm mới danh sách sách: " + e.getMessage());
            throw new RuntimeException("Không thể làm mới danh sách sách", e);
        }
    }

    // Lấy danh sách tất cả sách dưới dạng BookViewModel từ danh sách nội bộ
    public List<BookViewModel> getAllBooksForDisplay() {
        refreshBooks();
        return new ArrayList<>(bookViewModels);
    }

    // Tìm kiếm sách theo tiêu chí và từ khóa trên danh sách nội bộ
    public List<BookViewModel> searchBooks(String type, String keyword) {
        refreshBooks();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooksForDisplay();
        }

        List<BookViewModel> filteredBooks = new ArrayList<>();

        try {
            switch (type) {
                case "Tên":
                    filteredBooks = bookViewModels.stream()
                            .filter(book -> book.getName().toLowerCase().contains(keyword.toLowerCase()))
                            .collect(Collectors.toList());
                    break;
                case "Thể Loại":
                    filteredBooks = bookViewModels.stream()
                            .filter(book -> book.getCategoryName().toLowerCase().contains(keyword.toLowerCase()))
                            .collect(Collectors.toList());
                    break;
                case "Tác Giả":
                    filteredBooks = bookViewModels.stream()
                            .filter(book -> book.getAuthorName().toLowerCase().contains(keyword.toLowerCase()))
                            .collect(Collectors.toList());
                    break;
                case "Năm":
                    int year = Integer.parseInt(keyword);
                    filteredBooks = bookViewModels.stream()
                            .filter(book -> book.getYearOfPublication() != null && book.getYearOfPublication().getValue() == year)
                            .collect(Collectors.toList());
                    break;
                default:
                    throw new IllegalArgumentException("Tiêu chí tìm kiếm không hợp lệ: " + type);
            }
        } catch (NumberFormatException e) {
            System.err.println("Lỗi khi chuyển đổi năm tìm kiếm: " + keyword + ". Vui lòng nhập một số hợp lệ.");
            throw new IllegalArgumentException("Từ khóa tìm kiếm năm phải là một số hợp lệ", e);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm kiếm sách với tiêu chí " + type + " và từ khóa " + keyword + ": " + e.getMessage());
            throw new RuntimeException("Không thể thực hiện tìm kiếm sách", e);
        }

        return filteredBooks;
    }

    // Lấy sách theo ID (dạng BookViewModel) từ danh sách nội bộ
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

    // Lấy danh sách tất cả sách dạng Book từ cơ sở dữ liệu
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

    // Lấy sách theo ID (dạng Book) từ cơ sở dữ liệu
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

    // Chuyển Book thành BookViewModel để thêm hoặc cập nhật danh sách bookviewmodals
    private BookViewModel convertToBookViewModel(Book book) {
        BookViewModel viewModel = new BookViewModel();
        viewModel.setId(book.getId());
        viewModel.setName(book.getName());
        viewModel.setCategoryName(categoryBUS.getCategoryById(book.getCategoryId()).getName());
        viewModel.setAuthorName(authorBUS.getAuthorNameById(book.getAuthorId()));
        viewModel.setPublisherName(publisherBUS.getPublisherById(book.getPublisherId()).getName());
        viewModel.setQuantity(book.getQuantity());
        viewModel.setBorrowedQuantity(book.getBorrowedQuantity());
        viewModel.setUnitPrice(book.getUnitPrice());
        viewModel.setYearOfPublication(book.getYearOfPublication());
        return viewModel;
    }

    // Thêm sách mới
    public Long addBook(Book book) {
        validateBook(book);
        try {
            Long newBookId = bookRepository.create(book);
            if (newBookId == null || newBookId <= 0) {
                throw new RuntimeException("Thêm sách thất bại");
            }

            book.setId(newBookId);

            BookViewModel newBookViewModel = convertToBookViewModel(book);
            bookViewModels.add(newBookViewModel);
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

            BookViewModel updatedBookViewModel = convertToBookViewModel(book);
            for (int i = 0; i < bookViewModels.size(); i++) {
                if (bookViewModels.get(i).getId().equals(book.getId())) {
                    bookViewModels.set(i, updatedBookViewModel);
                    break;
                }
            }
            bookViewModels.replaceAll(p -> p.getId() == book.getId() ? updatedBookViewModel : p);
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

            bookViewModels.removeIf(book -> book.getId().equals(id));
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa sách với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể xóa sách", e);
        }
    }

    // Tìm sách theo AuthorID
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
    // xin dung sua cua minh
    public int sumBorrowedBook(){
        return bookRepository.sumBorrowedBook();
    }
    public int sumAvailableBook(){
        return bookRepository.sumAvailableBook();
    }
}