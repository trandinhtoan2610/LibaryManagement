package BUS;

import DAL.AuthorDAL;
import DAL.Interface.IRepositoryBase;
import DTO.AuthorDTO;

import java.util.ArrayList;
import java.util.List;

public class AuthorBUS {
    private final AuthorDAL authorRepository;
    public static List<AuthorDTO> authorDTOList;

    public AuthorBUS() {
        this.authorRepository = new AuthorDAL();
        authorDTOList = new ArrayList<>();
        if (authorDTOList.size() == 0) {
            getAuthorList();
        }


    }

    // Load danh sách tác giả từ DAL vào authorDTOList
    public void getAuthorList() {
        try {
            authorDTOList = authorRepository.findAll();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách tác giả: " + e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách tác giả", e);
        }
    }

    // Lấy danh sách tất cả tác giả
    public List<AuthorDTO> getAllAuthors() {
        return new ArrayList<>(authorDTOList);
    }

    // Lấy tác giả theo ID
    public AuthorDTO getAuthorById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        try {
            AuthorDTO author = authorRepository.findById(id);
            if (author == null) {
                throw new RuntimeException("Không tìm thấy tác giả với ID " + id);
            }
            return author;
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin tác giả với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể lấy thông tin tác giả với ID " + id, e);
        }
    }

    // Thêm tác giả mới
    public Long addAuthor(AuthorDTO author) {
        validateAuthor(author);
        try {
            Long newAuthorId = authorRepository.create(author);
            if (newAuthorId == null || newAuthorId <= 0) {
                throw new RuntimeException("Thêm tác giả thất bại");
            }
            author.setId(newAuthorId); // Cập nhật ID cho author
            authorDTOList.add(author); // Thêm vào danh sách tĩnh
            return newAuthorId;
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm tác giả: " + e.getMessage());
            throw new RuntimeException("Không thể thêm tác giả", e);
        }
    }

    // Cập nhật thông tin tác giả
    public void updateAuthor(AuthorDTO author) {
        if (author.getId() <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        validateAuthor(author);
        try {
            boolean success = authorRepository.update(author);
            if (!success) {
                throw new RuntimeException("Cập nhật tác giả thất bại");
            }
            // Cập nhật trong danh sách tĩnh
            authorDTOList.replaceAll(a -> a.getId() == author.getId() ? author : a);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật tác giả với ID " + author.getId() + ": " + e.getMessage());
            throw new RuntimeException("Không thể cập nhật tác giả", e);
        }
    }

    // Xóa tác giả
    public void deleteAuthor(AuthorDTO author) {
        if (author.getId() <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        try {
            boolean success = authorRepository.delete(author.getId());
            if (!success) {
                throw new RuntimeException("Xóa tác giả thất bại");
            }
            // Xóa khỏi danh sách tĩnh
            authorDTOList.removeIf(a -> a.getId() == author.getId());
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa tác giả với ID " + author.getId() + ": " + e.getMessage());
            throw new RuntimeException("Không thể xóa tác giả", e);
        }
    }

    // Tìm tác giả theo ID
    public AuthorDTO findAuthorByID(AuthorDTO author) {
        if (author.getId() <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        try {
            return authorRepository.findById(author.getId());
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm tác giả với ID " + author.getId() + ": " + e.getMessage());
            throw new RuntimeException("Không thể tìm tác giả", e);
        }
    }

    // Lấy tên tác giả theo ID (dùng cho BookViewModel)
    public String getAuthorNameById(Long id) {
        AuthorDTO author = getAuthorById(id);
        return author.getName();
    }

    public Long getAuthorMaxID(){
        return authorRepository.getMaxID();
    }

    // Kiểm tra tính hợp lệ của tác giả
    private void validateAuthor(AuthorDTO author) {
        if (author == null) {
            throw new IllegalArgumentException("Tác giả không được để trống");
        }
        if (author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tác giả không được để trống");
        }
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tác giả không được để trống");
        }
        if (author.getProductQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng sản phẩm không được âm");
        }
    }
}