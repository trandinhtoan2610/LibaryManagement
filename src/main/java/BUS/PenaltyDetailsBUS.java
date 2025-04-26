package BUS;

import DAL.PenaltyDetailsDAL;
import DTO.*;
import DTO.Enum.Status;
import DTO.Enum.SubStatus;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PenaltyDetailsBUS {
    private final PenaltyDetailsDAL penaltyDetailsDAL = new PenaltyDetailsDAL();
    public static List<PenaltyDetailsDTO> penaltyDetailsList;
    private BookBUS bookBUS;
    private BorrowDetailBUS borrowDetailBUS = new BorrowDetailBUS();

    public PenaltyDetailsBUS(){
        bookBUS = new BookBUS();
        if(penaltyDetailsList == null){
            getPenaltyDetailsList();
        }
    }

    public void getPenaltyDetailsList(){
        try{
            penaltyDetailsList = penaltyDetailsDAL.findAll();
        }catch (Exception e){
            throw new RuntimeException("Lỗi khi lấy danh sách chi tiết phiếu phạt !", e);
        }
    }


    public void addPenaltyDetails(String penaltySheetID, BorrowDTO borrowSheet){
        if(!borrowSheet.getStatus().equals(Status.Phạt))
            return;

        try {
            List<BorrowDetailDTO> borrowDetailList = borrowDetailBUS.getBorrowDetailsBySheetId(borrowSheet.getLongID());
            for(BorrowDetailDTO b : borrowDetailList){
                if(b.getStatus().equals(SubStatus.Đã_Trả) || b.getStatus().equals(SubStatus.Đang_Mượn))
                    continue;

                String violation="";
                Long punishFee = 0L;
                Book bookInPenalty = bookBUS.getBookById(b.getBookId());
                switch (b.getStatus()){

                    case SubStatus.Mất_Sách ->{
                        violation += "Mất sách";
                        punishFee = bookInPenalty.getUnitPrice(); // Lấy giá tiền của sách
                    }

                    case SubStatus.Hư_Sách -> {
                        violation += "Hư sách";
                        punishFee = bookInPenalty.getUnitPrice()/2; // Trả 50% tiền sách
                    }

                    case SubStatus.Quá_Hạn -> {
                        Date dueDate = borrowSheet.getDuedate();
                        Date returnDate = b.getActualReturnDate();
                        long lateDays = countDaysBetween(returnDate, dueDate);
                        violation = "Trả sách trễ " + lateDays + " ngày";
                        punishFee = 5000*lateDays*b.getQuantity(); // Trễ 1 ngày -> 5k =)))))
                    }
                }

                PenaltyDetailsDTO p = new PenaltyDetailsDTO(
                        penaltySheetID,
                        borrowSheet.getLongID(),
                        bookInPenalty.getId(),
                        b.getQuantity(),
                        violation,
                        punishFee
                );
                    penaltyDetailsDAL.create(p);
                    penaltyDetailsList.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm chi tiết phạt");
        }
    }

    //Hàm này tính số ngày giữa 2 ngày
    public long countDaysBetween(Date returnDate, Date dueDate){
        long diffInMillies = Math.abs(returnDate.getTime() - dueDate.getTime());
        long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return days;

    }

    // Xóa theo id phiếu phạt
    public void deletePenaltyDetailsByPenaltyID(String penaltyID){
        if(penaltyID == null)
            throw new RuntimeException("ID phiếu phạt truyền vào bị null !");

        try{
            Iterator<PenaltyDetailsDTO> iterator = penaltyDetailsList.iterator();
            while (iterator.hasNext()){
                PenaltyDetailsDTO p = iterator.next();
                if(p.getPenaltyID().equals(penaltyID)){
                    iterator.remove();
                    penaltyDetailsDAL.delete(p);
                }
            }
        }catch(Exception e){
            throw new RuntimeException("Lỗi khi xóa chi tiết phiếu phạt", e);
        }
    }


    // Lấy các chi tiết phiếu phạt theo id phiếu phạt
    public List<PenaltyDetailsDTO> getPenaltyDetailsByPenaltyID(String penaltyID){
        return penaltyDetailsDAL.findByPenaltyID(penaltyID);
    }


    public Long getTotalFee(String penaltyID){
        Long totalFee = 0L;
        for(PenaltyDetailsDTO p : penaltyDetailsList){
            if(p.getPenaltyID().equals(penaltyID))
                totalFee += p.getPunishFee();
        }

        return totalFee;
    }

    public String getPenaltyIDByBorrowSheet(BorrowDTO borrowDTO){
        String penaltyID = "";
        for (PenaltyDetailsDTO p : penaltyDetailsList){
            if(p.getBorrowID() == borrowDTO.getLongID()){
                penaltyID = p.getPenaltyID();
                break;
            }
        }
        return penaltyID;
    }
}