package GUI.Component.Dialog;

import BUS.SupplierBUS;
import DTO.SupplierDTO;
import GUI.Component.Panel.SupplierPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

public class DeleteSupplierDialog extends java.awt.Dialog {
    SupplierBUS supplierBUS;
    SupplierDTO supplierToDelete;
    SupplierPanel supplierPanel;

    public DeleteSupplierDialog(java.awt.Frame parent, boolean modal, SupplierDTO s, SupplierPanel p) {
        supplierBUS = new SupplierBUS();
        this.supplierToDelete = s;
        this.supplierPanel = p;
        super(parent, modal);
        initComponents();

        setMinimumSize(new Dimension(388, 188));
        setMaximumSize(new Dimension(388, 188));
        setLocationRelativeTo(null);
        setTitle("Xác nhận xóa nhà cung cấp");
        setResizable(false);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnDeleteSupplier = new javax.swing.JButton();
        btnCancelSupplier = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Xóa nhà cung cấp");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Bạn có muốn xóa nhà cung cấp?");

        btnDeleteSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSupplier.setText("Xóa");
        btnDeleteSupplier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        btnDeleteSupplier.addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent evt) {
                btnDeleteMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                btnDeleteMouseExited(evt);
            }
            public void mouseClicked(MouseEvent evt) {
            }
            public void mousePressed(MouseEvent evt) {
            }
            public void mouseReleased(MouseEvent evt) {
            }
        });

        btnCancelSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCancelSupplier.setText("Hủy bỏ");
        btnCancelSupplier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        btnCancelSupplier.addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
            public void mouseClicked(MouseEvent evt) {
            }
            public void mousePressed(MouseEvent evt) {
            }
            public void mouseReleased(MouseEvent evt) {
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(btnDeleteSupplier)
                        .addGap(56, 56, 56)
                        .addComponent(btnCancelSupplier))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteSupplier)
                    .addComponent(btnCancelSupplier))
                .addGap(0, 84, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void btnDeleteActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        boolean deleted = supplierBUS.deleteSupplier(supplierToDelete);
        if (deleted) {
            supplierPanel.reloadSupplierTable();
            AlertDialog successDeleteAlert = new AlertDialog(this, "Xóa nhà cung cấp thành công !");
            this.dispose();
            successDeleteAlert.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhà cung cấp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnCancelActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteMouseEntered(MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseEntered
        btnDeleteSupplier.setBackground(new Color(139, 0, 0));
    }//GEN-LAST:event_btnDeleteMouseEntered

    private void btnDeleteMouseExited(MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseExited
        btnDeleteSupplier.setBackground(new Color(255,0,51));
    }//GEN-LAST:event_btnDeleteMouseExited

    private void btnCancelMouseExited(MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancelSupplier.setBackground(new Color (0,204,51));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnCancelMouseEntered(MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancelSupplier.setBackground(new Color(0, 100, 0));
    }//GEN-LAST:event_btnCancelMouseEntered




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelSupplier;
    private javax.swing.JButton btnDeleteSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}