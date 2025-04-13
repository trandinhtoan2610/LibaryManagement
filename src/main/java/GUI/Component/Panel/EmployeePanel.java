package GUI.Component.Panel;

import BUS.EmployeeBUS;
import DTO.Employee;
import DTO.Enum.Gender;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddEmployeeDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeleteEmployeeDialog;
import GUI.Component.Dialog.UpdateEmployeeDialog;
import GUI.Component.Filter.EmployeeFilter;
import GUI.Component.Table.EmployeeTable;
import GUI.Component.TextField.RoundedTextField;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EmployeePanel extends JPanel {
    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final EmployeeTable employeeTable = new EmployeeTable();
    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private JPanel searchNavBarLabel;
    private RoundedTextField searchfield;
    private ButtonFilter filterButton;
    private JComboBox<String> searchOptionsComboBox;
    private JRadioButton allRadioButton;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private ButtonGroup buttonGroup;
    private ButtonRefresh buttonRefresh;

    //Tim Kiem

    private TableRowSorter<TableModel> sorter;

    private boolean salaryFilterActive = false;
    private double currentMinSalary = 0;
    private double currentMaxSalary = Double.MAX_VALUE;
    private String currentPositionFilter = "";

    private JFrame parentFrame;
    public EmployeePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        //Tim kiem
        sorter = new TableRowSorter<>(employeeTable.getModel());
        employeeTable.setRowSorter(sorter);

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
                AddEmployeeDialog addEmployeeDialog = new AddEmployeeDialog(parentFrame, EmployeePanel.this);
                addEmployeeDialog.setVisible(true);
            }
        });
        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Employee employee = employeeTable.getSelectedEmployee();
                if(employee == null){
                    AlertDialog updateAlert = new AlertDialog(parentFrame, "Vui lòng chọn nhân viên cần sửa");
                    updateAlert.setVisible(true);
                }else{
                    UpdateEmployeeDialog updateEmployeeDialog = new UpdateEmployeeDialog(parentFrame, EmployeePanel.this, employee);
                    updateEmployeeDialog.setVisible(true);
                }
            }
        });
        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Employee employee = employeeTable.getSelectedEmployee();
                if(employee == null){
                    AlertDialog deleteAlert = new AlertDialog(parentFrame, "Vui lòng chọn nhân viên cần xóa");
                    deleteAlert.setVisible(true);
                }else{
                    DeleteEmployeeDialog deleteEmployeeDialog = new DeleteEmployeeDialog(parentFrame, EmployeePanel.this, employee);
                    deleteEmployeeDialog.setVisible(true);
                }
            }
        });
        buttonExportExcel = new ButtonExportExcel();
        buttonImportExcel = new ButtonImportExcel();
        searchNavBarLabel = getSearchNavBarLabel();
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonExportExcel);
        buttonPanel.add(buttonImportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
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
        sorter.setRowFilter(null);
        searchfield.setText("");
        buttonGroup.clearSelection();
        salaryFilterActive = false;
        currentMinSalary = 0;
        currentMaxSalary = Double.MAX_VALUE;
        currentPositionFilter = "";
        allRadioButton.setSelected(true);
    }
    public void addEmployee(Employee employee) {
        employeeTable.addEmployee(employee);
    }
    public void removeEmployee(Employee employee) {
        employeeTable.removeEmployee(employee);
    }
    public void updateEmployee(Employee employee) {
        employeeTable.updateEmployee(employee);
    }
    public Employee getSelectedEmployee() {
       return employeeTable.getSelectedEmployee();
    }
    public JPanel getSearchNavBarLabel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topPanel.setBackground(Color.WHITE);

        String[] searchOptions = {"Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Địa chỉ"};
        searchOptionsComboBox = new JComboBox<>(searchOptions);

        searchOptionsComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchOptionsComboBox.setBackground(Color.WHITE);
        searchOptionsComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });

        searchfield = new RoundedTextField(12, 15, 15);
        searchfield.setPlaceholder("Từ khóa tìm kiếm....");
        searchfield.setBackground(new Color(245, 245, 245));
        searchfield.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchfield.setBorderColor(new Color(200, 200, 200));
        searchfield.setFocusBorderColor(new Color(0, 120, 215));
        searchfield.addActionListener(e -> performSearch());

        searchfield.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }
        });

        filterButton = new ButtonFilter();
        filterButton.addActionListener(e -> openDialogFilter());

        buttonRefresh = new ButtonRefresh();
        buttonRefresh.addActionListener(e -> refreshData());

        topPanel.add(searchOptionsComboBox);
        topPanel.add(searchfield);
        topPanel.add(filterButton);
        topPanel.add(buttonRefresh);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        bottomPanel.setBackground(Color.WHITE);

        ActionListener radioListener = e -> {
            if (e.getSource() == allRadioButton) {
                maleRadioButton.setSelected(false);
                femaleRadioButton.setSelected(false);
            } else {
                allRadioButton.setSelected(false);
            }
            performSearch();
        };
        allRadioButton = new JRadioButton("Tất cả");
        maleRadioButton = new JRadioButton("Nam");
        femaleRadioButton = new JRadioButton("Nữ");

        allRadioButton.setBackground(Color.WHITE);
        maleRadioButton.setBackground(Color.WHITE);
        femaleRadioButton.setBackground(Color.WHITE);
        allRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        maleRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        femaleRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        maleRadioButton.addActionListener(radioListener);
        femaleRadioButton.addActionListener(radioListener);
        allRadioButton.addActionListener(radioListener);

        JLabel genderLabel = new JLabel("Giới tính:");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(30, 0));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(maleRadioButton);
        buttonGroup.add(femaleRadioButton);
        buttonGroup.add(allRadioButton);
        allRadioButton.setSelected(true);

        bottomPanel.add(emptyPanel);
        bottomPanel.add(genderLabel);
        bottomPanel.add(allRadioButton);
        bottomPanel.add(maleRadioButton);
        bottomPanel.add(femaleRadioButton);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        return mainPanel;
    }

    private void performSearch(){
        try{
            String searchText = searchfield.getText();
            int searchColumm = searchOptionsComboBox.getSelectedIndex();
            if(searchColumm == 3){
                searchColumm = 7;
            }
            if(searchColumm == 2){
                searchColumm = 6;
            }
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            //Theo tu khoa tim kiem
            if(!searchText.isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + searchText, searchColumm));
            }
            //Theo gioi tinh
            if(!allRadioButton.isSelected()){
                if(maleRadioButton.isSelected() || femaleRadioButton.isSelected()) {
                    Gender genderFilter = maleRadioButton.isSelected() ? Gender.Nam : Gender.Nữ;
                    filters.add(RowFilter.regexFilter(genderFilter.toString(), 2));
                }
            }
            if (salaryFilterActive){
                if(!currentPositionFilter.isEmpty()){
                    System.out.println(currentPositionFilter);
                    filters.add(RowFilter.regexFilter(currentPositionFilter, 5));
                }
                System.out.println(currentMinSalary);
                System.out.println(currentMaxSalary);
                filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, currentMaxSalary+0.0001, 8));
                filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, currentMinSalary -0.0001, 8));
            }
            if(filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void openDialogFilter(){
        EmployeeFilter dialog = new EmployeeFilter(parentFrame);
        dialog.setEmployeeFilterListener((position, minSalary, maxSalary) -> {
            salaryFilterActive = true;
            currentMinSalary = minSalary;
            currentMaxSalary = maxSalary;
            currentPositionFilter = position;
            performSearch();
        });
        dialog.setVisible(true);
    }
}
