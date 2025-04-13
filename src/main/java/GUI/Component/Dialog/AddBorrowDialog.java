package GUI.Component.Dialog;

import BUS.BorrowDetailBUS;
import BUS.BorrowSheetBUS;
import BUS.EmployeeBUS;
import BUS.ReaderBUS;
import DTO.BorrowDTO;
import DTO.BorrowDetailDTO;
import DTO.Employee;
import DTO.Enum.Status;
import DTO.Enum.SubStatus;
import DTO.ReaderDTO;
import GUI.Component.Button.ButtonBack;
import GUI.Component.Button.ButtonChosen;
import GUI.Component.Button.ButtonIcon;
import GUI.Component.Panel.BorrowPanel;
import GUI.Component.Table.BorrowDetailTable;
import GUI.Component.TextField.CustomTextField;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddBorrowDialog extends JDialog {
    private BorrowSheetBUS borrowSheetBUS = new BorrowSheetBUS();
    private BorrowDetailBUS borrowDetailBUS = new BorrowDetailBUS();
    private JLabel employeeLabel;
    private JLabel readerLabel;
    private CustomTextField employeeField;
    private CustomTextField readerField;
    private ButtonChosen employeeChosen;
    private ButtonChosen readerChosen;

    private JLabel bookLabel;
    private BorrowDetailTable borrowDetailTable;

    private JButton addDetailButton;
    private JButton editDetailButton;
    private JButton deleteDetailButton;

    private JLabel borrowedDateLabel;
    private JLabel dueDateLabel;
    private JLabel actualReturnDateLabel;

    private JDateChooser borrowedDateChooser;
    private JDateChooser dueDateChooser;
    private JDateChooser actualReturnDateChooser;

    private Employee currentEmployee;
    private ReaderDTO currentReader;
    private JLabel employeeNameLabel;

    private JLabel readerNameLabel;
    private JLabel readerGenderLabel;
    private JLabel readerPhoneLabel;
    private JLabel readerAddressLabel;

    private JLabel statusLabel;
    private JLabel statusValueLabel;
    private ReaderBUS readerBUS = new ReaderBUS();
    private EmployeeBUS employeeBUS = new EmployeeBUS();

    private Long currentBorrowID;
    private BorrowPanel borrowPanel;
    private List<BorrowDetailDTO> pendingBorrowDetails = new ArrayList<>();

    public AddBorrowDialog(JFrame parent, BorrowPanel borrowPanel) {
        super(parent, "Thêm Phiếu Mượn", true);
        initComponents();
        this.borrowPanel = borrowPanel;
        setSize(700, 750);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        ButtonBack backButton = new ButtonBack();
        backButton.addActionListener(e -> dispose());
        panel.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel("Thêm Phiếu Mượn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Main content panel with vertical layout
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        // Panel for employee information
        JPanel employeePanel = new JPanel(new BorderLayout(5, 5));
        employeeLabel = new JLabel("Nhân viên (Mã NV):");
        employeeField = new CustomTextField();
        employeeField.setPreferredSize(new Dimension(75, 30));
        employeeChosen = new ButtonChosen();
        employeeChosen.addActionListener(e -> chooseEmployee());

        // Add action listener to employee field to show info when ID is entered
        employeeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }
        });

        JPanel employeeInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        employeeInputPanel.add(employeeLabel);
        employeeInputPanel.add(employeeField);
        employeeInputPanel.add(employeeChosen);

        JPanel employeeDetailsPanel = new JPanel(new GridLayout(1, 1, 5, 5));
        employeeNameLabel = new JLabel("          Tên NV: ");
        employeeDetailsPanel.add(employeeNameLabel);

        employeePanel.add(employeeInputPanel, BorderLayout.NORTH);
        employeePanel.add(employeeDetailsPanel, BorderLayout.CENTER);

        // Panel for reader information
        JPanel readerPanel = new JPanel(new BorderLayout(5, 5));
        readerLabel = new JLabel("Độc giả (Mã DG):    ");
        readerField = new CustomTextField();
        readerField.setPreferredSize(new Dimension(75, 30));
        readerChosen = new ButtonChosen();
        readerChosen.addActionListener(e -> chooseReader());
        readerField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showReaderInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showReaderInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showReaderInfo();
            }
        });
        JPanel readerInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        readerInputPanel.add(readerLabel);
        readerInputPanel.add(readerField);
        readerInputPanel.add(readerChosen);
        JPanel readerDetailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        readerNameLabel = new JLabel("          Tên độc giả: ");
        readerGenderLabel = new JLabel("          Giới tính: ");
        readerPhoneLabel = new JLabel("          SĐT: ");
        readerAddressLabel = new JLabel("          Địa chỉ: ");
        readerDetailsPanel.add(readerNameLabel);
        readerDetailsPanel.add(readerGenderLabel);
        readerDetailsPanel.add(readerPhoneLabel);
        readerDetailsPanel.add(readerAddressLabel);

        readerPanel.add(readerInputPanel, BorderLayout.NORTH);
        readerPanel.add(readerDetailsPanel, BorderLayout.CENTER);
        JPanel bookPanel = new JPanel(new BorderLayout(5, 5));
        bookLabel = new JLabel("Chi Tiết Phiếu Mượn:");
        bookPanel.add(bookLabel, BorderLayout.NORTH);
        borrowDetailTable = new BorrowDetailTable();
        JScrollPane scrollPane = new JScrollPane(borrowDetailTable);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        JPanel detailButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        addDetailButton = new ButtonIcon("/icons/addbook.svg");
        addDetailButton.addActionListener(e -> addBorrowDetail());
        deleteDetailButton = new ButtonIcon("/icons/deleteDetails.svg");
        deleteDetailButton.addActionListener(e -> deleteBorrowDetails());
        editDetailButton = new ButtonIcon("/icons/editDetails.svg");
        editDetailButton.addActionListener(e -> editBorrowDetails());

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(250, 0));
        statusLabel = new JLabel("Trạng thái:");
        statusValueLabel = new JLabel();

        detailButtonPanel.add(addDetailButton);
        detailButtonPanel.add(editDetailButton);
        detailButtonPanel.add(deleteDetailButton);
        detailButtonPanel.add(emptyPanel);
        detailButtonPanel.add(statusLabel);
        detailButtonPanel.add(statusValueLabel);
        bookPanel.add(scrollPane, BorderLayout.CENTER);
        bookPanel.add(detailButtonPanel, BorderLayout.SOUTH);

        // Panel chứa ngày mượn, hạn trả, ngày trả thực tế
        JPanel datePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        borrowedDateLabel = new JLabel("Ngày mượn:");
        borrowedDateChooser = new JDateChooser();
        borrowedDateChooser.setDateFormatString("dd/MM/yyyy");
        borrowedDateChooser.setPreferredSize(new Dimension(150, 30));
        dueDateLabel = new JLabel("Hạn trả:");
        dueDateChooser = new JDateChooser();
        dueDateChooser.setDateFormatString("dd/MM/yyyy");
        dueDateChooser.setPreferredSize(new Dimension(150, 30));
        actualReturnDateLabel = new JLabel("Ngày trả thực tế:");
        actualReturnDateChooser = new JDateChooser();
        actualReturnDateChooser.setDateFormatString("dd/MM/yyyy");
        actualReturnDateChooser.setPreferredSize(new Dimension(150, 30));
        actualReturnDateChooser.setEnabled(false);

        datePanel.add(borrowedDateLabel);
        datePanel.add(borrowedDateChooser);
        datePanel.add(dueDateLabel);
        datePanel.add(dueDateChooser);
        datePanel.add(actualReturnDateLabel);
        datePanel.add(actualReturnDateChooser);
        mainContentPanel.add(employeePanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(readerPanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(bookPanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(datePanel);

        panel.add(mainContentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setBackground(new Color(255, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(120, 30));
        cancelButton.addActionListener(e -> dispose());

        JButton addButton = new JButton("Thêm");
        addButton.setPreferredSize(new Dimension(120, 30));
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addBorrow());

        panel.add(cancelButton);
        panel.add(addButton);

        return panel;
    }

    private void editBorrowDetails() {
        int selectedRow = borrowDetailTable.getSelectedRow();
        BorrowDetailDTO selectedBorrowDetail = borrowDetailTable.getSelectedBorrowDetail();
        if (selectedBorrowDetail != null) {
            UpdateBorrowDetailDialog updateBorrowDetailDialog = new UpdateBorrowDetailDialog(this, getTempBorrowID(), selectedBorrowDetail);
            updateBorrowDetailDialog.setVisible(true);
            if (updateBorrowDetailDialog.getCurrentBorrowDetail() != null) {
                pendingBorrowDetails.set(selectedRow, updateBorrowDetailDialog.getCurrentBorrowDetail());
                borrowDetailTable.setBorrowDetails(pendingBorrowDetails);
                borrowDetailTable.refreshTable();
                updateMainStatus();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi tiết phiếu mượn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateMainStatus() {
        if (pendingBorrowDetails.isEmpty()) {
            statusValueLabel.setText("");
        } else {
            boolean flag = true;
            for (BorrowDetailDTO borrowDetail : pendingBorrowDetails) {
                if (borrowDetail.getStatus() == SubStatus.Đang_Mượn) {
                    flag = false;
                    statusValueLabel.setText("Đang Mượn");
                    return;
                }
            }
            if (flag) {
                statusValueLabel.setText("Đã Trả");
            }
        }
    }

    private void deleteBorrowDetails() {
        int selectedRow = borrowDetailTable.getSelectedRow();
        BorrowDetailDTO selectedBorrowDetail = borrowDetailTable.getSelectedBorrowDetail();
        if (selectedRow != -1) {
            DeleteBorrowDetailDialog deleteBorrowDetailDialog = new DeleteBorrowDetailDialog(this, getTempBorrowID(), selectedBorrowDetail);
            deleteBorrowDetailDialog.setVisible(true);
            if (deleteBorrowDetailDialog.isConfirmed()) {
                pendingBorrowDetails.remove(selectedRow);
                borrowDetailTable.setBorrowDetails(pendingBorrowDetails);
                borrowDetailTable.refreshTable();
                updateMainStatus();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi tiết phiếu mượn để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showEmployeeInfo() {
        String employeeId = employeeField.getText();
        if (employeeId.isEmpty()) {
            employeeNameLabel.setText("          Tên NV: Không tìm thấy");
        } else {
            try {
                Long employee = Long.parseLong(employeeId);
                currentEmployee = employeeBUS.getEmployeeById(employee);
                employeeNameLabel.setText("          Tên NV: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void showReaderInfo() {
        String readerId = readerField.getText();
        if (readerId.isEmpty()) {
            readerNameLabel.setText("          Tên độc giả: Không tìm thấy");
            readerGenderLabel.setText("Giới tính: Không tìm thấy");
            readerPhoneLabel.setText("          SĐT: Không tìm thấy");
            readerAddressLabel.setText("Địa chỉ: Không tìm thấy");
        } else {
            try {
                Long reader = Long.parseLong(readerId);
                currentReader = readerBUS.findReaderByID(reader);
                readerNameLabel.setText("          Tên độc giả: " + currentReader.getLastName() + " " + currentReader.getFirstName());
                readerGenderLabel.setText("Giới tính: " + currentReader.getGender().toString());
                readerPhoneLabel.setText("          SĐT: " + currentReader.getPhone());
                readerAddressLabel.setText("Địa chỉ: " + currentReader.getAddress());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void chooseEmployee() {
        ChooseEmployeeDialog chooseEmployeeDialog = new ChooseEmployeeDialog(this);
        chooseEmployeeDialog.setVisible(true);
        if (chooseEmployeeDialog.getSelectedEmployee() != null) {
            currentEmployee = chooseEmployeeDialog.getSelectedEmployee();
            employeeField.setText(currentEmployee.getId().toString());
            employeeNameLabel.setText("          Tên NV: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName());
        }

    }

    private void chooseReader() {
        ChooseReaderDialog chooseReaderDialog = new ChooseReaderDialog(this);
        chooseReaderDialog.setVisible(true);
        if (chooseReaderDialog.getSelectedReader() != null) {
            currentReader = chooseReaderDialog.getSelectedReader();
            readerField.setText(currentReader.getId().toString());
            readerNameLabel.setText("          Tên độc giả: " + currentReader.getLastName() + " " + currentReader.getFirstName());
            readerGenderLabel.setText("Giới tính: " + currentReader.getGender().toString());
            readerPhoneLabel.setText("          SĐT: " + currentReader.getPhone());
            readerAddressLabel.setText("Địa chỉ: " + currentReader.getAddress());
        }
    }

    private void addBorrowDetail() {
        //tam
        long tempBorrowID = 0L;

        AddBorrowDetailDialog addBorrowDetailDialog = new AddBorrowDetailDialog(this, getTempBorrowID());
        addBorrowDetailDialog.setVisible(true);
        if (addBorrowDetailDialog.getCurrentBorrowDetail() != null) {
            pendingBorrowDetails.add(addBorrowDetailDialog.getCurrentBorrowDetail());
            borrowDetailTable.setBorrowDetails(pendingBorrowDetails);
            borrowDetailTable.refreshTable();
            updateMainStatus();
        }
    }

    private long getTempBorrowID() {
        return -1;
    }

    private void setCurrentID() {
        try {
            currentBorrowID = borrowSheetBUS.getCurrentID() + 1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy ID phiếu mượn: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean checkField() {
        if (employeeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (readerField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã độc giả!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (borrowedDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày mượn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dueDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hạn trả!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dueDateChooser.getDate().before(borrowedDateChooser.getDate())) {
            JOptionPane.showMessageDialog(this, "Hạn trả không được trước ngày mượn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (pendingBorrowDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm chi tiết phiếu mượn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void addBorrow() {
        setCurrentID();
        if (checkField()) {
            Long borrowSheetId = currentBorrowID;
            BorrowDTO borrowDTO = new BorrowDTO(
                    borrowSheetId,
                    Long.parseLong(employeeField.getText()),
                    Long.parseLong(readerField.getText()),
                    borrowedDateChooser.getDate(),
                    dueDateChooser.getDate(),
                    statusValueLabel.getText().equals("Đã Trả") ? Status.Đã_Trả : Status.Đang_Mượn
            );
            Long uuid = borrowSheetBUS.addBorrowSheet(borrowDTO);
            for (BorrowDetailDTO detail : pendingBorrowDetails) {
                detail.setBorrowSheetId(uuid);
                borrowDetailBUS.addBorrowDetail(detail);
            }
            borrowDTO.setId(uuid);
            borrowPanel.addBorrow(borrowDTO);
            new AlertDialog(this, "Thêm phiếu mượn thành công!").setVisible(true);
            dispose();
        }
    }
}