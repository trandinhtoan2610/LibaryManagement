package GUI.Component.Dialog;

import BUS.*;
import DTO.*;
import DTO.Enum.PurchaseStatus;
import DTO.Enum.Status;
import GUI.Component.Button.ButtonChosen;
import GUI.Component.Button.ButtonIcon;
import GUI.Component.Panel.BookPanel;
import GUI.Component.Panel.PurchaseOrderPanel;
import GUI.Component.Panel.Statistics.Components.EventBusManager;
import GUI.Component.Panel.Statistics.Components.PurchaseChangeEvent;
import GUI.Component.Table.PurchaseOrderDetailsTable;
import GUI.Component.Table.PurchaseOrderTable;
import GUI.Component.TextField.CustomTextField;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdatePurchaseOrderDialog extends JDialog {
    private PurchaseOrderBUS purchaseOrderBUS = new PurchaseOrderBUS();
    private PurchaseOrderDetailBUS purchaseOrderDetailBUS = new PurchaseOrderDetailBUS();
    private SupplierBUS supplierBUS = new SupplierBUS();
    private EmployeeBUS employeeBUS = new EmployeeBUS();
    private BookBUS bookBUS = new BookBUS();

    private JLabel supplierLabel;
    private JLabel supplierNameLabel;
    private JLabel supplierPhoneLabel;
    private JLabel supplierAddressLabel;

    private JLabel employeeLabel;
    private JLabel employeeNameLabel;

    private CustomTextField supplierField;
    private CustomTextField employeeField;
    private ButtonChosen supplierChosen;
    private ButtonChosen employeeChosen;

    private JLabel bookLabel;
    private PurchaseOrderDetailsTable purchaseOrderDetailsTable;

    private JButton addDetailButton;
    private JButton editDetailButton;
    private JButton deleteDetailButton;

    private JLabel buyDateLabel;
    private JLabel statusLabel;
    private JLabel statusValueLabel;
    private JLabel totalAmountLabel;
    private JLabel totalAmountValueLabel;

    private JDateChooser buyDateChooser;
    private JComboBox<String> statusComboBox;

    private SupplierDTO currentSupplier;
    private Employee currentEmployee;
    private PurchaseOrderPanel purchaseOrderPanel;
    private PurchaseOrderDTO currentPurchaseOrder;
    private List<PurchaseOrderDetailDTO> pendingOrderDetails = new ArrayList<>();
    private PurchaseOrderTable purchaseOrderTable;

    // Constructor 1: 
    public UpdatePurchaseOrderDialog(JFrame parent, PurchaseOrderPanel purchaseOrderPanel, PurchaseOrderDTO purchaseOrder) {
        super(parent, "Cập Nhật Phiếu Nhập", true); 
        this.purchaseOrderPanel = purchaseOrderPanel; // Lưu trữ PurchaseOrderPanel để sau này gọi reloadPurchaseOrderTable()
        this.currentPurchaseOrder = purchaseOrder; // Lưu trữ phiếu nhập đang được cập nhật
        this.pendingOrderDetails = purchaseOrderDetailBUS.getPurchaseOrderDetailsByOrderId(purchaseOrder.getId());
        initComponents(); // Khởi tạo các thành phần giao diện
        loadPurchaseOrderData(); // Tải dữ liệu của phiếu nhập vào các thành phần giao diện
        setSize(800, 700); // Đặt kích thước của dialog
        setLocationRelativeTo(parent); // Đặt vị trí của dialog ở giữa cửa sổ cha
    }

    // Constructor 2: Không tham số
    public UpdatePurchaseOrderDialog(JFrame parent) {
        super(parent, "Cập Nhật Phiếu Nhập", true);
        initComponents();
        setSize(800, 700);
        setLocationRelativeTo(parent);
    }

    // Constructor 3:  Có PurchaseOrderPanel
    public UpdatePurchaseOrderDialog(JFrame parent, PurchaseOrderPanel purchaseOrderPanel) {
        super(parent, "Cập Nhật Phiếu Nhập", true);
        this.purchaseOrderPanel = purchaseOrderPanel;
        initComponents();
        setSize(800, 700);
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

        JLabel title = new JLabel("Cập Nhật Phiếu Nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        // Panel for supplier information
        JPanel supplierPanel = new JPanel(new BorderLayout(5, 5));
        supplierLabel = new JLabel("Mã nhà cung cấp:");
        supplierField = new CustomTextField();
        supplierField.setPreferredSize(new Dimension(100, 30));
        supplierChosen = new ButtonChosen();
        supplierChosen.addActionListener(e -> chooseSupplier());
        supplierField.setEditable(false); // Không cho phép sửa trực tiếp mã NCC

        supplierField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showSupplierInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showSupplierInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showSupplierInfo();
            }
        });

        JPanel supplierInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        supplierInputPanel.add(supplierLabel);
        supplierInputPanel.add(supplierField);
        supplierInputPanel.add(supplierChosen);

        JPanel supplierDetailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        supplierNameLabel = new JLabel("           Tên Nhà cung cấp: ");
        supplierPhoneLabel = new JLabel("           Điện thoại: ");
        supplierAddressLabel = new JLabel("           Địa chỉ: ");
        supplierDetailsPanel.add(supplierNameLabel);
        supplierDetailsPanel.add(supplierPhoneLabel);
        supplierDetailsPanel.add(supplierAddressLabel);

        supplierPanel.add(supplierInputPanel, BorderLayout.NORTH);
        supplierPanel.add(supplierDetailsPanel, BorderLayout.CENTER);

        // Panel for employee information
        JPanel employeePanel = new JPanel(new BorderLayout(5, 5));
        employeeLabel = new JLabel("Nhân viên (Mã NV):");
        employeeField = new CustomTextField();
        employeeField.setPreferredSize(new Dimension(100, 30));
        employeeChosen = new ButtonChosen();
        employeeChosen.addActionListener(e -> chooseEmployee());
        employeeField.setEditable(false); // Không cho phép sửa trực tiếp mã NV

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
        employeeNameLabel = new JLabel("           Tên NV: ");
        employeeDetailsPanel.add(employeeNameLabel);

        employeePanel.add(employeeInputPanel, BorderLayout.NORTH);
        employeePanel.add(employeeDetailsPanel, BorderLayout.CENTER);

        // Panel for order details
        JPanel detailsPanel = new JPanel(new BorderLayout(5, 5));
        bookLabel = new JLabel("Chi Tiết Phiếu Nhập:");
        detailsPanel.add(bookLabel, BorderLayout.NORTH);

        purchaseOrderDetailsTable = new PurchaseOrderDetailsTable();
        JScrollPane scrollPane = new JScrollPane(purchaseOrderDetailsTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel detailButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        addDetailButton = new ButtonIcon("/icons/addbook.svg");
        addDetailButton.addActionListener(e -> addOrderDetail());
        deleteDetailButton = new ButtonIcon("/icons/deleteDetails.svg");
        deleteDetailButton.addActionListener(e -> deleteOrderDetails());
        editDetailButton = new ButtonIcon("/icons/editDetails.svg");
        editDetailButton.setEnabled(false);
        editDetailButton.addActionListener(e -> UpdateOrderDetails());

        purchaseOrderDetailsTable.getSelectionModel().addListSelectionListener(e -> {
            editDetailButton.setEnabled(purchaseOrderDetailsTable.getSelectedRow() != -1);
        });

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(250, 0));
        totalAmountLabel = new JLabel("Tổng tiền:");
        totalAmountValueLabel = new JLabel("0 VND");

        detailButtonPanel.add(addDetailButton);
        detailButtonPanel.add(editDetailButton);
        detailButtonPanel.add(deleteDetailButton);
        detailButtonPanel.add(emptyPanel);
        detailButtonPanel.add(totalAmountLabel);
        detailButtonPanel.add(totalAmountValueLabel);
        detailsPanel.add(scrollPane, BorderLayout.CENTER);
        detailsPanel.add(detailButtonPanel, BorderLayout.SOUTH);

        // Panel for date and status
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buyDateLabel = new JLabel("Ngày nhập:");
        buyDateChooser = new JDateChooser();
        buyDateChooser.setDateFormatString("dd/MM/yyyy");

        statusLabel = new JLabel("Trạng thái:");
        statusComboBox = new JComboBox<>(new String[]{"Đang_Chờ", "Hoàn_Thành", "Đã_Hủy"});
        if (currentPurchaseOrder.getStatus() == PurchaseStatus.Hoàn_Thành){
            statusComboBox.setSelectedItem("Hoàn_Thành");
            statusComboBox.setEnabled(false);
        } else if (currentPurchaseOrder.getStatus() == PurchaseStatus.Đã_Hủy) {
            statusComboBox.setSelectedItem("Đã_Hủy");
            statusComboBox.setEnabled(false);
        } else if (currentPurchaseOrder.getStatus() == PurchaseStatus.Đang_Chờ) {
            statusComboBox.setSelectedItem("Đang_Chờ");
        }
        infoPanel.add(buyDateLabel);
        infoPanel.add(buyDateChooser);
        infoPanel.add(statusLabel);
        infoPanel.add(statusComboBox);

        mainContentPanel.add(supplierPanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(employeePanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(detailsPanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(infoPanel);

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

        JButton updateButton = new JButton("Cập nhật");
        updateButton.setPreferredSize(new Dimension(120, 30));
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(e -> updatePurchaseOrder());

        panel.add(cancelButton);
        panel.add(updateButton);
        return panel;
    }

    private void loadPurchaseOrderData() {
        if (currentPurchaseOrder != null) {
            supplierField.setText(currentPurchaseOrder.getSupplierId());
            employeeField.setText(String.valueOf(currentPurchaseOrder.getEmployeeId()));
            buyDateChooser.setDate(currentPurchaseOrder.getBuyDate());
            statusComboBox.setSelectedItem(currentPurchaseOrder.getStatus().toString().replace("_", " "));

            showSupplierInfo();
            showEmployeeInfo();

            try {
                purchaseOrderDetailsTable.setPurchaseOrderDetails(pendingOrderDetails);
                purchaseOrderDetailsTable.refreshTable();
                updateTotalAmount();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết phiếu nhập: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showSupplierInfo() {
        String supplierId = supplierField.getText();
        if (supplierId.isEmpty()) {
            supplierNameLabel.setText("     Tên nhà cung cấp: ");
            supplierPhoneLabel.setText("     Điện thoại: ");
            supplierAddressLabel.setText("     Địa chỉ: ");
            return;
        }
        try {
            currentSupplier = supplierBUS.getSupplierById(supplierId);
            if (currentSupplier != null) {
                supplierNameLabel.setText("     Tên nhà cung cấp: " + currentSupplier.getName());
                supplierPhoneLabel.setText("     Điện thoại: " + currentSupplier.getPhone());
                supplierAddressLabel.setText("     Địa chỉ: " + currentSupplier.getAddress());
            } else {
                supplierNameLabel.setText("     Tên nhà cung cấp: ");
                supplierPhoneLabel.setText("     Điện thoại: ");
                supplierAddressLabel.setText("     Địa chỉ: ");
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin nhà cung cấp: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEmployeeInfo() {
        String employeeId = employeeField.getText();
        if (employeeId.isEmpty()) {
            employeeNameLabel.setText("     Tên NV: ");
            return;
        }
        try {
            currentEmployee = employeeBUS.getEmployeeById(Long.parseLong(employeeId));
            if (currentEmployee != null) {
                employeeNameLabel.setText("     Tên NV: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName());
            } else {
                employeeNameLabel.setText("     Tên NV: ");
            }
        } catch (NumberFormatException e) {
            employeeNameLabel.setText("     Tên NV: ");
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chooseSupplier() {
        ChooseSupplierDialog chooseSupplierDialog = new ChooseSupplierDialog(this);
        chooseSupplierDialog.setVisible(true);
        if (chooseSupplierDialog.getSelectedSupplier() != null) {
            currentSupplier = chooseSupplierDialog.getSelectedSupplier();
            supplierField.setText(currentSupplier.getId());
            showSupplierInfo();
        }
    }

    private void chooseEmployee() {
        ChooseEmployeeDialog chooseEmployeeDialog = new ChooseEmployeeDialog(this);
        chooseEmployeeDialog.setVisible(true);
        if (chooseEmployeeDialog.getSelectedEmployee() != null) {
            currentEmployee = chooseEmployeeDialog.getSelectedEmployee();
            employeeField.setText(currentEmployee.getId().toString());
            showEmployeeInfo();
        }
    }

    private void addOrderDetail() {
        AddPurchaseOrderDetailsDialog addDetailDialog = new AddPurchaseOrderDetailsDialog(this,0L);
        addDetailDialog.setVisible(true);
        if (addDetailDialog.getCurrentOrderDetail() != null) {
            PurchaseOrderDetailDTO newDetail = addDetailDialog.getCurrentOrderDetail();
            for (PurchaseOrderDetailDTO detail : pendingOrderDetails) {
                if (detail.getBookId() == newDetail.getBookId()) {
                    detail.setQuantity(detail.getQuantity() + newDetail.getQuantity());
                    BigDecimal subTotal = detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity()));
                    detail.setSubTotal(subTotal);
                    purchaseOrderDetailsTable.updatePurchaseOrderDetails(detail);
                    purchaseOrderDetailsTable.refreshTable();
                    updateTotalAmount();
                    return;
                }
            }
            pendingOrderDetails.add(newDetail);
            purchaseOrderDetailsTable.setPurchaseOrderDetails(pendingOrderDetails);
            purchaseOrderDetailsTable.refreshTable();
            updateTotalAmount();
        }
    }


    private void UpdateOrderDetails() {
        int selectedRow = purchaseOrderDetailsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi tiết để sửa", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PurchaseOrderDetailDTO selectedDetail = pendingOrderDetails.get(selectedRow);
        UpdatePurchaseOrderDetail updateDialog = new UpdatePurchaseOrderDetail(this, selectedDetail);
        updateDialog.setVisible(true);

        if (updateDialog.getCurrentOrderDetail() != null) {
            pendingOrderDetails.set(selectedRow, updateDialog.getCurrentOrderDetail());
            purchaseOrderDetailsTable.setPurchaseOrderDetails(pendingOrderDetails);
            purchaseOrderDetailsTable.refreshTable();
            updateTotalAmount();
        }
    }

    private void deleteOrderDetails() {
        int selectedRow = purchaseOrderDetailsTable.getSelectedRow();

        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa chi tiết đã chọn?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                pendingOrderDetails.remove(selectedRow);
                purchaseOrderDetailsTable.setPurchaseOrderDetails(pendingOrderDetails);
                purchaseOrderDetailsTable.refreshTable();
                updateTotalAmount();

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa chi tiết thành công",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một chi tiết để xóa",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void updateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseOrderDetailDTO detail : pendingOrderDetails) {
            total = total.add(detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
        }
        totalAmountValueLabel.setText(total.toString() + " VND");
    }

    private boolean validateInput() {
        if (supplierField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (employeeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (buyDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhập hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (pendingOrderDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập chi tiết phiếu nhập", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void updatePurchaseOrder() {
        if (!validateInput()) return;

        try {
            currentPurchaseOrder.setSupplierId(supplierField.getText());
            currentPurchaseOrder.setEmployeeId(Long.parseLong(employeeField.getText()));
            currentPurchaseOrder.setBuyDate(buyDateChooser.getDate());
            currentPurchaseOrder.setStatus(PurchaseStatus.valueOf(statusComboBox.getSelectedItem().toString().replace(" ", "_")));
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (PurchaseOrderDetailDTO detail : pendingOrderDetails) {
                BigDecimal subTotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
                detail.setSubTotal(subTotal);
                totalAmount = totalAmount.add(subTotal);

                if (detail.getPurchaseOrderId() == 0) {
                    detail.setPurchaseOrderId(currentPurchaseOrder.getId());
                    purchaseOrderDetailBUS.addPurchaseOrderDetail(detail);
                } else {
                    boolean updated = purchaseOrderDetailBUS.updatePurchaseOrderDetail(detail);
                    if (!updated) {
                        throw new Exception("Không thể cập nhật chi tiết");
                    }
                }
            }
            //tang so luong sach
            if (currentPurchaseOrder.getStatus() == PurchaseStatus.Hoàn_Thành) {
                for (PurchaseOrderDetailDTO detail : pendingOrderDetails) {
                    Book book = bookBUS.getBookById(detail.getBookId());
                    if (book != null) {
                        book.setQuantity(book.getQuantity() + detail.getQuantity());
                        bookBUS.updateBook(book);
                    }
                }
                BookPanel.loadData();
            }
            currentPurchaseOrder.setTotalAmount(totalAmount);
            boolean orderUpdated = purchaseOrderBUS.updatePurchaseOrder(currentPurchaseOrder);
            if (!orderUpdated) {
                throw new Exception("Không thể cập nhật thông tin phiếu nhập");
            }
            purchaseOrderPanel.updatePurchaseOrder(currentPurchaseOrder);
            JOptionPane.showMessageDialog(this, "Cập nhật phiếu nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            EventBusManager.getEventBus().post(new PurchaseChangeEvent());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phiếu nhập: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}

