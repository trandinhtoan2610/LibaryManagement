package BUS;

import DAL.PublisherDAL;
import DAL.Interface.IRepositoryBase;
import DTO.Publisher;
import java.util.ArrayList;
import java.util.List;

public class PublisherBUS {
    private final IRepositoryBase<Publisher> publisherRepository;

    public PublisherBUS() {
        this.publisherRepository = new PublisherDAL();
    }

    // Lấy danh sách tất cả nhà xuất bản
    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        try {
            publishers = publisherRepository.findAll();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách nhà xuất bản: " + e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách nhà xuất bản", e);
        }
        return publishers;
    }

    // Lấy nhà xuất bản theo ID
    public Publisher getPublisherById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID nhà xuất bản không hợp lệ");
        }
        try {
            Publisher publisher = publisherRepository.findById(id);
            if (publisher == null) {
                throw new RuntimeException("Không tìm thấy nhà xuất bản với ID " + id);
            }
            return publisher;
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin nhà xuất bản với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể lấy thông tin nhà xuất bản với ID " + id, e);
        }
    }

    // Thêm nhà xuất bản mới
    public Long addPublisher(Publisher publisher) {
        validatePublisher(publisher);
        try {
            Long newPublisherId = publisherRepository.create(publisher);
            if (newPublisherId == null || newPublisherId <= 0) {
                throw new RuntimeException("Thêm nhà xuất bản thất bại");
            }
            return newPublisherId;
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm nhà xuất bản: " + e.getMessage());
            throw new RuntimeException("Không thể thêm nhà xuất bản", e);
        }
    }

    // Cập nhật thông tin nhà xuất bản
    public void updatePublisher(Publisher publisher) {
        if (publisher.getId() == null || publisher.getId() <= 0) {
            throw new IllegalArgumentException("ID nhà xuất bản không hợp lệ");
        }
        validatePublisher(publisher);
        try {
            boolean success = publisherRepository.update(publisher);
            if (!success) {
                throw new RuntimeException("Cập nhật nhà xuất bản thất bại");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật nhà xuất bản với ID " + publisher.getId() + ": " + e.getMessage());
            throw new RuntimeException("Không thể cập nhật nhà xuất bản", e);
        }
    }

    // Xóa nhà xuất bản
    public void deletePublisher(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID nhà xuất bản không hợp lệ");
        }
        try {
            boolean success = publisherRepository.delete(id);
            if (!success) {
                throw new RuntimeException("Xóa nhà xuất bản thất bại");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa nhà xuất bản với ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Không thể xóa nhà xuất bản", e);
        }
    }

    // Kiểm tra tính hợp lệ của nhà xuất bản
    private void validatePublisher(Publisher publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("Nhà xuất bản không được để trống");
        }
        if (publisher.getName() == null || publisher.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà xuất bản không được để trống");
        }
        if (publisher.getPhone() == null || publisher.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        if (publisher.getAddress() == null || publisher.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }
    }
}
