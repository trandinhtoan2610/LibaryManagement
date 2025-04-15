/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.Component.Dialog;

import BUS.SupplierBUS;
import DTO.SupplierDTO;
import GUI.Component.Panel.SupplierPanel;
import GUI.Controller.Controller;

import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class AddSupplierDialog extends javax.swing.JDialog {

    private SupplierBUS supplierBUS;
    private SupplierPanel supplierPanel;

    /**
     * Creates new form AddSupplierDialog
     */
    public AddSupplierDialog(java.awt.Frame parent, boolean modal, SupplierPanel supplierPanel) {
        super(parent, modal);
        this.supplierBUS = new SupplierBUS();
        this.supplierPanel = supplierPanel;
        initComponents();
        setLocationRelativeTo(parent); // Hiển thị dialog ở giữa cửa sổ cha
    }
    private boolean fieldController() {
        if (txtSupplierName.getText().trim().isEmpty()) {
            AlertDialog blankNameAlert = new AlertDialog(this, "Vui lòng nhập tên nhà cung cấp ! ");
            blankNameAlert.setVisible(true);
            txtSupplierName.requestFocus();
            return false;
        }
    
        if (txtSupplierPhone.getText().trim().isEmpty()) {
            AlertDialog blankPhoneAlert = new AlertDialog(this, "Vui lòng nhập số điện thoại ! ");
            blankPhoneAlert.setVisible(true);
            txtSupplierPhone.requestFocus();
            return false;
        }
        if(!Controller.checkValidPhone(txtSupplierPhone.getText())){
            AlertDialog invalidPhoneAlert = new AlertDialog(this,"Vui lòng nhập số điện thoại hợp lệ ! ");
            invalidPhoneAlert.setVisible(true);
            txtSupplierPhone.requestFocus();
            return false;
        }
    
        if (txtSupplierAddress.getText().trim().isEmpty()) {
            AlertDialog blankAddressAlert = new AlertDialog(this, "Vui lòng nhập địa chỉ ! ");
            blankAddressAlert.setVisible(true);
            txtSupplierAddress.requestFocus();
            return false;
        }
        return true;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblSupplierID = new javax.swing.JLabel();
        lblSupplierPhone = new javax.swing.JLabel();
        lblSupplierName = new javax.swing.JLabel();
        lblSupplierAddress = new javax.swing.JLabel();
        txtSupplierID = new javax.swing.JTextField();
        txtSupplierPhone = new javax.swing.JTextField();
        txtSupplierAddress = new javax.swing.JTextField();
        txtSupplierName = new javax.swing.JTextField();
        btnAddSupplier = new javax.swing.JButton();
        btnCancelSupplier = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thêm nhà cung cấp"); 

        jPanel1.setBackground(new java.awt.Color(51, 204, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); 
        jLabel1.setText("Thêm nhà cung cấp");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(24, 24, 24))
        );

        lblSupplierID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSupplierID.setText("Mã nhà cung cấp");

        lblSupplierPhone.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSupplierPhone.setText("Số điện thoại");

        lblSupplierName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSupplierName.setText("Tên nhà cung cấp");

        lblSupplierAddress.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSupplierAddress.setText("Địa chỉ");

        btnAddSupplier.setBackground(new java.awt.Color(51, 255, 0));
        btnAddSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddSupplier.setText("Thêm");
        btnAddSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSupplierActionPerformed(evt);
            }
        });

        btnCancelSupplier.setBackground(new java.awt.Color(255, 51, 51));
        btnCancelSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCancelSupplier.setText("Hủy bỏ");
        btnCancelSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAddSupplier)
                        .addGap(73, 73, 73)
                        .addComponent(btnCancelSupplier))
                    .addComponent(txtSupplierPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupplierAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSupplierID)
                    .addComponent(txtSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSupplierName)
                        .addGap(24, 24, 24)
                        .addComponent(lblSupplierPhone)
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSupplierAddress)
                            .addComponent(txtSupplierAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSupplierPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddSupplier)
                    .addComponent(btnCancelSupplier))
                .addGap(43, 43, 43))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSupplierActionPerformed
        if(fieldController()){
            String supplierName = txtSupplierName.getText().trim();
            String supplierPhone = txtSupplierPhone.getText().trim();
            String supplierAddress = txtSupplierAddress.getText().trim();
    
            SupplierDTO s = new SupplierDTO(
                    null, 
                    supplierName,
                    supplierPhone,
                    supplierAddress
            );
    
            supplierBUS.addSupplier(s);
    
            AlertDialog addSupplierSuccess = new AlertDialog(this, "Thêm nhà cung cấp thành công !");
            addSupplierSuccess.setVisible(true);
            supplierPanel.reloadSupplierTable();
            this.dispose();
    
            // AddSupplierDialog();
        }
    }
    private void btnCancelSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelSupplierActionPerformed
        dispose(); // Đóng dialog khi nhấn nút Hủy bỏ
    }//GEN-LAST:event_btnCancelSupplierActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddSupplierDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddSupplierDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddSupplierDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddSupplierDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddSupplierDialog dialog = new AddSupplierDialog(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddSupplier;
    private javax.swing.JButton btnCancelSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblSupplierAddress;
    private javax.swing.JLabel lblSupplierID;
    private javax.swing.JLabel lblSupplierName;
    private javax.swing.JLabel lblSupplierPhone;
    private javax.swing.JTextField txtSupplierAddress;
    private javax.swing.JTextField txtSupplierID;
    private javax.swing.JTextField txtSupplierName;
    private javax.swing.JTextField txtSupplierPhone;
    // End of variables declaration//GEN-END:variables
}