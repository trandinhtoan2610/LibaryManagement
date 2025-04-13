package BUS;

import DAL.BorrowDAL;
import DTO.BorrowDTO;

import java.util.List;

public class BorrowSheetBUS {
    private final BorrowDAL borrowSheetDAL = new BorrowDAL();
    public long createBorrowSheet(BorrowDTO dto) {
        validateBorrowData(dto);
        return borrowSheetDAL.create(dto);
    }
    public List<BorrowDTO> getAllBorrowSheet() {
        return borrowSheetDAL.findAll();
    }
    public Long addBorrowSheet(BorrowDTO dto) {
        return borrowSheetDAL.create(dto);
    }
    public boolean updateBorrowSheet(BorrowDTO dto) {
        try {
            return borrowSheetDAL.update(dto);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteBorrowSheet(Long id) {
        try {
            return borrowSheetDAL.delete(id);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void validateBorrowData(BorrowDTO dto) throws IllegalArgumentException {
        if (dto.getEmployeeId() == null || dto.getReaderId() == null) {
            throw new IllegalArgumentException("Thiếu thông tin nhân viên hoặc độc giả");
        }
    }
    public long getCurrentID(){
        return borrowSheetDAL.getCurrentID();
    }
}
