/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/AWTForms/Dialog.java to edit this template
 */
package GUI.Component.Dialog;

import BUS.AuthorBUS;
import DAL.AuthorDAL;
import DTO.AuthorDTO;
import GUI.Component.Panel.AuthorPanel;
import GUI.Component.Panel.ReaderPanel;
import GUI.Controller.Controller;
import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author keika
 */
public class UpdateAuthorDialog extends java.awt.Dialog {
    private final Color darkYellow = new Color(204, 153, 0);
    private final Color red = new Color(178,34,34);
    AuthorBUS authorBUS = new AuthorBUS();
    AuthorPanel authorPanel;

    private AuthorDTO authorUpdate;
    
    public UpdateAuthorDialog(java.awt.Frame parent, boolean modal, AuthorDTO author, AuthorPanel ap) {
        super(parent, modal);
        this.authorUpdate = author;
        this.authorPanel = ap;
        initComponents();
        setField();
        setMinimumSize(new Dimension(482, 270)); 
        setMaximumSize(new Dimension(482, 270)); 
        setTitle("Cập nhật tác giả");
        setResizable(false); 
        setLocationRelativeTo(null);
    }
    
    public boolean fieldControl(){
        String name = txtFullName.getText();
        if(name == ""){
            AlertDialog blankNameAlert = new AlertDialog(this,"Vui lòng nhập tên tác giả !");
            blankNameAlert.setVisible(true);
            return false;
        }
        if(!Controller.checkValidName(name)){
            AlertDialog invalidNameAlert = new AlertDialog(this,"Vui lòng nhập tên hợp lệ !");
            invalidNameAlert.setVisible(true);
            return false;
        }
        
        return true;
        
    }

    private void setField(){
        String id = String.valueOf(authorUpdate.getId());
        txtAuthorID.setText(id);
        String fullName = Controller.formatFullName(authorUpdate.getLastName()+ ' ' + authorUpdate.getFirstName());
        txtFullName.setText(fullName);        
        txtFullName.requestFocus();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlFooter = new javax.swing.JPanel();
        btnUpdate = new javax.swing.JButton();
        btnCancle = new javax.swing.JButton();
        pnlBody = new javax.swing.JPanel();
        lblAuthorID = new javax.swing.JLabel();
        txtAuthorID = new javax.swing.JTextField();
        lblAuthorName = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlHeader.setBackground(new java.awt.Color(255, 204, 0));
        pnlHeader.setPreferredSize(new java.awt.Dimension(100, 50));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SỬA THÔNG TIN TÁC GIẢ");
        pnlHeader.add(jLabel1, java.awt.BorderLayout.CENTER);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlFooter.setBackground(new java.awt.Color(255, 255, 255));
        pnlFooter.setPreferredSize(new java.awt.Dimension(100, 50));

        btnUpdate.setBackground(new java.awt.Color(255, 215, 47));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Cập nhật");
        btnUpdate.setPreferredSize(new java.awt.Dimension(80, 35));
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUpdateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUpdateMouseExited(evt);
            }
        });
        pnlFooter.add(btnUpdate);

        btnCancle.setBackground(new java.awt.Color(255, 102, 102));
        btnCancle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancle.setForeground(new java.awt.Color(255, 255, 255));
        btnCancle.setText("Hủy bỏ");
        btnCancle.setPreferredSize(new java.awt.Dimension(90, 35));
        btnCancle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancleMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancleMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancleMouseExited(evt);
            }
        });
        pnlFooter.add(btnCancle);

        add(pnlFooter, java.awt.BorderLayout.SOUTH);

        pnlBody.setBackground(new java.awt.Color(255, 255, 255));

        lblAuthorID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblAuthorID.setText("Mã tác giả ");

        txtAuthorID.setEditable(false);

        lblAuthorName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblAuthorName.setText("Họ và tên ");

        javax.swing.GroupLayout pnlBodyLayout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(pnlBodyLayout);
        pnlBodyLayout.setHorizontalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAuthorID, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAuthorID, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAuthorName, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );
        pnlBodyLayout.setVerticalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAuthorID, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAuthorID, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblAuthorName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        add(pnlBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void btnUpdateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseEntered
        btnUpdate.setBackground(darkYellow);
    }//GEN-LAST:event_btnUpdateMouseEntered

    private void btnUpdateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseExited
        btnUpdate.setBackground(new Color(255,215,47));
    }//GEN-LAST:event_btnUpdateMouseExited

    private void btnCancleMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleMouseEntered
        btnCancle.setBackground(red);
    }//GEN-LAST:event_btnCancleMouseEntered

    private void btnCancleMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleMouseExited
        btnCancle.setBackground(new Color(255,102,102));
    }//GEN-LAST:event_btnCancleMouseExited

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        if(fieldControl()){
            Long id = authorUpdate.getId();
            String fullName = Controller.formatFullName(txtFullName.getText());
            int firstSpace = fullName.indexOf(" ");
            String lastName = fullName.substring(0, firstSpace);        // Lấy họ
            String firstName = fullName.substring(firstSpace + 1);      // Lấy phần còn lại
            Long quantity = authorUpdate.getProductQuantity();
            AuthorDTO a = new AuthorDTO(id,lastName,firstName,quantity);
            authorBUS.updateAuthor(a);
            AlertDialog updateSuccess = new AlertDialog(this,"Cập nhật tác giả thành công !");
            authorPanel.reloadTable();
            this.dispose();
            updateSuccess.setVisible(true);
        }
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void btnCancleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCancleMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAuthorID;
    private javax.swing.JLabel lblAuthorName;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JTextField txtAuthorID;
    private javax.swing.JTextField txtFullName;
    // End of variables declaration//GEN-END:variables
}
