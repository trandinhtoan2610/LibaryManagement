package GUI.page;

import BUS.EmployeeBUS;
import DTO.Employee;
import DTO.Enum.Gender;
import GUI.Component.Table.EmployeeTable;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PurchasePage extends JFrame {
    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final EmployeeTable employeeTable = new EmployeeTable();



    private void setupUI() {
        setTitle("Quản lý nhân viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        // Toolbar với các nút chức năng
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Tải lại");
        JButton btnAdd = new JButton("Thêm");
        JButton btnDelete = new JButton("Xóa");

        toolbar.add(btnRefresh);
        toolbar.add(btnAdd);
        toolbar.add(btnDelete);
        add(toolbar, BorderLayout.NORTH);
    }

    private void loadData() {
        List<Employee> employees = employeeBUS.getAllEmployees();
        employeeTable.setEmployees(employees);
    }

    private void addEmployee() {
        // Hiển thị dialog thêm nhân viên
        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JComboBox<Gender> cboGender = new JComboBox<>(Gender.values());

        Object[] fields = {
                "First Name:", txtFirstName,
                "Last Name:", txtLastName,
                "Gender:", cboGender
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Thêm nhân viên",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            Employee newEmployee = new Employee(
                    null, // ID tự sinh
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    (Gender) cboGender.getSelectedItem(),
                    "username_default", // Nên thêm field nhập username
                    "password_default", // Nên thêm field nhập password
                    1L, // RoleID mặc định
                    "", // Phone
                    "", // Address
                    0f // Salary
            );

            try {
                employeeBUS.addEmployee(newEmployee);
                loadData(); // Refresh table
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteEmployee() {
        Employee selected = employeeTable.getSelectedEmployee();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Xóa nhân viên " + selected.getFirstName() + "?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                employeeBUS.deleteEmployee(selected.getId());
                loadData(); // Refresh table
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
        }
    }

    public static void main(String[] args) {
        new PurchasePage().setVisible(true);
    }
}