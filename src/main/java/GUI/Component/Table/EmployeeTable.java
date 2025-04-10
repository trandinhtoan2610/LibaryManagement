package GUI.Component.Table;

import DTO.Employee;
import DTO.Enum.Gender;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTable extends JTableCustom {
    private static final String[] HEADER = {
            "ID", "Họ Và Tên", "Giới Tính",
            "Tài Khoản", "Mật Khẩu", "Chức Vụ", "Số Điện Thoại",
            "Địa Chỉ", "Lương"
    };

    private DefaultTableModel tableModel;
    private static List<Employee> employees;

    public EmployeeTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.employees = new ArrayList<>();
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);
        getColumnModel().getColumn(0).setPreferredWidth(35);
        getColumnModel().getColumn(1).setPreferredWidth(175);
        getColumnModel().getColumn(2).setPreferredWidth(90);
        getColumnModel().getColumn(3).setPreferredWidth(90);
        getColumnModel().getColumn(4).setPreferredWidth(90);
        getColumnModel().getColumn(5).setPreferredWidth(90);
        getColumnModel().getColumn(6).setPreferredWidth(100);
        getColumnModel().getColumn(7).setPreferredWidth(120);
        getColumnModel().getColumn(8).setPreferredWidth(180);
        setAutoCreateRowSorter(true);
    }
    public void setEmployees(List<Employee> employees) {
        if(employees != null) {
            this.employees = new ArrayList<>(employees);
        }else{
            this.employees = new ArrayList<>();
        }
        refreshTable();
    }

    public void addEmployee(Employee employee) {
        if (employee != null) {
            employees.add(employee);
            refreshTable();
        }
    }
    public boolean updateEmployee(Employee employee) {
        Employee selectedEmployee = getSelectedEmployee();
        if (selectedEmployee != null) {
            int index = employees.indexOf(selectedEmployee);
            if (index != -1) {
                employees.set(index, employee);
                refreshTable();
                return true;
            }
        }
        return false;
    }
    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        refreshTable();
    }

    public Employee getSelectedEmployee() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return employees.get(modelRow);
        }
        return null;
    }
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            Long tmp = emp.getRoleID();
            String role = "";
            if(tmp == 1L){
                role = "Admin";
            }else if(tmp == 2L){
                role = "Quản Lý";
            }else{
                role = "Nhân Viên";
            }
            String gender = "";
            if (emp.getGender() == Gender.Nam) {
                gender = "Nam";
            } else {
                gender = "Nữ";
            }
            String fullName = emp.getFirstName() + " " + emp.getLastName();
            Object[] rowData = {
                    emp.getId(),
                    fullName,
                    gender,
                    emp.getUsername(),
                    "********",
                    role,
                    emp.getPhone(),
                    emp.getAddress(),
                    emp.getSalary()
            };
            tableModel.addRow(rowData);
        }
    }
}