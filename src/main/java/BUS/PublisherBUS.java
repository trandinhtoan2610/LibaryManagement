package BUS;


import DAL.PublisherDAL;
import DTO.Employee;
import DTO.PublisherDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PublisherBUS {
    private final PublisherDAL publisherDAL = new PublisherDAL();
    public static List<PublisherDTO> publisherList = new ArrayList<>();

    public PublisherBUS(){
        if(publisherList.isEmpty()) {
            publisherList = publisherDAL.findAll();
        }
    }
    public List<PublisherDTO> getAllPublishers() {
        return publisherDAL.findAll();
    }
    public PublisherDTO getPublisherById(long id) {
        return publisherDAL.findById(id);
    }
    public long addPublisher(PublisherDTO publisherDTO) {
        return publisherDAL.create(publisherDTO);
    }
    public boolean updatePublisher(PublisherDTO publisherDTO) {
        try {
            return publisherDAL.update(publisherDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deletePublisher(long id) {
        try {
            return publisherDAL.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public long getCurrentID(){
        return publisherDAL.getCurrentID();
    }
    public int addPublisherFromExcel(List<PublisherDTO> publishers) {
        int successCount = 0;

        if (publishers == null || publishers.isEmpty()) {
            return 0;
        }

        for (PublisherDTO pub : publishers) {
            try {
                long newId = addPublisher(pub);
                if (newId > 0) {
                    successCount++;
                    pub.setId(newId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return successCount;
    }
    public boolean exportToExcel(String filePath) {
        try {
            // Tạo workbook mới
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách nhà xuất bản");

            // Tạo header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Tên nhà xuất bản", "Số điện thoại", "Địa chỉ"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (PublisherDTO pub : publisherList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pub.getId());
                row.createCell(1).setCellValue(pub.getName());
                row.createCell(2).setCellValue(pub.getPhone());
                row.createCell(3).setCellValue(pub.getAddress());
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
