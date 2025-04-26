package BUS;

import static BUS.PublisherBUS.publisherList;
import DAL.AuthorDAL;
import DAL.Interface.IRepositoryBase;
import DTO.AuthorDTO;
import DTO.PublisherDTO;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AuthorBUS {
    private final AuthorDAL authorRepository;
    public static List<AuthorDTO> authorDTOList;

    public AuthorBUS() {
        this.authorRepository = new AuthorDAL();
        if (authorDTOList == null) {
            authorDTOList = new ArrayList<>();
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
    public void deleteAuthor(Long authorID) {
        if (authorID <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        try {
            boolean success = authorRepository.delete(authorID);
            if (!success) {
                throw new RuntimeException("Xóa tác giả thất bại");
            }
            // Xóa khỏi danh sách tĩnh
            authorDTOList.removeIf(a -> a.getId() == authorID);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa tác giả với ID " + authorID + ": " + e.getMessage());
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


    public void deleteUnusedAuthor() {
        Iterator<AuthorDTO> iterator = authorDTOList.iterator();
        while (iterator.hasNext()) {
            AuthorDTO author = iterator.next();
            if (author.getProductQuantity() == 0) {
                iterator.remove(); // an toàn khi remove trong khi duyệt
                authorRepository.delete(author.getId());
            }
        }
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
    
    
    public boolean exportToExcel(String filePath) {
        try {
            // Tạo workbook mới
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách nhà xuất bản");

            // Tạo header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Tên tác giả", "Số lượng tác phẩm"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (AuthorDTO a : authorDTOList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(a.getId());
                row.createCell(1).setCellValue(a.getName());
                row.createCell(2).setCellValue(a.getProductQuantity());
            }

            // Tự động điều chỉnh độ rộng cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                return true;
            } finally {
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}