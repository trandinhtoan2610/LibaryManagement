package BUS;

import DAL.EmployeeDAL;
import DTO.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class EmployeeBUS {
    private final EmployeeDAL employeeDAL = new EmployeeDAL();
    public static List<Employee> employeeList = new ArrayList<>();

    public EmployeeBUS() {
        if(employeeList.size() == 0) {
            employeeList = employeeDAL.findAll();
        }
    }

    public Employee login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username or password is null or empty");
        }
        return employeeDAL.login(username, password);
    }
    public Employee getEmployeeById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return employeeDAL.findById(id);
    }
    public List<Employee> getAllEmployees() {
        return employeeDAL.findAll();
    }

    public void showmess(String s) {
        JOptionPane.showMessageDialog(null, s);
    }
    public long addEmployee(Employee employee) {
        return employeeDAL.create(employee);
    }

    public boolean updateEmployee(Employee employee) {
        try {
            return employeeDAL.update(employee);
        }catch (Exception e){
            e.printStackTrace();    
            return false;
        }
    }
    public boolean deleteEmployee(long id) {
        try {
            return employeeDAL.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public long getCurrentID(){
        return employeeDAL.getCurrentID();
    }
    public int addEmployeesFromExcel(List<Employee> employees) {
        int successCount = 0;

        if (employees == null || employees.isEmpty()) {
            return 0;
        }

        for (Employee emp : employees) {
            try {
                long newId = addEmployee(emp);
                if (newId > 0) {
                    successCount++;
                    emp.setId(newId);
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
            Sheet sheet = workbook.createSheet("Danh sách nhân viên");

            // Tạo header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Họ và tên đệm", "Tên", "Giới tính", "Tài Khoản","Mật khẩu","Chức vụ", "Số điện thoại", "Địa chỉ", "Lương"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Employee emp : employeeList) {
                String position = "";
                if (emp.getRoleID() == 1L){
                    position = "Admin";
                } else if (emp.getRoleID() == 2L) {
                    position = "Quản lý";
                }else if (emp.getRoleID() == 3L) {
                    position = "Nhân viên";
                }else {
                    position = "";
                }
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(emp.getId());
                row.createCell(1).setCellValue(emp.getFirstName());
                row.createCell(2).setCellValue(emp.getLastName());
                row.createCell(3).setCellValue(emp.getGender().toString());
                row.createCell(4).setCellValue(emp.getUsername());
                row.createCell(5).setCellValue(emp.getPassword());
                row.createCell(6).setCellValue(position);
                row.createCell(7).setCellValue(emp.getPhone());
                row.createCell(8).setCellValue(emp.getAddress());
                row.createCell(9).setCellValue(emp.getSalary());
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
