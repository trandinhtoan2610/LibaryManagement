package GUI.Component.Dialog;

import BUS.BookBUS;
import BUS.PurchaseOrderBUS;
import DTO.Book;
import DTO.Enum.PurchaseStatus;
import DTO.PurchaseOrderDTO;
import DTO.PurchaseOrderDetailDTO;
import GUI.Component.Panel.BookPanel;
import GUI.Component.Panel.PurchaseOrderPanel;
import GUI.Component.Panel.Statistics.Components.EventBusManager;
import GUI.Component.Panel.Statistics.Components.PurchaseChangeEvent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;

public class DeletePurchaseOrderDialog extends java.awt.Dialog {
    private PurchaseOrderBUS purchaseOrderBUS;
    private PurchaseOrderDTO purchaseOrderToDelete;
    private PurchaseOrderPanel purchaseOrderPanel;
    private final BookBUS bookBUS = new BookBUS();

    private JPanel jPanel1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JButton btnDeletePO;
    private JButton btnCancelPO;

    public DeletePurchaseOrderDialog(java.awt.Frame parent, boolean modal, PurchaseOrderDTO po, PurchaseOrderPanel panel) {
        super(parent, modal);
        this.purchaseOrderBUS = new PurchaseOrderBUS();
        this.purchaseOrderToDelete = po;
        this.purchaseOrderPanel = panel;

        initComponents();

        setMinimumSize(new Dimension(420, 240));
        setLocationRelativeTo(null);
        setTitle("Xác nhận xóa phiếu nhập hàng");
        setResizable(false);
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel("Xóa phiếu nhập hàng");
        jLabel2 = new JLabel("Bạn có chắc chắn muốn xóa phiếu nhập hàng này?");
        btnDeletePO = new JButton("Xóa");
        btnCancelPO = new JButton("Hủy bỏ");

        // Header Panel
        jPanel1.setBackground(new Color(255, 51, 51));
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 26));
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setHorizontalAlignment(JLabel.CENTER);

        // Confirmation label
        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        jLabel2.setHorizontalAlignment(JLabel.CENTER);

        // Delete Button
        btnDeletePO.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnDeletePO.setBackground(new Color(220, 53, 69));
        btnDeletePO.setForeground(Color.WHITE);
        btnDeletePO.setFocusPainted(false);
        btnDeletePO.addActionListener(this::btnDeleteActionPerformed);

        // Cancel Button
        btnCancelPO.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnCancelPO.setBackground(new Color(108, 117, 125));
        btnCancelPO.setForeground(Color.WHITE);
        btnCancelPO.setFocusPainted(false);
        btnCancelPO.addActionListener(this::btnCancelActionPerformed);

        // Hover effect
        btnCancelPO.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCancelPO.setBackground(new Color(73, 80, 87));
            }
            public void mouseExited(MouseEvent evt) {
                btnCancelPO.setBackground(new Color(108, 117, 125));
            }
        });

        // Layout
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(30)
                    .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addGap(30))
                .addGroup(layout.createSequentialGroup()
                    .addGap(80)
                    .addComponent(btnDeletePO, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addGap(60)
                    .addComponent(btnCancelPO, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addGap(80))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                .addGap(30)
                .addComponent(jLabel2)
                .addGap(30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeletePO, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelPO, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                .addGap(30)
        );

        // Set header layout
        GroupLayout headerLayout = new GroupLayout(jPanel1);
        jPanel1.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap()
        );
        headerLayout.setVerticalGroup(
            headerLayout.createSequentialGroup()
                .addGap(15)
                .addComponent(jLabel1)
                .addGap(15)
        );

        pack();
    }

    private void btnDeleteActionPerformed(ActionEvent evt) {
        boolean deleted = purchaseOrderBUS.deletePurchaseOrder(purchaseOrderToDelete.getId());
        if (deleted) {
            purchaseOrderPanel.deletePurchaseOrder(purchaseOrderToDelete);
            AlertDialog successDeleteAlert = new AlertDialog(this, "Xóa phiếu nhập hàng thành công!");
            successDeleteAlert.setVisible(true);
            this.dispose();
            EventBusManager.getEventBus().post(new PurchaseChangeEvent());
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa phiếu nhập hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
        this.dispose();
    }
}
