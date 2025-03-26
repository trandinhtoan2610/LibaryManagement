package GUI.Component.Table;

import DTO.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EmployeeTable extends JTableCustom {
    String header[] = {"ID", "First Name", "Last Name", "Gender", "Username", "Password", "RoleID", "Phone", "Address", "Salary"};
    ArrayList<Employee> employees;
    public EmployeeTable() {
        super(new DefaultTableModel());

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee Table");
        frame.getContentPane().add(new EmployeeTable());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
