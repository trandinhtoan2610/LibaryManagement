package BUS;

import DAL.BorrowDetailDAL;
import DTO.BorrowDetailDTO;

import java.util.List;

public class BorrowDetailBUS {
    private final BorrowDetailDAL borrowDetailDAL = new BorrowDetailDAL();

    public List<BorrowDetailDTO> getAllBorrowDetails(){ return borrowDetailDAL.findAll(); }
    public Long addBorrowDetail(BorrowDetailDTO  borrowDetailDTO) {
        return borrowDetailDAL.create(borrowDetailDTO);
    }
    public boolean updateBorrowDetail(BorrowDetailDTO borrowDetailDTO) {
        try {
            return borrowDetailDAL.update(borrowDetailDTO);
        }catch(Exception e) {
            System.out.println("Không cập nhật chi tiết phiếu mượn: " + e.getMessage());
            return false;
        }
    }
    public boolean deleteBorrowDetail(BorrowDetailDTO  borrowDetailDTO) {
        try {
            return borrowDetailDAL.delete(borrowDetailDTO);
        } catch (Exception e) {
            System.out.println("Không xóa chi tiết phiếu mượn: " + e.getMessage());
            return false;
        }
    }
    public List<BorrowDetailDTO> getBorrowDetailsBySheetId(Long borrowSheetId) {
        return borrowDetailDAL.findByBorrowSheetId(borrowSheetId);
    }

}
