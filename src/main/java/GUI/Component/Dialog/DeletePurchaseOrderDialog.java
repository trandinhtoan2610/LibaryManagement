package GUI.Component.Dialog;

import BUS.PurchaseOrderBUS;
import DTO.PurchaseOrderDTO;
import GUI.Component.Panel.PurchaseOrderPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

public class DeletePurchaseOrderDialog extends java.awt.Dialog {
    PurchaseOrderBUS purchaseOrderBUS;
    PurchaseOrderDTO purchaseOrderToDelete;
    PurchaseOrderPanel purchaseOrderPanel;

    public DeletePurchaseOrderDialog(java.awt.Frame parent, boolean modal, PurchaseOrderDTO po, PurchaseOrderPanel panel) {
        purchaseOrderBUS = new PurchaseOrderBUS();
        this.purchaseOrderToDelete = po;
        this.purchaseOrderPanel = panel;
        super(parent, modal);
        initComponents();

        setMinimumSize(new Dimension(388, 188));
        setMaximumSize(new Dimension(388, 188));
        setLocationRelativeTo(null);
        setTitle("Xác nhận xóa phiếu nhập hàng");
        setResizable(false);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnDeletePO = new javax.swing.JButton();
        btnCancelPO = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); 
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Xóa phiếu nhập hàng");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(90, 90, 90)
                    .addComponent(jLabel1)
                    .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(jLabel1)
                    .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); 
        jLabel2.setText("Bạn có muốn xóa phiếu nhập hàng?");

        btnDeletePO.setFont(new java.awt.Font("Segoe UI", 0, 18)); 
        btnDeletePO.setText("Xóa");
        btnDeletePO.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnCancelPO.setFont(new java.awt.Font("Segoe UI", 0, 18)); 
        btnCancelPO.setText("Hủy bỏ");
        btnCancelPO.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        btnCancelPO.addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
            public void mouseClicked(MouseEvent evt) {}
            public void mousePressed(MouseEvent evt) {}
            public void mouseReleased(MouseEvent evt) {}
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(105, 105, 105)
                    .addComponent(jLabel2)
                    .addContainerGap(105, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addGap(130, 130, 130)
                    .addComponent(btnDeletePO)
                    .addGap(56, 56, 56)
                    .addComponent(btnCancelPO)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(44, 44, 44)
                    .addComponent(jLabel2)
                    .addGap(50, 50, 50)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDeletePO)
                        .addComponent(btnCancelPO))
                    .addGap(0, 84, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnDeleteActionPerformed(ActionEvent evt) {
        boolean deleted = purchaseOrderBUS.deletePurchaseOrder(purchaseOrderToDelete);
        if (deleted) {
            AlertDialog successDeleteAlert = new AlertDialog(this, "Xóa phiếu nhập hàng thành công!");
            this.dispose();
            successDeleteAlert.setVisible(true);
            purchaseOrderPanel.reloadPurchaseOrderTable();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa phiếu nhập hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
        this.dispose();
    }

    private void btnCancelMouseEntered(MouseEvent evt) {
        btnCancelPO.setBackground(new Color(0, 100, 0));
    }

    private void btnCancelMouseExited(MouseEvent evt) {
        btnCancelPO.setBackground(new Color(0, 204, 51));
    }

    // Variables declaration
    private javax.swing.JButton btnCancelPO;
    private javax.swing.JButton btnDeletePO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
}
