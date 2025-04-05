package GUI.Component.Dialog;

import DTO.SupplierDTO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class UpdateSupplierDialog extends javax.swing.JDialog {
    private JTextField txtSupplierName;
    private JTextField txtSupplierPhone;
    private JTextField txtSupplierAddress;
    private JButton btnUpdateSupplier;
    private JButton btnCancelSupplier;
    private SupplierDTO supplier;
   
//    public UpdateSupplierDialog() {
//        initComponents();
//    }
    public UpdateSupplierDialog(JFrame parent, boolean modal, SupplierDTO supplier) {
        super(parent, modal);
        this.supplier = supplier;
        initComponents();
        addListeners();
        populateFields();
    }
    private void populateFields() {
        if (supplier != null) {
            txtSupplierName.setText(supplier.getName());
            txtSupplierPhone.setText(supplier.getPhone());
            txtSupplierAddress.setText(supplier.getAddress());
        }
    }
    private void addListeners() {
        btnUpdateSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSupplier();
            }
        });

        btnCancelSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    private void updateSupplier() {
        String name = txtSupplierName.getText();
        String phone = txtSupplierPhone.getText();
        String address = txtSupplierAddress.getText();

        // Perform the action to update the supplier (e.g., save to database)
        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Example: Update the supplier information
        System.out.println("Updating supplier: Name=" + name + ", Phone=" + phone + ", Address=" + address);

        // Clear the fields after updating
        txtSupplierName.setText("");
        txtSupplierPhone.setText("");
        txtSupplierAddress.setText("");

        dispose();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblSupplierID = new javax.swing.JLabel();
        lblSupplierName = new javax.swing.JLabel();
        lblSupplierPhone = new javax.swing.JLabel();
        lblSupplierAddress = new javax.swing.JLabel();
        txtSupplierID1 = new javax.swing.JTextField();
        txtSupplierName = new javax.swing.JTextField();
        txtSupplierPhone = new javax.swing.JTextField();
        txtSupplierAddress = new javax.swing.JTextField();
        btnSaveUpdateSupplier = new javax.swing.JButton();
        btnCancelUpdateSupplier = new javax.swing.JButton();
        btnSaveUpdateSupplier.addActionListener(e -> updateSupplier());
        btnCancelUpdateSupplier.addActionListener(e -> dispose());

        jPanel1.setBackground(new java.awt.Color(102, 0, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setBackground(new java.awt.Color(0, 204, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cập nhật nhà cung cấp");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(134, 134, 134))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        lblSupplierID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSupplierID.setText("Mã nhà cung cấp");

        lblSupplierName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSupplierName.setText("Tên nhà cung cấp");

        lblSupplierPhone.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSupplierPhone.setText("Số điện thoại");

        lblSupplierAddress.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSupplierAddress.setText("Địa chỉ");

        btnSaveUpdateSupplier.setBackground(new java.awt.Color(0, 204, 0));
        btnSaveUpdateSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSaveUpdateSupplier.setText("Lưu thay đổi");
        btnSaveUpdateSupplier.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnCancelUpdateSupplier.setBackground(new java.awt.Color(255, 51, 0));
        btnCancelUpdateSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCancelUpdateSupplier.setText("Hủy Bỏ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblSupplierName)
                            .addComponent(lblSupplierID))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSupplierID1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupplierID2, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSupplierAddress)
                            .addComponent(lblSupplierPhone))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSupplierID3, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupplierID4, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(28, 28, 28))
            .addGroup(layout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addComponent(btnSaveUpdateSupplier)
                .addGap(114, 114, 114)
                .addComponent(btnCancelUpdateSupplier)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupplierID1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupplierID2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lblSupplierPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSupplierAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSupplierID3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSupplierID4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveUpdateSupplier)
                    .addComponent(btnCancelUpdateSupplier))
                .addGap(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelUpdateSupplier;
    private javax.swing.JButton btnSaveUpdateSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblSupplierAddress;
    private javax.swing.JLabel lblSupplierID;
    private javax.swing.JLabel lblSupplierName;
    private javax.swing.JLabel lblSupplierPhone;
    private javax.swing.JTextField txtSupplierID1;
    private javax.swing.JTextField txtSupplierID2;
    private javax.swing.JTextField txtSupplierID3;
    private javax.swing.JTextField txtSupplierID4;
    // End of variables declaration//GEN-END:variables
}
