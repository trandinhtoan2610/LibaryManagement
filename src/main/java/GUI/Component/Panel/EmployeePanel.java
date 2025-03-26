package GUI.Component.Panel;

import GUI.Component.Table.EmployeeTable;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {
    EmployeeTable employeeTable;

    public EmployeePanel() {
        this.setLayout(null);
        employeeTable = new EmployeeTable();
        this.add(employeeTable);
        this.setSize(employeeTable.getWidth(), employeeTable.getHeight());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
        EmployeePanel employeePanel = new EmployeePanel();
        frame.add(employeePanel);
        frame.setSize(employeePanel.getWidth(), employeePanel.getHeight());
        frame.setVisible(true);
    }
}
