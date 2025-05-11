package BUS;

import DAL.BorrowDAL;
import DTO.BorrowDTO;
import DTO.Statistics.MonthData;
import DTO.Statistics.QuarterData;
import DTO.Statistics.QuarterDataStringId;
import DTO.Statistics.StatusData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowSheetBUS {
    private final BorrowDAL borrowSheetDAL = new BorrowDAL();
    public static List<BorrowDTO> borrowSheetList = new ArrayList<>();
    public BorrowSheetBUS() {
        if (borrowSheetList.isEmpty()) {
            borrowSheetList = borrowSheetDAL.findAll();
        }
    }
    public int getCountBorrowSheet(){
        return borrowSheetList.size();
    }
    public Long addBorrowSheet(BorrowDTO dto) {
        Long newId = borrowSheetDAL.create(dto);
        if (newId != null) {
            dto.setId(newId);
            borrowSheetList.add(dto);
        }
        return newId;
    }
    public boolean updateBorrowSheet(BorrowDTO dto) {
        if (borrowSheetDAL.update(dto)) {
            for (int i = 0; i < borrowSheetList.size(); i++) {
                if (borrowSheetList.get(i).getId().equals(dto.getId())) {
                    borrowSheetList.set(i, dto);
                    break;
                }
            }
            return true;
        }
        return false;
    }
    public boolean deleteBorrowSheet(Long id) {
        if (borrowSheetDAL.delete(id)) {
            borrowSheetList.removeIf(borrow -> borrow.getId().equals(id));
            return true;
        }
        return false;
    }

    private void validateBorrowData(BorrowDTO dto) throws IllegalArgumentException {
        if (dto.getEmployeeId() == null || dto.getReaderId() == null) {
            throw new IllegalArgumentException("Thiếu thông tin nhân viên hoặc độc giả");
        }
    }
    public long getCurrentID(){
        return borrowSheetDAL.getCurrentID();
    }

    public BorrowDTO findBorrowSheetByID(Long id){
        return borrowSheetDAL.findById(id);
    }

    public List<Integer> getListYear() {
        return borrowSheetDAL.getListYear();
    }
    public List<QuarterData> getQuarterEmloyeeData(int year){
        return borrowSheetDAL.getQuarterEmployeeData(year);
    }
    public List<QuarterDataStringId> getQuarterReaderData(int year){
        return borrowSheetDAL.getQuarterReaderData(year);
    }
    public List<QuarterData> getQuarterBookData(int year){
        return borrowSheetDAL.getQuarterBookData(year);
    }
    public List<QuarterData> getQuarterBookDataByDate(Date startDate, Date endDate) {
        return borrowSheetDAL.getQuarterBookDataByDate(startDate, endDate);
    }
    public List<QuarterDataStringId> getQuarterReaderDataByDate(Date startDate, Date endDate) {
        return borrowSheetDAL.getQuarterReaderDataByDate(startDate, endDate);
    }
    public List<QuarterData> getQuarterEmployeeDataByDate(Date startDate, Date endDate) {
        return borrowSheetDAL.getQuarterEmployeeDataByDate(startDate, endDate);
    }
    public List<MonthData> getMonthDataByYear(int year){
        return borrowSheetDAL.getListMonthForYear(year);
    }
    public List<MonthData> getMonthDataByDate(Date startDate, Date endDate) {
        return borrowSheetDAL.getListMonthByDate(startDate, endDate);
    }
    public List<StatusData> getStatusDataByYear(int Year){
        return borrowSheetDAL.getListStatusByYear(Year);
    }
    public List<StatusData> getStatusDataByDate(Date startDate, Date endDate) {
        return borrowSheetDAL.getListStatusByDate(startDate, endDate);
    }
}
