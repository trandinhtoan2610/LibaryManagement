package BUS;

import DAL.PenaltyDAL;
import DTO.*;
import DTO.Statistics.LostBookPreciousData;
import DTO.Statistics.PenaltyDateData;
import DTO.Statistics.PenaltyTimeData;
import DTO.Statistics.PenaltyPreciousData;

import java.util.ArrayList;
import java.util.List;

public class PenaltyBUS {
    private final PenaltyDAL penaltyDAL = new PenaltyDAL();
    public static List<PenaltyDTO> penaltyList;
    private PenaltyDetailsBUS penaltyDetailsBUS;
    private ReaderBUS readerBUS;
    private BorrowSheetBUS borrowSheetBUS;
    private EmployeeBUS employeeBUS;

    public PenaltyBUS(){
        this.penaltyDetailsBUS = new PenaltyDetailsBUS();
        this.readerBUS = new ReaderBUS();
        this.borrowSheetBUS = new BorrowSheetBUS();
        this.employeeBUS = new EmployeeBUS();

        if(penaltyList == null ){
            penaltyList = new ArrayList<>();
            getPenaltyList();
        }
    }

    public void getPenaltyList(){
        try{
            penaltyList = penaltyDAL.findAll();
        }catch (Exception e){
            System.out.println("Lỗi khi lấy danh sách phiếu phạt !" + e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách phiếu phạt", e);
        }
    }

    public void addPenaltySheet(PenaltyDTO p){
//        System.out.println("add penalty sheet");
        try{
            penaltyDAL.create(p);
            penaltyList.add(p);
            System.out.println(p.getPenaltyDate());
//            System.out.println("Thêm thành công");
        }catch (Exception e){
            e.printStackTrace();            // <—
            throw new RuntimeException("Không thể thêm phiếu phạt ", e);
        }
    }

    public void updatePenaltySheet(PenaltyDTO p){
        try{
            boolean success = penaltyDAL.update(p);
            if(success){
                penaltyList.replaceAll(penaltySheet -> penaltySheet.getId().equals(p.getId()) ? p : penaltySheet);
            }

        }catch (Exception e){
            throw new RuntimeException("Không thể cập nhật phiếu phạt ", e);
        }
    }

    public void deletePenaltySheet(PenaltyDTO p){
        try{
            boolean success = penaltyDAL.delete(p);
            if(success){
                penaltyList.removeIf(penaltySheet -> penaltySheet.getId().equals(p.getId()));
            }
        }catch (Exception e){
            throw new RuntimeException("Không thể xóa phiếu phạt", e);
        }
    }

    public PenaltyDTO findPenaltySheetByID(String id){
        if(id.isEmpty()) {
            return null;
        }
        try{
            return penaltyDAL.findById(id);
        }
        catch (Exception e){
            System.out.println("Có lỗi khi tìm phiếu phạt theo ID :" + id);
            e.printStackTrace();
            return null;
        }
    }

    public String getCurrentID(){
        String maxID = "PP";
        try {
            Long maxNum = penaltyDAL.getCurrentID() == null ? 1 : penaltyDAL.getCurrentID() + 1;
            return maxID+= maxNum;

        } catch (Exception e) {
            System.out.println("Lỗi khi lấy ID : " + e.getMessage());
            return null;
        }
    }

    public ReaderDTO getPenaltyReader(PenaltyDTO p) {
        BorrowDTO b = getPenaltyBorrowSheet(p);
        return readerBUS.findReaderByID(b.getReaderId());
    }

    //Lấy phiếu mượn từ phiếu phạt :
    public BorrowDTO getPenaltyBorrowSheet(PenaltyDTO p){
        PenaltyDetailsDTO penaltyDetail = getPenaltyDetails(p).get(0);
        return borrowSheetBUS.findBorrowSheetByID(penaltyDetail.getBorrowID());
    }

    //Lấy chi tiết phạt :
    public List<PenaltyDetailsDTO> getPenaltyDetails(PenaltyDTO p){
        return penaltyDetailsBUS.getPenaltyDetailsByPenaltyID(p.getId());
    }

    public Employee getEmployeeHandlePenalty(PenaltyDTO p){
        try {
            return employeeBUS.getEmployeeById(p.getEmployeeID());
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getActiveYears(){
        try{
            return penaltyDAL.getActiveYears();
        }catch (Exception e){
            System.out.println("Lỗi BUS khi lấy các năm của phiếu phạt");
            e.printStackTrace();
        }
        return null;
    }

    public String countPenaltySheet(){
        try{
            return penaltyDAL.countPenaltySheet();
        }catch (Exception e){
            System.out.println("Lỗi BUS khi lấy sl phiếu phạt");
            e.printStackTrace();
        }
        return null;
    }

    public String countEmployeePenalty(){
        try{
            return penaltyDAL.countEmployeeHandle();
        }catch (Exception e){
            System.out.println("Lỗi BUS khi lấy sl nhân viên");
            e.printStackTrace();
        }
        return null;
    }

    public Long sumPenaltyFee(){
        try{
            return penaltyDAL.sumPenaltyFee();
        }catch (Exception e){
            System.out.println("Lỗi BUS khi lấy sl độc giả");
            e.printStackTrace();
        }
        return null;
    }

    public String countLostBook(){
        try{
            return penaltyDAL.countLostBook();
        }catch (Exception e){
            System.out.println("Lỗi BUS khi lấy sl sách tổn thất");
            e.printStackTrace();
        }
        return null;
    }

    public List<PenaltyPreciousData<Long>> getPreciousPenaltyEmployee (String year){
        try{
            return penaltyDAL.getPreciousEachEmployee(year);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<PenaltyPreciousData<String>> getPreciousPenaltyReader(String year){
        try{
            return penaltyDAL.getPreciousEachReader(year);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<LostBookPreciousData> getPreciousLostBook(String year){
        try{
            return penaltyDAL.getPreciousLostBook(year);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<PenaltyTimeData> getMonthsData(String year){
        try{
            return penaltyDAL.getMonthsData(year);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<PenaltyTimeData> getYearsData(String startYear, String endYear){
        try{
            return penaltyDAL.getYearsData(startYear, endYear);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public List<PenaltyDateData> getDatesData(String startYear, String endYear){
        try{
            return penaltyDAL.getDatesDate(startYear, endYear);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
