package GUI.Component.Dialog;


import BUS.ReaderBUS;
import DTO.Enum.Gender;
import DTO.ReaderDTO;
import GUI.Component.Panel.ReaderPanel;
import GUI.Component.Table.ReaderTable;
import GUI.Controller.Controller;
import java.awt.Color;
import java.awt.Dimension;
import java.io.Reader;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;


public class AddnUpdateReaderDialog extends java.awt.Dialog {
    private final Color red = new Color(178,34,34);
    private final Color darkGreen = new Color(0,100,0);
    private final Color darkYellow = new Color(204, 153, 0);
    private final Color fixHeaderColor = new Color(218, 165, 32);
    private final int newCount = 3;
    
    private String mode;
    private ReaderBUS readerBUS;
    private long currentID;
    private ReaderPanel readerPanel;
    private ReaderDTO readerUpdate;
    
    // Dialog thêm hoặc cập nhật độc giả : 
    public AddnUpdateReaderDialog(java.awt.Frame parent, boolean modal,String mode, ReaderPanel rp, ReaderDTO readerUpdate) {
        super(parent, modal);
        this.mode = mode; //mode để xác định dialog này là  thêm hay cập nhật độc giả.
        this.readerPanel = rp;
        this.readerUpdate = readerUpdate; // null nếu như mode là thêm.
        setDialogTitle();
        
         
        readerBUS = new ReaderBUS();
        
        initComponents();
        if(this.mode.equals("add")) //GUI thêm độc giả
            addReaderDialog();
        else
            updateReaderDialog();  //GUI cập nhật độc giả
        
        setMinimumSize(new Dimension(728, 438)); 
        setMaximumSize(new Dimension(728, 438)); 
        setResizable(false); 
        setLocationRelativeTo(null);
    }
    
    private void setDialogTitle(){
        if(this.mode.equals("add"))
            this.setTitle("Thêm độc giả");
        else
            this.setTitle("Cập nhật độc giả");
    }
    
