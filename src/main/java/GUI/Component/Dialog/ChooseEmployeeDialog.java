package GUI.Component.Dialog;

import BUS.EmployeeBUS;
import DTO.Employee;
import GUI.Component.Table.EmployeeTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;

public class ChooseEmployeeDialog extends JDialog {
    private final EmployeeTable employeeTable = new EmployeeTable();
    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private Employee selectedEmployee;

    public ChooseEmployeeDialog(JDialog parent) {
        super(parent, "Chọn nhân viên", true);
        setSize(500, 300);
        setLocationRelativeTo(parent);
        if (parent != null) {
            Point location = this.getLocation();
            this.setLocation(location.x + 50, location.y + 90);
        }
        setLayout(new BorderLayout(10, 10));
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        employeeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
                    int selectedRow = employeeTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedEmployee = employeeTable.getSelectedEmployee();
                        dispose();
                    }
                }
            }
        });
        loadData();
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    private void loadData() {
        List<Employee> employees = employeeBUS.getAllEmployees();
        if (employees != null) {
            employeeTable.setEmployees(employees);
        } else {
            System.out.println("Không có dữ liệu");
        }
    }
}
