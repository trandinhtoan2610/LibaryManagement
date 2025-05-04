/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/AWTForms/Dialog.java to edit this template
 */
package GUI.Component.Dialog;

import BUS.PenaltyBUS;
import BUS.PenaltyDetailsBUS;
import DTO.PenaltyDTO;
import GUI.Component.Panel.PenaltyPanel;
import GUI.Component.Panel.Statistics.Components.EventBusManager;
import GUI.Component.Panel.Statistics.Components.PenaltyChangeEvent;

import java.awt.*;

/**
 *
 * @author keika
 */
public class DeletePenaltyDialog extends javax.swing.JDialog {
    private PenaltyDTO penaltySheet;
    private PenaltyBUS penaltyBUS;
    private PenaltyDetailsBUS penaltyDetailsBUS;
    private boolean mode;
    
    public DeletePenaltyDialog(java.awt.Frame parent, boolean modal, PenaltyDTO penalty, boolean isBorrowDeleted) {
        super(parent, modal);
        penaltySheet = penalty;
        mode = isBorrowDeleted;
        penaltyBUS = new PenaltyBUS();
        penaltyDetailsBUS = new PenaltyDetailsBUS();
        initComponents();
        setMinimumSize(new Dimension(400,200));
        setMaximumSize(new Dimension(400,200));
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        btnConfirmDelete = new javax.swing.JButton();
        btnCancleDelete = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        lblContent = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        headerPanel.setBackground(new java.awt.Color(255, 51, 51));
        headerPanel.setPreferredSize(new java.awt.Dimension(100, 50));
        headerPanel.setRequestFocusEnabled(false);
        headerPanel.setLayout(new java.awt.BorderLayout());

        lblHeader.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader.setText("XÓA PHIẾU PHẠT");
        headerPanel.add(lblHeader, java.awt.BorderLayout.CENTER);

        getContentPane().add(headerPanel, java.awt.BorderLayout.PAGE_START);

        footerPanel.setBackground(new java.awt.Color(255, 255, 255));
        footerPanel.setPreferredSize(new java.awt.Dimension(100, 50));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout();
        flowLayout1.setAlignOnBaseline(true);
        footerPanel.setLayout(flowLayout1);

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
        footerPanel.add(btnConfirmDelete);

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
        footerPanel.add(btnCancleDelete);

        getContentPane().add(footerPanel, java.awt.BorderLayout.PAGE_END);

        jDesktopPane1.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane1.setLayout(new java.awt.BorderLayout());

        lblContent.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblContent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContent.setText("Bạn chắc chắn muốn xóa phiếu phạt này ?");
        jDesktopPane1.add(lblContent, java.awt.BorderLayout.CENTER);

        getContentPane().add(jDesktopPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void btnConfirmDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmDeleteMouseClicked
        try {
            penaltyDetailsBUS.deletePenaltyDetailsByPenaltyID(penaltySheet.getId());
            penaltyBUS.deletePenaltySheet(penaltySheet);
            PenaltyPanel.reloadTable();
            this.dispose();
            new AlertDialog(this, "Xóa phiếu phạt thành công !").setVisible(true);
            EventBusManager.getEventBus().post(new PenaltyChangeEvent());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Có lỗi khi xóa phiếu phạt");
        }

    }//GEN-LAST:event_btnConfirmDeleteMouseClicked

    private void btnConfirmDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmDeleteMouseEntered
        btnConfirmDelete.setBackground(new Color(139, 0, 0));
    }//GEN-LAST:event_btnConfirmDeleteMouseEntered

    private void btnConfirmDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmDeleteMouseExited
        btnConfirmDelete.setBackground(new Color(255,0,51));
    }//GEN-LAST:event_btnConfirmDeleteMouseExited

    private void btnCancleDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleDeleteMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCancleDeleteMouseClicked

    private void btnCancleDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleDeleteMouseEntered
        btnCancleDelete.setBackground(new Color(0, 100, 0));
    }//GEN-LAST:event_btnCancleDeleteMouseEntered

    private void btnCancleDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleDeleteMouseExited
        btnCancleDelete.setBackground(new Color (0,204,51));
    }//GEN-LAST:event_btnCancleDeleteMouseExited



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancleDelete;
    private javax.swing.JButton btnConfirmDelete;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel lblContent;
    private javax.swing.JLabel lblHeader;
    // End of variables declaration//GEN-END:variables
}
