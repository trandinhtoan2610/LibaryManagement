
package GUI.Component.Dialog;

import BUS.ReaderBUS;
import DTO.ReaderDTO;
import GUI.Component.Panel.ReaderPanel;
import java.awt.Color;
import java.awt.Dimension;

public class DeleteReaderDialog extends java.awt.Dialog {
    ReaderBUS readerBUS;
    ReaderDTO reader;
    ReaderPanel readerPanel;
    
    public DeleteReaderDialog(java.awt.Frame parent, boolean modal, ReaderDTO r, ReaderPanel p) {
        readerBUS = new ReaderBUS();
        this.reader = r;
        this.readerPanel = p;
        super(parent, modal);
        initComponents();
        
        setMinimumSize(new Dimension(388, 188)); 
        setMaximumSize(new Dimension(388, 188));
        setLocationRelativeTo(null);
        setTitle("Xác nhận xóa độc giả");
        setResizable(false); 
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        deleteReaderHeader = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        deleteReaderFooter = new javax.swing.JPanel();
        btnConfirmDelete = new javax.swing.JButton();
        btnCancleDelete = new javax.swing.JButton();
        deleteReaderBody = new javax.swing.JPanel();
        lblDeleteReader = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        deleteReaderHeader.setBackground(new java.awt.Color(255, 51, 51));
        deleteReaderHeader.setPreferredSize(new java.awt.Dimension(100, 50));
        deleteReaderHeader.setRequestFocusEnabled(false);
        deleteReaderHeader.setLayout(new java.awt.BorderLayout());

        lblHeader.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader.setText("XÓA ĐỘC GIẢ");
        deleteReaderHeader.add(lblHeader, java.awt.BorderLayout.CENTER);

        jPanel1.add(deleteReaderHeader, java.awt.BorderLayout.PAGE_START);

        deleteReaderFooter.setBackground(new java.awt.Color(255, 255, 255));
        deleteReaderFooter.setPreferredSize(new java.awt.Dimension(100, 50));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout();
        flowLayout1.setAlignOnBaseline(true);
        deleteReaderFooter.setLayout(flowLayout1);

        btnConfirmDelete.setBackground(new java.awt.Color(255, 0, 51));
        btnConfirmDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnConfirmDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmDelete.setText("Xác nhận");
        btnConfirmDelete.setPreferredSize(new java.awt.Dimension(90, 38));
        btnConfirmDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConfirmDeleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConfirmDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConfirmDeleteMouseExited(evt);
            }
        });
        deleteReaderFooter.add(btnConfirmDelete);

        btnCancleDelete.setBackground(new java.awt.Color(0, 204, 51));
        btnCancleDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancleDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnCancleDelete.setText("Hủy");
        btnCancleDelete.setPreferredSize(new java.awt.Dimension(90, 38));
        btnCancleDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancleDeleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancleDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancleDeleteMouseExited(evt);
            }
        });
        deleteReaderFooter.add(btnCancleDelete);

        jPanel1.add(deleteReaderFooter, java.awt.BorderLayout.PAGE_END);

        deleteReaderBody.setBackground(new java.awt.Color(255, 255, 255));
        deleteReaderBody.setLayout(new java.awt.BorderLayout());

        lblDeleteReader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDeleteReader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeleteReader.setText("Bạn chắc chắn muốn xóa độc giả này ?");
        deleteReaderBody.add(lblDeleteReader, java.awt.BorderLayout.CENTER);

        jPanel1.add(deleteReaderBody, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void btnConfirmDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmDeleteMouseClicked
        readerBUS.deleteReader(reader);
        readerPanel.reloadReaderTable();
        AlertDialog successDeleteAlert = new AlertDialog(this, "Xóa độc giả thành công !");
        this.dispose();
        successDeleteAlert.setVisible(true);
        
        
    }//GEN-LAST:event_btnConfirmDeleteMouseClicked
    
    //Nút hủy bỏ -> tắt dialog
    private void btnCancleDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleDeleteMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCancleDeleteMouseClicked
    //Nút xác nhận xóa độc giả
    private void btnConfirmDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmDeleteMouseEntered
        btnConfirmDelete.setBackground(new Color(139, 0, 0));
    }//GEN-LAST:event_btnConfirmDeleteMouseEntered

    private void btnConfirmDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmDeleteMouseExited
        btnConfirmDelete.setBackground(new Color(255,0,51));
    }//GEN-LAST:event_btnConfirmDeleteMouseExited

    private void btnCancleDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleDeleteMouseExited
        btnCancleDelete.setBackground(new Color (0,204,51));
    }//GEN-LAST:event_btnCancleDeleteMouseExited

    private void btnCancleDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleDeleteMouseEntered
        btnCancleDelete.setBackground(new Color(0, 100, 0));
    }//GEN-LAST:event_btnCancleDeleteMouseEntered

    
    



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancleDelete;
    private javax.swing.JButton btnConfirmDelete;
    private javax.swing.JPanel deleteReaderBody;
    private javax.swing.JPanel deleteReaderFooter;
    private javax.swing.JPanel deleteReaderHeader;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDeleteReader;
    private javax.swing.JLabel lblHeader;
    // End of variables declaration//GEN-END:variables
}
