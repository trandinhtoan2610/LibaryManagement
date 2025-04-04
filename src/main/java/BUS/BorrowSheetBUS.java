package BUS;

import DAL.BorrowDAL;
import DTO.BorrowDTO;

import java.util.List;

public class BorrowSheetBUS {
    private final BorrowDAL borrowSheetDAL = new BorrowDAL();

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
    public boolean deleteBorrowSheet(BorrowDTO dto) {
        try {
            return borrowSheetDAL.delete(dto.getId());
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
