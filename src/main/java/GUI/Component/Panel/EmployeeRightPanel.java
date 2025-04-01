package GUI.Component.Panel;

import BUS.EmployeeBUS;
import DTO.Employee;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddEmployeeDialog;
import GUI.Component.Panel.Components.SearchNavBarLabel;
import GUI.Component.Table.EmployeeTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EmployeeRightPanel extends JPanel {
    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final EmployeeTable employeeTable = new EmployeeTable();
    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private SearchNavBarLabel searchNavBarLabel;
    private JFrame parentFrame;
    public EmployeeRightPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(0, 17));
        this.add(emptyPanel, BorderLayout.SOUTH);
        loadData();
    }
    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddEmployeeDialog addEmployeeDialog = new AddEmployeeDialog(parentFrame, EmployeeRightPanel.this);
                addEmployeeDialog.setVisible(true);
                employeeTable.refreshTable();
            }
        });
        buttonUpdate = new ButtonUpdate();
        buttonDelete = new ButtonDelete();
        buttonExportExcel = new ButtonExportExcel();
        buttonImportExcel = new ButtonImportExcel();
        searchNavBarLabel = new SearchNavBarLabel();
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonExportExcel);
        buttonPanel.add(buttonImportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }
    private void loadData() {
        List<Employee> employees = employeeBUS.getAllEmployees();
        if (employees != null) {
            employeeTable.setEmployees(employees);
        }else{
            System.out.println("Không có dữ liệu nhân viên");
        }
    }
    public void refreshData() {
        employeeTable.refreshTable();
    }
}
