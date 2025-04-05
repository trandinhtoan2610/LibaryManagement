package GUI.Component.Panel;

import DTO.SupplierDTO;
import BUS.SupplierBUS;
import DTO.SupplierDTO;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeleteSupplierDialog;
import GUI.Component.Table.SupplierTable;
import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import GUI.Component.Button.ButtonAdd;
import GUI.Component.Button.ButtonDelete;
import GUI.Component.Button.ButtonUpdate;
import GUI.Component.Button.ButtonExportExcel;
import GUI.Component.Button.ButtonImportExcel;
import GUI.Component.Dialog.AddSupplierDialog;
import GUI.Component.Dialog.UpdateSupplierDialog;
import GUI.Component.Table.SupplierTable;import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SupplierPanel extends javax.swing.JPanel {

    public static SupplierBUS supplierBUS;
    Window parent = SwingUtilities.getWindowAncestor(this);
    private final AlertDialog fixSupplierAlert = new AlertDialog(parent, "Vui lòng chọn nhà cung cấp cần sửa!");
    private final AlertDialog deleteSupplierAlert = new AlertDialog(parent, "Vui lòng chọn nhà cung cấp cần xóa!");
    private JTextField txtSearchName; // Khai báo txtSearchName

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
            txtSearchName.getDocument().addDocumentListener(new DocumentListener() {
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

    public void loadTableFilter() {
        // Thêm logic lọc dữ liệu dựa trên txtSearchName.getText()
        // và cập nhật supplierTable1
    }

    private void initComponents() {
        buttonAdd = new GUI.Component.Button.ButtonAdd();
        buttonDelete = new GUI.Component.Button.ButtonDelete();
        buttonUpdate = new GUI.Component.Button.ButtonUpdate();
        buttonExportExcel = new GUI.Component.Button.ButtonExportExcel();
        buttonImportExcel1 = new GUI.Component.Button.ButtonImportExcel();
        jScrollPane1 = new javax.swing.JScrollPane();
        supplierTable1 = new GUI.Component.Table.SupplierTable();
        txtSearchName = new JTextField(); // Khởi tạo txtSearchName

        buttonAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonAddMouseClicked(evt);
            }
        });

        buttonDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonDeleteMouseClicked(evt);
            }
        });

        buttonUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonUpdateMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(supplierTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonExportExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonImportExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 370, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(txtSearchName)) // Thêm txtSearchName
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonExportExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonImportExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // Thêm txtSearchName
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
    }

//    private void buttonAddMouseClicked(java.awt.event.MouseEvent evt) {
//        AddUpdateSupplier addDialog = new AddUpdateSupplier((javax.swing.JFrame) SwingUtilities.getWindowAncestor(this), true, "add", null);
//        addDialog.setVisible(true);
//    }
    private void buttonAddMouseClicked(java.awt.event.MouseEvent evt) {
        AddSupplierDialog addDialog = new AddSupplierDialog((javax.swing.JFrame) SwingUtilities.getWindowAncestor(this), true);
        addDialog.setVisible(true);
}

    private void buttonDeleteMouseClicked(java.awt.event.MouseEvent evt) {
        SupplierDTO s = supplierTable1.getSelectedSupplier();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhà cung cấp này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                supplierTable1.deleteSupplier(s);
                JOptionPane.showMessageDialog(this, "Xóa nhà cung cấp thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void buttonUpdateMouseClicked(java.awt.event.MouseEvent evt) {
        if (supplierTable1.getSelectedSupplier() == null) {
            fixSupplierAlert.setVisible(true);
        } else {
            SupplierDTO supplier = supplierTable1.getSelectedSupplier();
            UpdateSupplierDialog updateDialog = new UpdateSupplierDialog(null, true, supplier);
            updateDialog.setVisible(true);
        }
    }

    private GUI.Component.Button.ButtonAdd buttonAdd;
    private GUI.Component.Button.ButtonDelete buttonDelete;
    private GUI.Component.Button.ButtonExportExcel buttonExportExcel;
    private GUI.Component.Button.ButtonImportExcel buttonImportExcel1;
    private GUI.Component.Button.ButtonUpdate buttonUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private GUI.Component.Table.SupplierTable supplierTable1;
}