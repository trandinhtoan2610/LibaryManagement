package GUI.Component.Dialog;

import BUS.EmployeeBUS;
import BUS.PenaltyBUS;
import DTO.*;
import DTO.Enum.PayStatus;
import GUI.Component.Panel.PenaltyPanel;
import GUI.Component.Panel.Statistics.Components.EventBusManager;
import GUI.Component.Panel.Statistics.Components.PenaltyChangeEvent;
import GUI.Controller.Controller;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Date;
import java.util.List;


public class UpdatePenaltyDialog extends JDialog {
    private PenaltyDTO penalty;
    private ReaderDTO reader;
    private BorrowDTO borrowSheet;
    private List<PenaltyDetailsDTO> penaltyDetails;
    private PenaltyBUS penaltyBUS;
    private EmployeeBUS employeeBUS;

   
    public UpdatePenaltyDialog(java.awt.Frame parent, boolean modal, PenaltyDTO p, List<PenaltyDetailsDTO> list, ReaderDTO reader, BorrowDTO borrowSheet ) {
        super(parent, modal);
        penaltyBUS = new PenaltyBUS();
        this.penalty = p;
        this.penaltyDetails = list;
        this.reader = reader;
        this.borrowSheet = borrowSheet;
        this.employeeBUS = new EmployeeBUS();
        initComponents();
        tblPenaltyDetails.resetTable(list);
        setMinimumSize(new Dimension(750,600));
        setMaximumSize(new Dimension(750,600));
        setSize(650, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        renderDialog();

        txtEmployeeID.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showEmployeeInfo();
            }
        });
    }

    private void showEmployeeInfo() {
        String employeeId = txtEmployeeID.getText();
        if (employeeId.isEmpty()) {
            lblEmployeeName.setText("Không tìm thấy");
        } else {
            try {
                Long employee = Long.parseLong(employeeId);
                Employee e = employeeBUS.getEmployeeById(employee);
                lblEmployeeName.setText(e.getFirstName() + " " + e.getLastName());
            } catch (Exception e) {
                lblEmployeeName.setText("Không tìm thấy");
            }
        }
    }

    public void renderDialog(){
        txtPayDate.setDateFormatString("dd/MM/yyyy");
        txtPenaltyDate.setDateFormatString("dd/MM/yyyy");

        String borrowID = borrowSheet.getId();
        String readerID = reader.getId();
        String readerName = Controller.formatFullName(reader.getLastName() + " " + reader.getFirstName());
        txtPenaltyDate.setDate(penalty.getPenaltyDate());

        lblBorrowID.setText(borrowID);
        lblReaderID.setText(readerID);
        lblReaderName.setText(readerName);

        if(penalty.getPayStatus().equals(PayStatus.Chưa_Thanh_Toán)){
            lblStatus.setForeground(new Color(204,51,0));
            lblStatus.setText("Chưa thanh toán");
        }
        else {
            lblStatus.setForeground(new Color(0,153,51));
            lblStatus.setText("Đã thanh toán");
        }

        if(penalty.getEmployeeID() == null){
            lblEmployeeName.setText("");
            txtEmployeeID.setText("");
        }
        else {
            Employee employee = penaltyBUS.getEmployeeHandlePenalty(penalty);
            lblEmployeeName.setText(Controller.formatFullName(employee.getFirstName() + " " + employee.getLastName()));
            txtEmployeeID.setText(penalty.getEmployeeID().toString());
        }

        Date payDate = penalty.getPayDate();
        if(payDate == null){
            txtPayDate.setDate(null);
        }
        else{
            txtPayDate.setDate(payDate);
        }
    }

    public int checkField() {

        Date payDate = txtPayDate.getDate();
        String empTxt = txtEmployeeID.getText().trim();

        // Cả ngày & mã NV đều trống –‑> chưa thanh toán
        if (payDate == null && empTxt.isEmpty()) {
            return 0;
        }

        // Có ngày nhưng chưa nhập NV
        if (payDate != null && empTxt.isEmpty()) {
            new AlertDialog(this, "Vui lòng chọn nhân viên xử lý!").setVisible(true);
            return 1;
        }

        // Kiểm tra mã NV là số
        long employeeID;
        try {
            employeeID = Long.parseLong(empTxt);
        } catch (NumberFormatException ex) {
            new AlertDialog(this, "Vui lòng nhập mã nhân viên hợp lệ!").setVisible(true);
            return 1;
        }

        // Kiểm tra nhân viên có tồn tại ko
        if (employeeBUS.getEmployeeById(employeeID) == null) {
            new AlertDialog(this, "Không tìm thấy nhân viên với mã NV: " + employeeID).setVisible(true);
            return 1;
        }

        // Nếu có nhân viên -> Kiểm tra ngày nộp phạt
        if (payDate == null) {
            new AlertDialog(this, "Vui lòng chọn ngày thanh toán!").setVisible(true);
            return 1;
        }
        if (payDate.before(penalty.getPenaltyDate())) {
            new AlertDialog(this, "Ngày thanh toán không được trước ngày phạt!").setVisible(true);
            return 1;
        }

        return 2;
    }



    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        headerTitlePanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        sheetDetailPanel = new javax.swing.JPanel();
        penaltyStatusPanel = new javax.swing.JPanel();
        lblStatusTitle = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        penaltyInfoPanel = new javax.swing.JPanel();
        borrowSheetDetailPanel = new javax.swing.JPanel();
        lblBorrowTitle = new javax.swing.JLabel();
        lblBorrowID = new javax.swing.JLabel();
        readerInfoPanel = new javax.swing.JPanel();
        lblReaderTitle = new javax.swing.JLabel();
        lblReaderName = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblReaderID = new javax.swing.JLabel();
        footerPanel = new javax.swing.JPanel();
        submitButtonsPanel = new javax.swing.JPanel();
        btnSubmit = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        statusDetailPanel = new javax.swing.JPanel();
        employeePanel = new javax.swing.JPanel();
        lblEmployeeTitle = new javax.swing.JLabel();
        lblEmployeeName = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblEmployeeIDTitle = new javax.swing.JLabel();
        txtEmployeeID = new javax.swing.JTextField();
        btnSearchEmployee = new GUI.Component.Button.ButtonChosen();
        lblPenaltyDate = new javax.swing.JLabel();
        txtPenaltyDate = new com.toedter.calendar.JDateChooser();
        lblPayDate = new javax.swing.JLabel();
        txtPayDate = new com.toedter.calendar.JDateChooser();
        bodyPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPenaltyDetails = new GUI.Component.Table.PenaltyDetailsTable();

        setMinimumSize(new java.awt.Dimension(670, 530));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setPreferredSize(new java.awt.Dimension(100, 150));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerTitlePanel.setBackground(new java.awt.Color(51, 153, 255));
        headerTitlePanel.setPreferredSize(new java.awt.Dimension(100, 40));
        headerTitlePanel.setLayout(new java.awt.BorderLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Cập nhật phiếu phạt");
        lblTitle.setToolTipText("");
        headerTitlePanel.add(lblTitle, java.awt.BorderLayout.CENTER);

        headerPanel.add(headerTitlePanel, java.awt.BorderLayout.PAGE_START);

        sheetDetailPanel.setBackground(new java.awt.Color(255, 255, 255));
        sheetDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chung", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 2, 12))); // NOI18N
        sheetDetailPanel.setLayout(new java.awt.BorderLayout());

        penaltyStatusPanel.setBackground(new java.awt.Color(255, 255, 255));
        penaltyStatusPanel.setPreferredSize(new java.awt.Dimension(30, 30));

        lblStatusTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStatusTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatusTitle.setText("Trạng thái : ");
        penaltyStatusPanel.add(lblStatusTitle);

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(204, 51, 0));
        lblStatus.setText("Chưa thanh toán ");
        penaltyStatusPanel.add(lblStatus);

        sheetDetailPanel.add(penaltyStatusPanel, java.awt.BorderLayout.PAGE_START);

        penaltyInfoPanel.setBackground(new java.awt.Color(255, 255, 255));
        penaltyInfoPanel.setLayout(new java.awt.GridLayout(2, 0));

        borrowSheetDetailPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblBorrowTitle.setText("Từ phiếu mượn : ");
        borrowSheetDetailPanel.add(lblBorrowTitle);

        lblBorrowID.setText("BorrowID");
        borrowSheetDetailPanel.add(lblBorrowID);

        penaltyInfoPanel.add(borrowSheetDetailPanel);

        readerInfoPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblReaderTitle.setText("Độc giả : ");
        readerInfoPanel.add(lblReaderTitle);

        lblReaderName.setText("ReaderName");
        readerInfoPanel.add(lblReaderName);

        jLabel10.setText("-");
        readerInfoPanel.add(jLabel10);

        jLabel1.setText("Mã ĐG :");
        readerInfoPanel.add(jLabel1);

        lblReaderID.setText("ReaderID");
        readerInfoPanel.add(lblReaderID);

        penaltyInfoPanel.add(readerInfoPanel);

        sheetDetailPanel.add(penaltyInfoPanel, java.awt.BorderLayout.CENTER);

        headerPanel.add(sheetDetailPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        footerPanel.setBackground(new java.awt.Color(255, 255, 255));
        footerPanel.setPreferredSize(new java.awt.Dimension(100, 200));
        footerPanel.setLayout(new java.awt.BorderLayout());

        submitButtonsPanel.setBackground(new java.awt.Color(255, 255, 255));
        submitButtonsPanel.setPreferredSize(new java.awt.Dimension(100, 40));
        submitButtonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 4));

        btnSubmit.setBackground(new java.awt.Color(255, 255, 51));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSubmit.setText("Cập nhật");
        btnSubmit.setPreferredSize(new java.awt.Dimension(85, 30));
        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSubmitMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmitMouseExited(evt);
            }
        });
        submitButtonsPanel.add(btnSubmit);

        btnReject.setBackground(new java.awt.Color(255, 51, 51));
        btnReject.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReject.setForeground(new java.awt.Color(255, 255, 255));
        btnReject.setText("Hủy bỏ");
        btnReject.setPreferredSize(new java.awt.Dimension(85, 30));
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
        submitButtonsPanel.add(btnReject);

        footerPanel.add(submitButtonsPanel, java.awt.BorderLayout.PAGE_END);

        statusDetailPanel.setBackground(new java.awt.Color(255, 255, 255));
        statusDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin xử lý", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        statusDetailPanel.setLayout(new java.awt.GridLayout(3, 2, 10, 15));

        employeePanel.setBackground(new java.awt.Color(255, 255, 255));
        employeePanel.setLayout(new java.awt.BorderLayout());

        lblEmployeeTitle.setText("Nhân viên xử lý : ");
        lblEmployeeTitle.setMinimumSize(new java.awt.Dimension(38, 50));
        lblEmployeeTitle.setPreferredSize(new java.awt.Dimension(95, 50));
        employeePanel.add(lblEmployeeTitle, java.awt.BorderLayout.LINE_START);

        lblEmployeeName.setText("jLabel7");
        employeePanel.add(lblEmployeeName, java.awt.BorderLayout.CENTER);

        statusDetailPanel.add(employeePanel);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        lblEmployeeIDTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEmployeeIDTitle.setText("Mã NV:");
        jPanel1.add(lblEmployeeIDTitle);

        txtEmployeeID.setPreferredSize(new java.awt.Dimension(104, 22));
        jPanel1.add(txtEmployeeID);

        btnSearchEmployee.setPreferredSize(new java.awt.Dimension(35, 25));
        btnSearchEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchEmployeeActionPerformed(evt);
            }
        });
        jPanel1.add(btnSearchEmployee);

        statusDetailPanel.add(jPanel1);

        lblPenaltyDate.setText("Ngày phạt : ");
        statusDetailPanel.add(lblPenaltyDate);

        txtPenaltyDate.setEnabled(false);
        statusDetailPanel.add(txtPenaltyDate);

        lblPayDate.setText("Ngày nộp phạt :");
        statusDetailPanel.add(lblPayDate);
        statusDetailPanel.add(txtPayDate);

        footerPanel.add(statusDetailPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

        bodyPanel.setBackground(new java.awt.Color(255, 255, 255));
        bodyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết phạt", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 14))); // NOI18N
        bodyPanel.setLayout(new java.awt.BorderLayout());

        tblPenaltyDetails.setToolTipText("");
        jScrollPane1.setViewportView(tblPenaltyDetails);

        bodyPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(bodyPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void btnSearchEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchEmployeeActionPerformed
        ChooseEmployeeDialog chooseEmployeeDialog = new ChooseEmployeeDialog(this);
        chooseEmployeeDialog.setVisible(true);
        if (chooseEmployeeDialog.getSelectedEmployee() != null) {
            Employee currentEmployee = chooseEmployeeDialog.getSelectedEmployee();
            txtEmployeeID.setText(currentEmployee.getId().toString());
            lblEmployeeName.setText(Controller.formatFullName(currentEmployee.getFirstName()+ " "+ currentEmployee.getLastName()));
        }
    }//GEN-LAST:event_btnSearchEmployeeActionPerformed

    private void btnSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmitMouseClicked
        if(checkField()== 1) return;

        if(checkField() == 0){
            penalty.setEmployeeID(null);
            penalty.setPayDate(null);
            penalty.setPayStatus(PayStatus.Chưa_Thanh_Toán);
        }

        else if(checkField() == 2){
            Long employeeID = Long.parseLong(txtEmployeeID.getText());
            Date payDate = txtPayDate.getDate();
            penalty.setPayDate(payDate);
            penalty.setEmployeeID(employeeID);
            penalty.setPayStatus(PayStatus.Đã_Thanh_Toán);
        }

        try{
            penaltyBUS.updatePenaltySheet(penalty);
            new AlertDialog(this, "Cập nhật thành công !").setVisible(true);
            this.dispose();
            PenaltyPanel.reloadTable();
            EventBusManager.getEventBus().post(new PenaltyChangeEvent());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Có lỗi khi cập nhật phiếu phạt");
        }
    }//GEN-LAST:event_btnSubmitMouseClicked

    private void btnRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRejectMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnRejectMouseClicked

    private void btnSubmitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmitMouseEntered
        btnSubmit.setBackground(new Color(218,165,32));
    }//GEN-LAST:event_btnSubmitMouseEntered

    private void btnSubmitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmitMouseExited
        btnSubmit.setBackground(new Color(255,255,51));
    }//GEN-LAST:event_btnSubmitMouseExited

    private void btnRejectMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRejectMouseEntered
        btnReject.setBackground(new Color(204,51,0));
    }//GEN-LAST:event_btnRejectMouseEntered

    private void btnRejectMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRejectMouseExited
        btnReject.setBackground(new Color(255,51,51));
    }//GEN-LAST:event_btnRejectMouseExited

   
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JPanel borrowSheetDetailPanel;
    private javax.swing.JButton btnReject;
    private GUI.Component.Button.ButtonChosen btnSearchEmployee;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JPanel employeePanel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headerTitlePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBorrowID;
    private javax.swing.JLabel lblBorrowTitle;
    private javax.swing.JLabel lblEmployeeIDTitle;
    private javax.swing.JLabel lblEmployeeName;
    private javax.swing.JLabel lblEmployeeTitle;
    private javax.swing.JLabel lblPayDate;
    private javax.swing.JLabel lblPenaltyDate;
    private javax.swing.JLabel lblReaderID;
    private javax.swing.JLabel lblReaderName;
    private javax.swing.JLabel lblReaderTitle;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel penaltyInfoPanel;
    private javax.swing.JPanel penaltyStatusPanel;
    private javax.swing.JPanel readerInfoPanel;
    private javax.swing.JPanel sheetDetailPanel;
    private javax.swing.JPanel statusDetailPanel;
    private javax.swing.JPanel submitButtonsPanel;
    private GUI.Component.Table.PenaltyDetailsTable tblPenaltyDetails;
    private javax.swing.JTextField txtEmployeeID;
    private com.toedter.calendar.JDateChooser txtPayDate;
    private com.toedter.calendar.JDateChooser txtPenaltyDate;
    // End of variables declaration//GEN-END:variables
}
