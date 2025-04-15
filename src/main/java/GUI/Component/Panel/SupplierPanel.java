package GUI.Component.Panel;

import BUS.SupplierBUS;
import DTO.SupplierDTO;
import GUI.Component.Dialog.AddSupplierDialog;
import GUI.Component.Dialog.UpdateSupplierDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeleteSupplierDialog;
import GUI.Component.Table.SupplierTable;
import GUI.Component.TextField.RoundedTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import GUI.Component.Button.ButtonAdd;
import GUI.Component.Button.ButtonDelete;
import GUI.Component.Button.ButtonUpdate;
import GUI.Component.Button.ButtonExportExcel;
import GUI.Component.Button.ButtonImportExcel;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SupplierPanel extends javax.swing.JPanel {

    public static SupplierBUS supplierBUS;
    Window parent = SwingUtilities.getWindowAncestor(this);
    private final AlertDialog fixSupplierAlert = new AlertDialog(parent, "Vui lòng chọn nhà cung cấp cần sửa!");
    private final AlertDialog deleteSupplierAlert = new AlertDialog(parent, "Vui lòng chọn nhà cung cấp cần xóa!");
    private JTextField txtSearchSupplierName; 

    public SupplierPanel() {
        try {
            initComponents();
            supplierBUS = new SupplierBUS();
            if (supplierBUS.supplierList == null) {
                System.err.println("Lỗi: supplierList là null, khởi tạo danh sách rỗng.");
            } else {
                System.out.println("Số lượng nhà cung cấp: " + supplierBUS.supplierList.size());
            }

            reloadSupplierTable();
            txtSearchSupplierName.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    loadTableFilter();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    loadTableFilter();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    loadTableFilter();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void reloadSupplierTable() {  
        supplierTable1.resetTable();
    }
    // public void reloadSupplierTable() {
    //     supplierTable1.resetTable();
    //     supplierTable1.loadData(supplierBUS.getAllSuppliers());
    // }

    public void loadTableFilter() {
        String searchText = txtSearchSupplierName.getText();
        List<SupplierDTO> filteredList = supplierBUS.searchSuppliersByAll(searchText);
        supplierTable1.loadData(filteredList);
    }

    private void initComponents() {
        navbarSupplierPanel = new javax.swing.JPanel();
        leftNavbarSupplierPanel = new javax.swing.JPanel();
        buttonAdd = new GUI.Component.Button.ButtonAdd();
        buttonDelete = new GUI.Component.Button.ButtonDelete();
        buttonUpdate = new GUI.Component.Button.ButtonUpdate();
        buttonExportExcel = new GUI.Component.Button.ButtonExportExcel();
        buttonImportExcel1 = new GUI.Component.Button.ButtonImportExcel();
        rightNavbarSupplierPanel = new javax.swing.JPanel();
        headerSearchPanel = new javax.swing.JPanel();
        lblSearchTitle = new javax.swing.JLabel();
        bodySearchPanel = new javax.swing.JPanel();
        lblSearchByName = new javax.swing.JLabel();
        txtSearchSupplierName = new JTextField(); // Khởi tạo txtSearchSupplierName
        footerSearchPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        supplierTable1 = new GUI.Component.Table.SupplierTable();

        setLayout(new java.awt.BorderLayout());

        navbarSupplierPanel.setBackground(new java.awt.Color(255, 255, 255));
        navbarSupplierPanel.setLayout(new java.awt.BorderLayout(5, 5));

        leftNavbarSupplierPanel.setBackground(new java.awt.Color(255, 255, 255));
        leftNavbarSupplierPanel.setLayout(new java.awt.GridLayout(1, 0, 5, 0));
        buttonAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonAddMouseClicked(evt);
            }
        });
        leftNavbarSupplierPanel.add(buttonAdd);

        buttonDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonDeleteMouseClicked(evt);
            }
        });
        leftNavbarSupplierPanel.add(buttonDelete);

        buttonUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonUpdateMouseClicked(evt);
            }
        });
        leftNavbarSupplierPanel.add(buttonUpdate);

        leftNavbarSupplierPanel.add(buttonExportExcel);
        leftNavbarSupplierPanel.add(buttonImportExcel1);
        navbarSupplierPanel.add(leftNavbarSupplierPanel, java.awt.BorderLayout.LINE_START);

        rightNavbarSupplierPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightNavbarSupplierPanel.setLayout(new java.awt.GridLayout(3, 0));

        headerSearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerSearchPanel.setLayout(new java.awt.BorderLayout());

        lblSearchTitle.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblSearchTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearchTitle.setText("Tìm kiếm ");
        headerSearchPanel.add(lblSearchTitle, java.awt.BorderLayout.CENTER);
        rightNavbarSupplierPanel.add(headerSearchPanel);

        bodySearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        bodySearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        lblSearchByName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSearchByName.setText("Nhập thông tin tìm kiếm :"); // Cập nhật nhãn
        lblSearchByName.setPreferredSize(new java.awt.Dimension(150, 30)); // Tăng kích thước nhãn
        bodySearchPanel.add(lblSearchByName);

        txtSearchSupplierName.setPreferredSize(new java.awt.Dimension(300, 30));
        bodySearchPanel.add(txtSearchSupplierName);
        rightNavbarSupplierPanel.add(bodySearchPanel);

        footerSearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightNavbarSupplierPanel.add(footerSearchPanel);

        navbarSupplierPanel.add(rightNavbarSupplierPanel, java.awt.BorderLayout.LINE_END);

        add(navbarSupplierPanel, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setViewportView(supplierTable1);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddMouseClicked(java.awt.event.MouseEvent evt) {
        AddSupplierDialog addDialog = new AddSupplierDialog((javax.swing.JFrame) SwingUtilities.getWindowAncestor(this), true, (this));
        addDialog.setVisible(true);
    }

    private void buttonDeleteMouseClicked(java.awt.event.MouseEvent evt) {
        SupplierDTO selectedSupplier = supplierTable1.getSelectedSupplier();
        if (selectedSupplier != null) {
            DeleteSupplierDialog deleteDialog = new DeleteSupplierDialog(
                (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this),  true,  selectedSupplier,this );
            deleteDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buttonUpdateMouseClicked(java.awt.event.MouseEvent evt) {
        SupplierDTO selectedSupplier = supplierTable1.getSelectedSupplier();
        if (selectedSupplier == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            UpdateSupplierDialog updateDialog = new UpdateSupplierDialog((javax.swing.JFrame) SwingUtilities.getWindowAncestor(this), this,selectedSupplier );
            updateDialog.setVisible(true);
        }
    }
    

    private GUI.Component.Button.ButtonAdd buttonAdd;
    private GUI.Component.Button.ButtonDelete buttonDelete;
    private GUI.Component.Button.ButtonExportExcel buttonExportExcel;
    private GUI.Component.Button.ButtonImportExcel buttonImportExcel1;
    private GUI.Component.Button.ButtonUpdate buttonUpdate;
    private javax.swing.JPanel bodySearchPanel;
    private javax.swing.JPanel footerSearchPanel;
    private javax.swing.JPanel headerSearchPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSearchByName;
    private javax.swing.JLabel lblSearchTitle;
    private javax.swing.JPanel leftNavbarSupplierPanel;
    private javax.swing.JPanel navbarSupplierPanel;
    private javax.swing.JPanel rightNavbarSupplierPanel;
    private GUI.Component.Table.SupplierTable supplierTable1;
}