    private void setCurrentID(){
        try {
        currentID = readerBUS.getCurrentID() + 1; //Lấy ID hiện tại
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi lấy ID độc giả: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE); 
    }
    }
    
    //GUI thêm độc giả :
    private void addReaderDialog(){
        lblAddReaderHeader.setText("THÊM ĐỘC GIẢ MỚI");  
        setCurrentID();
        txtReaderID.setText(String.valueOf(currentID)); 

        // Đặt các Field khác thành rỗng
        txtReaderFullName.setText("");
        txtReaderPhone.setText("");
        txtReaderAddress.setText("");
        lblReaderPrestige.setVisible(false);
        txtReaderPrestige.setVisible(false);
        btnGroupGender.clearSelection();
        btnFemale.setSelected(true);
        btnAdd.setVisible(true);
        btnUpdate.setVisible(false);
        btnFemale.requestFocus();
    }
    
    
    // GUI cập nhật độc giả :
    private void updateReaderDialog(){
        
        
        lblAddReaderHeader.setText("CẬP NHẬT THÔNG TIN ĐỘC GIẢ");
        addReaderHeaderPanel.setBackground(fixHeaderColor);
        
        //Lấy ra thông tin độc giả muốn sửa :
        txtReaderID.setText(readerUpdate.getId().toString());
        txtReaderFullName.setText(readerUpdate.getLastName() + ' ' +readerUpdate.getFirstName() );
        lblReaderPrestige.setVisible(true);
        txtReaderPrestige.setVisible(true);
        txtReaderPrestige.setText(Integer.toString(readerUpdate.getComplianceCount()));
        String gender = readerUpdate.getGender() == Gender.Nam ? "Nam" : "Nữ";
        if(gender.equals("Nam"))
            btnMale.setSelected(true);
        else
            btnFemale.setSelected(true);
        
        txtReaderPhone.setText(readerUpdate.getPhone());
        txtReaderAddress.setText(readerUpdate.getAddress());
        btnUpdate.setVisible(true);
        btnAdd.setVisible(false);
        txtReaderFullName.requestFocus();
    }
    
    
    
    
    private void addHover(JButton b, Color g){
        b.setBackground(g);
    }
    
    //Hàm kiểm tra ràng buộc :
    private boolean fieldController(){
        if(btnGroupGender.getSelection()== null){
            AlertDialog GenderAlert = new AlertDialog(this,"Vui lòng chọn giới tính ! ");
            GenderAlert.setVisible(true);
            btnFemale.requestFocus();
            return false;
        }
        
        if(txtReaderFullName.equals("")){
            AlertDialog blankNameAlert = new AlertDialog(this,"Vui lòng nhập họ và tên ! ");
            blankNameAlert.setVisible(true);
            txtReaderFullName.requestFocus();
            return false;
        }
        
        if(!Controller.checkValidName(txtReaderFullName.getText())){
            AlertDialog invalidNameAlert = new AlertDialog(this,"Vui lòng nhập họ và tên hợp lệ ! ");
            invalidNameAlert.setVisible(true);
            txtReaderFullName.requestFocus();
            return false;
        }
        
        if(txtReaderPhone.equals("")){
            AlertDialog blankPhoneAlert = new AlertDialog(this,"Vui lòng nhập số điện thoại ! ");
            blankPhoneAlert.setVisible(true);
            txtReaderPhone.requestFocus();
            return false;
        }
                
        
        if(!Controller.checkValidPhone(txtReaderPhone.getText())){
            AlertDialog invalidPhoneAlert = new AlertDialog(this,"Vui lòng nhập số điện thoại hợp lệ ! ");
            invalidPhoneAlert.setVisible(true);
            txtReaderPhone.requestFocus();
            return false;
        }
            
        if(txtReaderAddress.getText().equals("")){
            AlertDialog blankAddressAlert = new AlertDialog(this,"Vui lòng nhập địa chỉ ! ");
            blankAddressAlert.setVisible(true);
            txtReaderAddress.requestFocus();
            return false;
        }
        return true;
    }
    
    

    
    
    
    

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupGender = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        addReaderHeaderPanel = new javax.swing.JPanel();
        lblAddReaderHeader = new javax.swing.JLabel();
        addReaderFooterPanel = new javax.swing.JPanel();
        btnUpdate = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        addReaderBodyPanel = new javax.swing.JPanel();
        lblReaderID = new javax.swing.JLabel();
        lblReaderFullname = new javax.swing.JLabel();
        lblReaderGender = new javax.swing.JLabel();
        lblReaderPhone = new javax.swing.JLabel();
        lblReaderAddress = new javax.swing.JLabel();
        txtReaderID = new javax.swing.JTextField();
        txtReaderFullName = new javax.swing.JTextField();
        btnMale = new javax.swing.JRadioButton();
        btnFemale = new javax.swing.JRadioButton();
        txtReaderAddress = new javax.swing.JTextField();
        txtReaderPhone = new javax.swing.JTextField();
        lblReaderPrestige = new javax.swing.JLabel();
        txtReaderPrestige = new javax.swing.JTextField();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(227, 105));
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setMinimumSize(new java.awt.Dimension(227, 105));
        jPanel1.setPreferredSize(new java.awt.Dimension(640, 100));
        jPanel1.setLayout(new java.awt.BorderLayout());

        addReaderHeaderPanel.setBackground(new java.awt.Color(0, 204, 102));
        addReaderHeaderPanel.setForeground(new java.awt.Color(255, 255, 255));
        addReaderHeaderPanel.setPreferredSize(new java.awt.Dimension(100, 60));
        addReaderHeaderPanel.setLayout(new java.awt.BorderLayout());

        lblAddReaderHeader.setBackground(new java.awt.Color(255, 255, 255));
        lblAddReaderHeader.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        lblAddReaderHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblAddReaderHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddReaderHeader.setText("THÊM ĐỘC GIẢ MỚI");
        addReaderHeaderPanel.add(lblAddReaderHeader, java.awt.BorderLayout.CENTER);

        jPanel1.add(addReaderHeaderPanel, java.awt.BorderLayout.PAGE_START);

        addReaderFooterPanel.setBackground(new java.awt.Color(255, 255, 255));
        addReaderFooterPanel.setPreferredSize(new java.awt.Dimension(100, 70));
        addReaderFooterPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 22, 5));

        btnUpdate.setBackground(new java.awt.Color(255, 215, 47));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Cập nhật");
        btnUpdate.setPreferredSize(new java.awt.Dimension(105, 40));
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
        addReaderFooterPanel.add(btnUpdate);

        btnAdd.setBackground(new java.awt.Color(0, 255, 204));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.setPreferredSize(new java.awt.Dimension(105, 40));
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddMouseExited(evt);
            }
        });
        addReaderFooterPanel.add(btnAdd);

        btnReject.setBackground(new java.awt.Color(255, 102, 102));
        btnReject.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReject.setForeground(new java.awt.Color(255, 255, 255));
        btnReject.setText("Hủy bỏ");
        btnReject.setPreferredSize(new java.awt.Dimension(105, 40));
        btnReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRejectMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRejectMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRejectMouseExited(evt);
            }
        });
        addReaderFooterPanel.add(btnReject);

        jPanel1.add(addReaderFooterPanel, java.awt.BorderLayout.PAGE_END);

        addReaderBodyPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblReaderID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblReaderID.setText("Mã độc giả");
        lblReaderID.setPreferredSize(new java.awt.Dimension(95, 34));

        lblReaderFullname.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblReaderFullname.setText("Họ và tên");
        lblReaderFullname.setPreferredSize(new java.awt.Dimension(95, 34));

        lblReaderGender.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblReaderGender.setText("Giới tính");
        lblReaderGender.setPreferredSize(new java.awt.Dimension(95, 34));

        lblReaderPhone.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblReaderPhone.setText("Số điện thoại");
        lblReaderPhone.setPreferredSize(new java.awt.Dimension(95, 34));

        lblReaderAddress.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblReaderAddress.setText("Địa chỉ");
        lblReaderAddress.setPreferredSize(new java.awt.Dimension(95, 34));

        txtReaderID.setEditable(false);

        btnGroupGender.add(btnMale);
        btnMale.setText("Nam");
        btnMale.setToolTipText("");

        btnGroupGender.add(btnFemale);
        btnFemale.setText("Nữ");
        btnFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFemaleActionPerformed(evt);
            }
        });

        lblReaderPrestige.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblReaderPrestige.setText("Uy tín ");

        txtReaderPrestige.setEditable(false);
        txtReaderPrestige.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReaderPrestigeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addReaderBodyPanelLayout = new javax.swing.GroupLayout(addReaderBodyPanel);
        addReaderBodyPanel.setLayout(addReaderBodyPanelLayout);
        addReaderBodyPanelLayout.setHorizontalGroup(
            addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addReaderBodyPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addReaderBodyPanelLayout.createSequentialGroup()
                        .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addReaderBodyPanelLayout.createSequentialGroup()
                                .addComponent(lblReaderGender, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnMale, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFemale))
                            .addGroup(addReaderBodyPanelLayout.createSequentialGroup()
                                .addComponent(lblReaderID, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtReaderID, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)
                                .addComponent(lblReaderPrestige, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtReaderPrestige, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 267, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addReaderBodyPanelLayout.createSequentialGroup()
                        .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblReaderPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblReaderFullname, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblReaderAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtReaderAddress)
                            .addComponent(txtReaderFullName, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtReaderPhone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE))
                        .addGap(51, 51, 51))))
        );
        addReaderBodyPanelLayout.setVerticalGroup(
            addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addReaderBodyPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReaderID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReaderID)
                    .addComponent(lblReaderPrestige, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReaderPrestige, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReaderGender, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMale)
                    .addComponent(btnFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReaderFullname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addReaderBodyPanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtReaderFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReaderPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReaderPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(addReaderBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReaderAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReaderAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        jPanel1.add(addReaderBodyPanel, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void btnFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFemaleActionPerformed

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseEntered
        addHover(btnAdd, darkGreen);
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnRejectMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRejectMouseEntered
        addHover(btnReject, red);
    }//GEN-LAST:event_btnRejectMouseEntered

    private void btnAddMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseExited
        btnAdd.setBackground(new Color(0,255,204));
    }//GEN-LAST:event_btnAddMouseExited

    private void btnRejectMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRejectMouseExited
        btnReject.setBackground(new Color(255,102,102));
    }//GEN-LAST:event_btnRejectMouseExited

    private void btnUpdateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseEntered
        addHover(btnUpdate, darkYellow);
    }//GEN-LAST:event_btnUpdateMouseEntered

    private void btnUpdateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseExited
        btnUpdate.setBackground(new Color(255,215,47));
    }//GEN-LAST:event_btnUpdateMouseExited

    //Nút xác nhận thêm độc giả :
    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        if(fieldController()){
            String fullName = Controller.formatFullName(txtReaderFullName.getText());
            int firstSpace = fullName.indexOf(" ");
            String lastName = fullName.substring(0, firstSpace);        // Lấy họ
            String firstName = fullName.substring(firstSpace + 1); // Lấy phần còn lại
            
            ReaderDTO r = new ReaderDTO( 
                    Long.parseLong(txtReaderID.getText()),
                    firstName,
                    lastName,
                    btnFemale.isSelected() ? Gender.Nam : Gender.Nữ,
                    txtReaderPhone.getText(),
                    txtReaderAddress.getText(),
                    newCount
            );
            
            readerBUS.addReader(r);
            
            AlertDialog addReaderSuccess = new AlertDialog(this, "Thêm độc giả thành công !");
            addReaderSuccess.setVisible(true);
            readerPanel.reloadReaderTable();
               
            addReaderDialog();
            } 
    }//GEN-LAST:event_btnAddMouseClicked

    //Nút xác nhận cập nhật thông tin độc giả
    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        if(fieldController()){
            String fullName = Controller.formatFullName(txtReaderFullName.getText());
            int firstSpace = fullName.indexOf(" ");
            String lastName = fullName.substring(0, firstSpace);        // Lấy họ
            String firstName = fullName.substring(firstSpace + 1);      // Lấy phần còn lại
            
            ReaderDTO r = new ReaderDTO( 
                    Long.parseLong(txtReaderID.getText()),
                    firstName,
                    lastName,
                    btnMale.isSelected() ? Gender.Nam : Gender.Nữ,
                    txtReaderPhone.getText(),
                    txtReaderAddress.getText(),
                    readerUpdate.getComplianceCount()
            );
            
            readerBUS.updateReader(r);
            readerPanel.reloadReaderTable();
            AlertDialog updateSuccess = new AlertDialog(this,"Cập nhật độc giả thành công !");
            updateSuccess.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnUpdateMouseClicked

    //Nút tắt dialog
    private void btnRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRejectMouseClicked
        this.dispose(); 
    }//GEN-LAST:event_btnRejectMouseClicked

    private void txtReaderPrestigeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReaderPrestigeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReaderPrestigeActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addReaderBodyPanel;
    private javax.swing.JPanel addReaderFooterPanel;
    private javax.swing.JPanel addReaderHeaderPanel;
    private javax.swing.JButton btnAdd;
    private javax.swing.JRadioButton btnFemale;
    private javax.swing.ButtonGroup btnGroupGender;
    private javax.swing.JRadioButton btnMale;
    private javax.swing.JButton btnReject;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAddReaderHeader;
    private javax.swing.JLabel lblReaderAddress;
    private javax.swing.JLabel lblReaderFullname;
    private javax.swing.JLabel lblReaderGender;
    private javax.swing.JLabel lblReaderID;
    private javax.swing.JLabel lblReaderPhone;
    private javax.swing.JLabel lblReaderPrestige;
    private javax.swing.JTextField txtReaderAddress;
    private javax.swing.JTextField txtReaderFullName;
    private javax.swing.JTextField txtReaderID;
    private javax.swing.JTextField txtReaderPhone;
    private javax.swing.JTextField txtReaderPrestige;
    // End of variables declaration//GEN-END:variables
}