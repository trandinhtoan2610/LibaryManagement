package GUI.Component.Table;

import DTO.Employee;
import DTO.Enum.Gender;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
public class EmployeeTable extends JTableCustom {
    private static final String[] HEADER = {
            "ID", "First Name", "Last Name", "Gender",
            "Username", "Password", "RoleID", "Phone",
            "Address", "Salary"
    };

    private DefaultTableModel tableModel;
    private List<Employee> employees;

    public EmployeeTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.employees = new ArrayList<>();
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getColumnModel().getColumn(0).setPreferredWidth(35);
        getColumnModel().getColumn(1).setPreferredWidth(175);
        getColumnModel().getColumn(2).setPreferredWidth(90);
        getColumnModel().getColumn(3).setPreferredWidth(90);
        getColumnModel().getColumn(4).setPreferredWidth(90);
        getColumnModel().getColumn(5).setPreferredWidth(90);
        getColumnModel().getColumn(6).setPreferredWidth(55);
        getColumnModel().getColumn(7).setPreferredWidth(120);
        getColumnModel().getColumn(8).setPreferredWidth(180);
        getColumnModel().getColumn(9).setPreferredWidth(150);
        setAutoCreateRowSorter(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    Employee selectedEmployee = getSelectedEmployee();
                    if(selectedEmployee != null) {
                        openEmployeeDetails(selectedEmployee);
                    }
                }
            }
        });
    }
    private void openEmployeeDetails(Employee employee) {
        JOptionPane.showMessageDialog(
                this,
                "Chi tiết nhân viên:\n" +
                        "ID: " + employee.getId() + "\n" +
                        "Tên: " + employee.getFirstName() + " " + employee.getLastName(),
                "Thông tin nhân viên",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees != null ? new ArrayList<>(employees) : new ArrayList<>();
        refreshTable();
    }

    public void addEmployee(Employee employee) {
        if (employee != null) {
            employees.add(employee);
            refreshTable();
        }
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
    private String formatVND(double amount) {
        if (amount >= 1_000_000_000) {
            return String.format("%,.2f tỷ đ", amount / 1_000_000_000);
        } else if (amount >= 1_000_000) {
            return String.format("%,.2f triệu đ", amount / 1_000_000);
        } else {
            return String.format("%,.2f đ", amount);
        }
    }
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            Object[] rowData = {
                    emp.getId(),
                    emp.getFirstName(),
                    emp.getLastName(),
                    emp.getGender(),
                    emp.getUsername(),
                    "********",
                    emp.getRoleID(),
                    emp.getPhone(),
                    emp.getAddress(),
                    formatVND(emp.getSalary()),
            };
            tableModel.addRow(rowData);
        }
    }
    public static void main(String[] args) {
            JFrame frame = new JFrame("Employee Table");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            List<Employee> sampleData = new ArrayList<>();
            sampleData.add(new Employee(1L, "Hoàng Lê Nhất Thống", "Chí", Gender.Female, "jdoe", "pass123", 1L,
                    "0329997881", "270 Phan Đình Phùng", 550000000.0f));
            sampleData.add(new Employee(2L, "Jane", "Smith", Gender.Female, "jsmith", "pass456", 2L,
                    "555-0202", "456 Oak Ave", 65000.0f));
            EmployeeTable table = new EmployeeTable();
            table.setEmployees(sampleData);
            frame.add(new JScrollPane(table));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
    }
}