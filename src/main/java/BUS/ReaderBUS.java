package BUS;

import DAL.ReaderDAL;
import DTO.AuthorDTO;
import DTO.ReaderDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReaderBUS {
    ReaderDAL readerDAL;
    public static List<ReaderDTO> readerList;

    public ReaderBUS(){
        readerDAL = new ReaderDAL();
        if(readerList == null ){
            readerList = new ArrayList<>();
            readerList = readerDAL.findAll();
        }

    }

    public int getCountReader(){
        return readerList.size();
    }

    public void addReader(ReaderDTO reader){
        try {
            readerDAL.create(reader);
            readerList.add(reader);
        }catch(Exception e){
            System.out.println("Lỗi khi thêm độc giả : " + e.getMessage());
            
        }
    }

    public void deleteReader(ReaderDTO reader) {
        try {
            boolean success = readerDAL.delete(reader.getId()); // Xóa khỏi DB
            if (success) {
                readerList.removeIf(r -> r.getId().equals(reader.getId())); // Xóa khỏi danh sách 
            }
        }catch(Exception e){
            System.out.println("Lỗi khi xóa độc giả : " + e.getMessage());
        }
    }

    public void updateReader(ReaderDTO reader){
        try {
            boolean success = readerDAL.update(reader);
            if (success){
                readerList.replaceAll(r -> r.getId().equals(reader.getId()) ? reader : r);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật độc giả : " + e.getMessage());
        }
    }

    public ReaderDTO findReaderByID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID độc giả không được để trống");
        }
        try {
            return readerDAL.findById(id);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm độc giả theo ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public String getCurrentID(){
        String maxID = "RD";
        try {
            Long maxNum = readerDAL.getCurrentID() + 1;
            return maxID + maxNum;

        } catch (Exception e) {
            System.out.println("Lỗi khi lấy ID : " + e.getMessage());
            return null;
        }
    }

    public boolean exportToExcel(String filePath) {
        try {
            // Tạo workbook mới
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách độc giả");

            // Tạo header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Họ", "Tên ", "Giới tính", "Số điện thoại", "Địa chỉ", "Uy tín"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (ReaderDTO a : readerList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(a.getId());
                row.createCell(1).setCellValue(a.getLastName());
                row.createCell(2).setCellValue(a.getFirstName());
                row.createCell(3).setCellValue(a.getGender().toString());
                row.createCell(4).setCellValue(a.getPhone());
                row.createCell(5).setCellValue(a.getAddress());
                row.createCell(6).setCellValue(a.getComplianceCount());
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

    public ReaderDTO findAllStatusReaderByID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID độc giả không được để trống");
        }
        try {
            return readerDAL.findBothStatusById(id);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm độc giả theo ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}