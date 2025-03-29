package GUI;

import BUS.EmployeeBUS;
import DTO.Employee;
import DTO.Enum.Gender;
import GUI.Component.Table.EmployeeTable;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TestEmployee extends JFrame {
    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final EmployeeTable employeeTable = new EmployeeTable();

    public TestEmployee() {
        setupUI();
        loadData();
    }

    private void setupUI() {
        setTitle("Quản lý nhân viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        JPanel toolbar= new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(toolbar, BorderLayout.NORTH);
    }
    private void loadData() {
        List<Employee> employees = employeeBUS.getAllEmployees();
        employeeTable.setEmployees(employees);
    }
    public static void main(String[] args) {
            new TestEmployee().setVisible(true);
    }
}