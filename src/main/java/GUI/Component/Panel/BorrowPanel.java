package GUI.Component.Panel;

import BUS.BorrowDetailBUS;
import BUS.BorrowSheetBUS;
import BUS.EmployeeBUS;
import BUS.ReaderBUS;
import DTO.BorrowDTO;
import DTO.BorrowDetailDTO;
import DTO.Employee;
import DTO.Enum.Gender;
import DTO.Enum.Status;
import DTO.ReaderDTO;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddBorrowDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeleteBorrowDialog;
import GUI.Component.Dialog.UpdateBorrowDialog;
import GUI.Component.Filter.EmployeeFilter;
import GUI.Component.Panel.Components.SearchNavBarLabel;
import GUI.Component.Table.BookTable;
import GUI.Component.Table.BorrowDetailTable;
import GUI.Component.Table.BorrowinSheetTable;
import GUI.Component.TextField.RoundedTextField;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class BorrowPanel extends JPanel {
    private final BorrowSheetBUS borrowSheetBUS = new BorrowSheetBUS();
    private final BorrowDetailBUS borrowDetailBUS = new BorrowDetailBUS();
    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final ReaderBUS readerBUS = new ReaderBUS();
    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private JPanel searchNavBarLabel;
    private RoundedTextField searchfield;
    private JComboBox<String> searchOptionsComboBox;
    private JRadioButton allRadioButton;
    private JRadioButton borrowedRadioButton;
    private JRadioButton returnedRadioButton;
    private JRadioButton overdueRadioButton;
    private ButtonGroup buttonGroup;
    private ButtonRefresh buttonRefresh;
    private ButtonFilter filterButton;

    private JPanel readerPanel;
    private JPanel employeePanel;

    private BorrowDetailTable borrowDetailTable;
    private BorrowinSheetTable borrowinSheetTable;

    private Employee currentEmployee;
    private ReaderDTO currentReader;

    private BookTable bookTable;
    private TableRowSorter<TableModel> sorter;

    private JFrame parentFrame;

    public BorrowPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(0, 5));
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);
        borrowinSheetTable = new BorrowinSheetTable();
        borrowinSheetTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateEmployeeAndReaderInfo();
                updateBorrowDetailInfo();
            }
        });
        sorter = new TableRowSorter<>(borrowinSheetTable.getModel());
        borrowinSheetTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(borrowinSheetTable);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0)); // Thêm khoảng cách ngang 10px

        JPanel infoPanel = employeeNreaderPanel();
        infoPanel.setPreferredSize(new Dimension(400, 150));

        borrowDetailTable = new BorrowDetailTable();
        customizeDetailTable(); // Tùy chỉnh bảng chi tiết
        bottomPanel.setBackground(new Color(240, 240, 240));
        JScrollPane detailScrollPane = new JScrollPane(borrowDetailTable);
        detailScrollPane.setPreferredSize(new Dimension(300, 180));
        borrowDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        detailScrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu mượn"));
        bottomPanel.setPreferredSize(new Dimension(0, 220));
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(detailScrollPane, BorderLayout.CENTER);

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        paddedPanel.add(bottomPanel, BorderLayout.CENTER);
        paddedPanel.setBackground(new Color(240, 240, 240));
        this.add(paddedPanel, BorderLayout.SOUTH);
        loadData();
    }
    private void customizeDetailTable() {
        TableColumnModel columnModel = borrowDetailTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Cột 1
        columnModel.getColumn(1).setPreferredWidth(100); // Cột 2
        columnModel.getColumn(2).setPreferredWidth(100);// Cột 3
        borrowDetailTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        borrowDetailTable.setRowHeight(25);
        borrowDetailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        borrowDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }


    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddBorrowDialog addBorrowDialog = new AddBorrowDialog(parentFrame, BorrowPanel.this);
                addBorrowDialog.setVisible(true);
                refreshTable();
            }
        });
        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
                if (selectedBorrow == null) {
                    AlertDialog updateAlert = new AlertDialog(parentFrame, "Vui lòng chọn phiếu mượn cần sửa");
                    updateAlert.setVisible(true);
                } else {
                    UpdateBorrowDialog updateBorrowDialog = new UpdateBorrowDialog(parentFrame, BorrowPanel.this, selectedBorrow);
                    updateBorrowDialog.setVisible(true);
                    updateBorrow(selectedBorrow);
                    refreshTable();
                }
            }
        });
        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
                if (selectedBorrow == null) {
                    AlertDialog deleteAlert = new AlertDialog(parentFrame, "Vui lòng chọn phiếu mượn cần xóa");
                    deleteAlert.setVisible(true);
                } else {
                    DeleteBorrowDialog deleteBorrowDialog = new DeleteBorrowDialog(parentFrame, BorrowPanel.this, selectedBorrow);
                    deleteBorrowDialog.setVisible(true);
                    deleteBorrow(selectedBorrow);
                    borrowSheetBUS.deleteBorrowSheet(Long.parseLong(selectedBorrow.getId().substring(2)));
                    refreshTable();
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
        buttonPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private JPanel employeeNreaderPanel() {
        JPanel container = new JPanel(new GridBagLayout()); // Dùng GridBagLayout để tùy chỉnh kích thước tốt hơn
        container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.setBackground(new Color(245, 245, 245)); // Màu nền sáng
        getEmployeeNReaderbyId();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        employeePanel = new JPanel(new GridLayout(2, 1));
        employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));
        employeePanel.setBackground(Color.WHITE);
        employeePanel.setPreferredSize(new Dimension(350, 80));
        employeePanel.add(createStyledLabel("Mã NV: "));
        employeePanel.add(createStyledLabel("Họ tên: "));
        gbc.gridy = 0;
        gbc.weighty = 0.40;
        container.add(employeePanel, gbc);
        readerPanel = new JPanel(new GridLayout(5, 1));
        readerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Độc giả"));
        readerPanel.setBackground(Color.WHITE);
        readerPanel.setPreferredSize(new Dimension(350, 120));
        readerPanel.add(createStyledLabel("Mã DG: "));
        readerPanel.add(createStyledLabel("Họ tên: "));
        readerPanel.add(createStyledLabel("Giới tính: "));
        readerPanel.add(createStyledLabel("Điện thoại: "));
        readerPanel.add(createStyledLabel("Địa chỉ: "));
        gbc.gridy = 1;
        gbc.weighty = 0.60;
        container.add(readerPanel, gbc);
        return container;
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return label;
    }
    private void loadData() {
        List<BorrowDTO> borrows = borrowSheetBUS.getAllBorrowSheet();
        if (borrows != null) {
            borrowinSheetTable.setBorrows(borrows);
        }else{
            System.out.println("Không có dữ liệu nhân viên");
        }
    }

    public void refreshTable() {
        borrowinSheetTable.refreshTable();
    }

    public void addBorrow(BorrowDTO borrowDTO) {
        borrowinSheetTable.addBorrow(borrowDTO);
    }

    public void updateBorrow(BorrowDTO borrowDTO) {
        borrowinSheetTable.updateBorrow(borrowDTO);
    }

    public void deleteBorrow(BorrowDTO borrowDTO) {
        borrowinSheetTable.removeBorrow(borrowDTO);
    }
    private void getEmployeeNReaderbyId() {
        int selectedRow = borrowinSheetTable.getSelectedRow();
        if (selectedRow != -1) {
            BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
            currentEmployee = employeeBUS.getEmployeeById(selectedBorrow.getEmployeeId());
            currentReader = readerBUS.findReaderByID(selectedBorrow.getReaderId());
        }
    }

    private void updateEmployeeAndReaderInfo() {
        int selectedRow = borrowinSheetTable.getSelectedRow();
        if (selectedRow != -1) {
            BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
            currentEmployee = employeeBUS.getEmployeeById(selectedBorrow.getEmployeeId());
            currentReader = readerBUS.findReaderByID(selectedBorrow.getReaderId());

            employeePanel.removeAll();
            employeePanel.setLayout(new GridLayout(2, 1));
            employeePanel.add(createStyledLabel("Mã NV: " + currentEmployee.getId()));
            employeePanel.add(createStyledLabel("Họ tên: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName()));
            employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));

            readerPanel.removeAll();
            readerPanel.setLayout(new GridLayout(5, 1));
            readerPanel.add(createStyledLabel("Mã DG: " + currentReader.getId()));
            readerPanel.add(createStyledLabel("Họ tên: " + currentReader.getLastName() + " " + currentReader.getFirstName()));
            readerPanel.add(createStyledLabel("Giới tính: " + currentReader.getGender().toString()));
            readerPanel.add(createStyledLabel("Điện thoại: " + currentReader.getPhone()));
            readerPanel.add(createStyledLabel("Địa chỉ: " + currentReader.getAddress()));
            readerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Độc giả"));

            employeePanel.revalidate();
            employeePanel.repaint();
            readerPanel.revalidate();
            readerPanel.repaint();
        }
    }
    private void updateBorrowDetailInfo() {
        int selectedRow = borrowinSheetTable.getSelectedRow();
        if (selectedRow != -1) {
            BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
            System.out.println(selectedBorrow.getId());
            List<BorrowDetailDTO> borrowDetails = borrowDetailBUS.getBorrowDetailsBySheetId(Long.parseLong(selectedBorrow.getId().substring(2)));
            borrowDetailTable.setBorrowDetails(borrowDetails);
        }
    }
    public JPanel getSearchNavBarLabel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topPanel.setBackground(Color.WHITE);

        String[] searchOptions = {"Mã phiếu mượn", "Mã Nhân Viên","Mã Độc Giả"};
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        bottomPanel.setBackground(Color.WHITE);

        ActionListener radioListener = e -> {
            if (e.getSource() == allRadioButton) {
                borrowedRadioButton.setSelected(false);
                returnedRadioButton.setSelected(false);
                overdueRadioButton.setSelected(false);
            } else {
                allRadioButton.setSelected(false);
            }
            performSearch();
        };
        allRadioButton = new JRadioButton("Tất cả");
        borrowedRadioButton = new JRadioButton("Đang Mượn");
        returnedRadioButton = new JRadioButton("Đã Trả");
        overdueRadioButton = new JRadioButton("Quá Ngày");

        allRadioButton.setBackground(Color.WHITE);
        borrowedRadioButton.setBackground(Color.WHITE);
        returnedRadioButton.setBackground(Color.WHITE);
        overdueRadioButton.setBackground(Color.WHITE);
        allRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowedRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        returnedRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        overdueRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowedRadioButton.addActionListener(radioListener);
        returnedRadioButton.addActionListener(radioListener);
        overdueRadioButton.addActionListener(radioListener);
        allRadioButton.addActionListener(radioListener);

        JLabel genderLabel = new JLabel("Trạng Thái:");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(-10, 0));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(borrowedRadioButton);
        buttonGroup.add(returnedRadioButton);
        buttonGroup.add(overdueRadioButton);
        buttonGroup.add(allRadioButton);
        allRadioButton.setSelected(true);

        bottomPanel.add(emptyPanel);
        bottomPanel.add(genderLabel);
        bottomPanel.add(allRadioButton);
        bottomPanel.add(borrowedRadioButton);
        bottomPanel.add(returnedRadioButton);
        bottomPanel.add(overdueRadioButton);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        return mainPanel;
    }
    private void performSearch(){
        try{
            String searchText = searchfield.getText();
            int searchColumm = searchOptionsComboBox.getSelectedIndex();
            List<RowFilter<Object, Object>> filters = new ArrayList<>();
            if(!searchText.isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + searchText, searchColumm));
            }
            if(!allRadioButton.isSelected()){
                if(borrowedRadioButton.isSelected() || returnedRadioButton.isSelected() || overdueRadioButton.isSelected()) {
                    Status statusFilter;
                    if (borrowedRadioButton.isSelected()) {
                        statusFilter = Status.Đang_Mượn;
                    } else if (returnedRadioButton.isSelected()) {
                        statusFilter = Status.Đã_Trả;
                    } else {
                        statusFilter = Status.Quá_Ngày;
                    }
                    filters.add(RowFilter.regexFilter(statusFilter.toString(), 6));
                }
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


    }
    public void refreshData() {
        borrowinSheetTable.refreshTable();
        borrowDetailTable.refreshTable();
        sorter.setRowFilter(null);
        searchfield.setText("");
        buttonGroup.clearSelection();
        allRadioButton.setSelected(true);
    }
}
