package GUI.Excel;

import DTO.Employee;
import DTO.Enum.Gender;
import DTO.PublisherDTO;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelMaster {
    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook wb;
    private Sheet sh;
    private Cell cell;
    private Row row;
    private CellStyle cellstyle;
    private Color mycolor;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();

    public void setExcelFile(String ExcelPath, String SheetName) throws Exception {
        try {
            File f = new File(ExcelPath);
            if (!f.exists()) {
                throw new FileNotFoundException("File không tồn tại: " + ExcelPath);
            }

            fis = new FileInputStream(ExcelPath);
            wb = WorkbookFactory.create(fis);

            // Kiểm tra và tạo sheet nếu cần
            sh = wb.getSheet(SheetName);
            if (sh == null) {
                sh = wb.createSheet(SheetName);
                // Tạo header nếu sheet mới
                Row headerRow = sh.createRow(0);
                headerRow.createCell(0).setCellValue("ID");
                headerRow.createCell(1).setCellValue("Tên");
                // Thêm các cột header khác...
            }

            this.excelFilePath = ExcelPath;

            // Cập nhật columns map nếu có dữ liệu
            if (sh.getPhysicalNumberOfRows() > 0) {
                sh.getRow(0).forEach(cell -> {
                    columns.put(cell.getStringCellValue(), cell.getColumnIndex());
                });
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thiết lập file Excel: " + e.getMessage());
            throw e;
        }
    }

    public String getCellData(int rownum, int colnum) throws Exception {
        try {
            cell = sh.getRow(rownum).getCell(colnum);
            String CellData = null;
            switch (cell.getCellType()) {
                case STRING:
                    CellData = cell.getStringCellValue().trim();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf((long) cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = String.valueOf(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
                default:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            System.out.println("Error in getCellData: " + e.getMessage());
            return "";
        }
    }

    public String getCellData(String columnName, int rownum) throws Exception {
        return getCellData(rownum, columns.get(columnName));
    }

    // đọc File excel thêm vào đây
    public List<Employee> readEmployeesFromExcel() throws Exception {
        List<Employee> employees = new ArrayList<>();

        for (int i = 1; i <= sh.getLastRowNum(); i++) {
            Row row = sh.getRow(i);
            if (row == null) continue;

            Gender gender;
            String genderStr = getCellData("Gender", i).trim();
            if (genderStr.equals("Nữ")) {
                gender = Gender.Nữ;
            } else gender = Gender.Nam;
            Employee emp = new Employee(
                    getCellData("FirstName", i),
                    getCellData("LastName", i),
                    gender,
                    getCellData("Username", i),
                    getCellData("Password", i),
                    Long.parseLong(getCellData("RoleId", i)),
                    getCellData("Phone", i),
                    getCellData("Address", i),
                    Long.parseLong(getCellData("Salary", i))
            );
            employees.add(emp);
        }

        return employees;
    }
    public List<PublisherDTO> readPublisherFromExcel() throws Exception {
        List<PublisherDTO> publishers = new ArrayList<>();

        for (int i = 1; i <= sh.getLastRowNum(); i++) {
            Row row = sh.getRow(i);
            if (row == null) continue;

            PublisherDTO pul = new PublisherDTO(
                    getCellData("Name", i),
                    getCellData("Phone", i),
                    getCellData("Address", i)
            );
            publishers.add(pul);
        }

        return publishers;
    }
}
