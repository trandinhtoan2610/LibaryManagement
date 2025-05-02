package GUI.Component.Panel;

import DTO.PurchaseOrderDTO;
import DTO.SupplierDTO;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddPurchaseOrderDialog;
import GUI.Component.Dialog.DeletePurchaseOrderDialog;
import GUI.Component.Dialog.UpdatePurchaseOrdersDialog;
import GUI.Component.Table.PurchaseOrderDetailsTable;
import GUI.Component.Table.PurchaseOrderTable;

import BUS.PurchaseOrderBUS;
import DTO.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.TableColumnModel;

public class PurchaseOrderPanel extends JPanel {
    private final PurchaseOrderTable purchaseOrderTable = new PurchaseOrderTable();
    private final PurchaseOrderBUS purchaseOrderBUS = new PurchaseOrderBUS();

    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private JPanel searchNavBarLabel;

    private JPanel supplierPanel;
    private JPanel employeePanel;

    private PurchaseOrderDetailsTable purchaseOrderDetailsTable;

    private JFrame parentFrame;

    public PurchaseOrderPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(0, 5));

        // Nút điều khiển và thanh công cụ
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);

        // Table phiếu nhập
        JScrollPane scrollPane = new JScrollPane(purchaseOrderTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel thông tin chi tiết
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        JPanel infoPanel = employeeNsupplierPanel(new Employee(1L, "Hoàng", "Quý"),
                new SupplierDTO("NCC1", "Thanh Hóa", "0987654321", "Hà Nội"));
        infoPanel.setPreferredSize(new Dimension(400, 150));

        purchaseOrderDetailsTable = new PurchaseOrderDetailsTable();
        customizeDetailTable();
        bottomPanel.setBackground(new Color(240, 240, 240));
        JScrollPane detailScrollPane = new JScrollPane(purchaseOrderDetailsTable);
        detailScrollPane.setPreferredSize(new Dimension(300, 180));
        purchaseOrderDetailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        detailScrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));
        bottomPanel.setPreferredSize(new Dimension(0, 220));
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(detailScrollPane, BorderLayout.CENTER);

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        paddedPanel.add(bottomPanel, BorderLayout.CENTER);
        paddedPanel.setBackground(new Color(240, 240, 240));
        this.add(paddedPanel, BorderLayout.SOUTH);

        // Đọc tất cả các phiếu nhập từ BUS và thiết lập bảng
        List<PurchaseOrderDTO> list = purchaseOrderBUS.getAllPurchaseOrders();
        purchaseOrderTable.setPurchaseOrderDTOS(list);
    }

    private void customizeDetailTable() {
        // Đặt kích thước cột
        TableColumnModel columnModel = purchaseOrderDetailsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Cột 1
        columnModel.getColumn(1).setPreferredWidth(100); // Cột 2
        columnModel.getColumn(2).setPreferredWidth(100); // Cột 3
        // Đặt font và chiều cao
        purchaseOrderDetailsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        purchaseOrderDetailsTable.setRowHeight(25);
        purchaseOrderDetailsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Tắt tự động resize
        purchaseOrderDetailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);

        // Thêm button Add
        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddPurchaseOrderDialog addDialog = new AddPurchaseOrderDialog(parentFrame, PurchaseOrderPanel.this);
                addDialog.setVisible(true);
            }
        });

        // Thêm button Update
        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                UpdatePurchaseOrderDialog updateDialog = new UpdatePurchaseOrderDialog(parentFrame, PurchaseOrderPanel.this);
                updateDialog.setVisible(true);
            }
        });

        // Thêm button Delete
        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                DeletePurchaseOrderDialog deleteDialog = new DeletePurchaseOrderDialog(parentFrame, "Xóa phiếu nhập", true);
                deleteDialog.setVisible(true);
            }
        });

        // Các button Export Excel, Import Excel
        buttonExportExcel = new ButtonExportExcel();
        buttonImportExcel = new ButtonImportExcel();
        searchNavBarLabel = new JPanel();

        // Thêm vào panel
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonExportExcel);
        buttonPanel.add(buttonImportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private JPanel employeeNsupplierPanel(Employee employee, SupplierDTO supplierDTO) {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel nhân viên
        employeePanel = new JPanel(new GridLayout(2, 1));
        employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));
        employeePanel.setBackground(Color.WHITE);
        employeePanel.setPreferredSize(new Dimension(350, 80));
        employeePanel.add(createStyledLabel("Mã NV: " + employee.getId()));
        employeePanel.add(createStyledLabel("Họ tên: " + employee.getFirstName() + " " + employee.getLastName()));

        gbc.gridy = 0;
        gbc.weighty = 0.40;
        container.add(employeePanel, gbc);

        // Panel nhà cung cấp
        supplierPanel = new JPanel(new GridLayout(5, 1));
        supplierPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhà cung cấp"));
        supplierPanel.setBackground(Color.WHITE);
        supplierPanel.setPreferredSize(new Dimension(350, 120));
        supplierPanel.add(createStyledLabel("Mã Nhà Cung Cấp: " + supplierDTO.getId()));
        supplierPanel.add(createStyledLabel("Tên Nhà Cung Cấp: " + supplierDTO.getName()));
        supplierPanel.add(createStyledLabel("Điện thoại: " + supplierDTO.getPhone()));
        supplierPanel.add(createStyledLabel("Địa chỉ: " + supplierDTO.getAddress()));

        gbc.gridy = 1;
        gbc.weighty = 0.60;
        container.add(supplierPanel, gbc);

        return container;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return label;
    }
}
