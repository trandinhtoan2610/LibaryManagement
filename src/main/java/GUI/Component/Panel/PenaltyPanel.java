/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.Component.Panel;

import BUS.*;
import DTO.*;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeletePenaltyDialog;
import GUI.Component.Dialog.PenaltyFilterDialog;
import GUI.Component.Dialog.UpdatePenaltyDialog;
import GUI.Controller.Controller;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PenaltyPanel extends javax.swing.JPanel {
    private PenaltyBUS penaltyBUS;
    private EmployeeBUS employeeBUS;
    private JFrame parentFrame;
  
    public PenaltyPanel() {
        parentFrame = this.parentFrame;
        penaltyBUS = new PenaltyBUS();
        employeeBUS = new EmployeeBUS();
        initComponents();
        reloadTable();

        txtSearchPenalty.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
    }

    public static void reloadTable(){
        tblPenalty.clearSelection();
        tblPenalty.resetTable();
    }

    public static void advanceFilterTable(List<PenaltyDTO> filterList){
        tblPenalty.clearSelection();
        tblPenalty.advanceFilterTable(filterList);
    }

    public void clearDetailsInfo(){
        lblBorrowID.setText("");
        lblEmployeeInfo.setText("");
        lblReaderID.setText("");
        lblReaderName.setText("");
        lblReaderPhone.setText("");
        lblReaderAddress.setText("");
        lblReaderGender.setText("");
        tblPenaltyDetails.resetTable(null);
    }

    public void filterTable(){
        String penaltyID = txtSearchPenalty.getText();
        tblPenalty.filterTable(penaltyID);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupStatus = new javax.swing.ButtonGroup();
        headerPanel = new javax.swing.JPanel();
        leftHeaderPanel = new javax.swing.JPanel();
        btnUpdate = new GUI.Component.Button.ButtonUpdate();
        btnDelete = new GUI.Component.Button.ButtonDelete();
        btnExportPDF = new GUI.Component.Button.ButtonExportPDF();
        btnExportExcel = new GUI.Component.Button.ButtonExportExcel();
        rightHeaderPanel = new javax.swing.JPanel();
        topSearchPanel = new javax.swing.JPanel();
        lblSearchPenalty = new javax.swing.JLabel();
        txtSearchPenalty = new javax.swing.JTextField();
        btnFilter = new GUI.Component.Button.ButtonFilter();
        btnRefresh = new GUI.Component.Button.ButtonRefresh();
        footerPanel = new javax.swing.JPanel();
        rightFooterPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPenaltyDetails = new GUI.Component.Table.PenaltyDetailsTable();
        leftFooterPanel = new javax.swing.JPanel();
        sheetDetailPanel = new javax.swing.JPanel();
        lblEmployeeTitle = new javax.swing.JLabel();
        lblBorrowTitle = new javax.swing.JLabel();
        lblBorrowID = new javax.swing.JLabel();
        lblEmployeeInfo = new javax.swing.JLabel();
        readerDetailPanel = new javax.swing.JPanel();
        lblTitleReaderID = new javax.swing.JLabel();
        lblTitileReaderName = new javax.swing.JLabel();
        lblTitleReaderPhone = new javax.swing.JLabel();
        lblTitleReaderGender = new javax.swing.JLabel();
        lblTitleReaderAddress = new javax.swing.JLabel();
        lblReaderID = new javax.swing.JLabel();
        lblReaderGender = new javax.swing.JLabel();
        lblReaderPhone = new javax.swing.JLabel();
        lblReaderAddress = new javax.swing.JLabel();
        lblReaderName = new javax.swing.JLabel();
        bodyPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPenalty = new GUI.Component.Table.PenaltyTable();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setPreferredSize(new java.awt.Dimension(868, 120));
        headerPanel.setLayout(new java.awt.BorderLayout());

        leftHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        leftHeaderPanel.setPreferredSize(new java.awt.Dimension(550, 120));
        leftHeaderPanel.setLayout(new java.awt.GridLayout(1, 0));

        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });
        leftHeaderPanel.add(btnUpdate);

        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteMouseClicked(evt);
            }
        });
        leftHeaderPanel.add(btnDelete);
        leftHeaderPanel.add(btnExportPDF);
        leftHeaderPanel.add(btnExportExcel);

        headerPanel.add(leftHeaderPanel, java.awt.BorderLayout.LINE_START);

        rightHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightHeaderPanel.setLayout(new java.awt.BorderLayout());

        topSearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        topSearchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 12, 48));

        lblSearchPenalty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSearchPenalty.setText("Mã phiếu phạt  :");
        lblSearchPenalty.setPreferredSize(new java.awt.Dimension(110, 30));
        topSearchPanel.add(lblSearchPenalty);

        txtSearchPenalty.setPreferredSize(new java.awt.Dimension(170, 30));
        txtSearchPenalty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchPenaltyActionPerformed(evt);
            }
        });
        topSearchPanel.add(txtSearchPenalty);

        btnFilter.setPreferredSize(new java.awt.Dimension(55, 35));
        btnFilter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFilterMouseClicked(evt);
            }
        });
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });
        topSearchPanel.add(btnFilter);

        btnRefresh.setMaximumSize(new java.awt.Dimension(50, 35));
        btnRefresh.setPreferredSize(new java.awt.Dimension(50, 35));
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        topSearchPanel.add(btnRefresh);

        rightHeaderPanel.add(topSearchPanel, java.awt.BorderLayout.CENTER);

        headerPanel.add(rightHeaderPanel, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.PAGE_START);

        footerPanel.setBackground(new java.awt.Color(255, 255, 255));
        footerPanel.setPreferredSize(new java.awt.Dimension(1055, 220));
        footerPanel.setLayout(new java.awt.BorderLayout());

        rightFooterPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightFooterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết phiếu phạt", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 2, 12))); // NOI18N
        rightFooterPanel.setPreferredSize(new java.awt.Dimension(650, 220));
        rightFooterPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setViewportView(tblPenaltyDetails);

        rightFooterPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        footerPanel.add(rightFooterPanel, java.awt.BorderLayout.LINE_END);

        leftFooterPanel.setBackground(new java.awt.Color(255, 255, 255));
        leftFooterPanel.setLayout(new java.awt.BorderLayout());

        sheetDetailPanel.setBackground(new java.awt.Color(255, 255, 255));
        sheetDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chung", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        sheetDetailPanel.setPreferredSize(new java.awt.Dimension(407, 80));

        lblEmployeeTitle.setBackground(new java.awt.Color(0, 0, 0));
        lblEmployeeTitle.setText("Nhân viên xử lý :");

        lblBorrowTitle.setBackground(new java.awt.Color(102, 102, 102));
        lblBorrowTitle.setText("Từ phiếu mượn :");

        javax.swing.GroupLayout sheetDetailPanelLayout = new javax.swing.GroupLayout(sheetDetailPanel);
        sheetDetailPanel.setLayout(sheetDetailPanelLayout);
        sheetDetailPanelLayout.setHorizontalGroup(
            sheetDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sheetDetailPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(sheetDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblBorrowTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEmployeeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sheetDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBorrowID, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(lblEmployeeInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                .addContainerGap())
        );
        sheetDetailPanelLayout.setVerticalGroup(
            sheetDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sheetDetailPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sheetDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBorrowTitle)
                    .addComponent(lblBorrowID, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sheetDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmployeeInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmployeeTitle))
                .addContainerGap())
        );

        leftFooterPanel.add(sheetDetailPanel, java.awt.BorderLayout.PAGE_START);

        readerDetailPanel.setBackground(new java.awt.Color(255, 255, 255));
        readerDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin độc giả", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 2, 12))); // NOI18N

        lblTitleReaderID.setText("Mã độc giả :");

        lblTitileReaderName.setText("Họ và tên :");

        lblTitleReaderPhone.setText("SĐT :");

        lblTitleReaderGender.setText("Giới tính :");

        lblTitleReaderAddress.setText("Địa chỉ :");

        javax.swing.GroupLayout readerDetailPanelLayout = new javax.swing.GroupLayout(readerDetailPanel);
        readerDetailPanel.setLayout(readerDetailPanelLayout);
        readerDetailPanelLayout.setHorizontalGroup(
            readerDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(readerDetailPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(readerDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(readerDetailPanelLayout.createSequentialGroup()
                        .addComponent(lblTitleReaderAddress)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReaderAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(readerDetailPanelLayout.createSequentialGroup()
                        .addComponent(lblTitleReaderID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReaderID, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTitileReaderName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReaderName, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                    .addGroup(readerDetailPanelLayout.createSequentialGroup()
                        .addComponent(lblTitleReaderGender)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReaderGender, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(readerDetailPanelLayout.createSequentialGroup()
                        .addComponent(lblTitleReaderPhone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReaderPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        readerDetailPanelLayout.setVerticalGroup(
            readerDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(readerDetailPanelLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(readerDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblReaderID, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitleReaderID)
                    .addComponent(lblTitileReaderName)
                    .addComponent(lblReaderName, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(readerDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReaderGender, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitleReaderGender))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(readerDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitleReaderPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblReaderPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(readerDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReaderAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTitleReaderAddress))
                .addContainerGap())
        );

        leftFooterPanel.add(readerDetailPanel, java.awt.BorderLayout.CENTER);

        footerPanel.add(leftFooterPanel, java.awt.BorderLayout.CENTER);

        add(footerPanel, java.awt.BorderLayout.PAGE_END);

        bodyPanel.setBackground(new java.awt.Color(255, 255, 255));
        bodyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách phiếu phạt", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 14))); // NOI18N
        bodyPanel.setLayout(new java.awt.BorderLayout());

        tblPenalty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPenaltyMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblPenaltyMouseExited(evt);
            }
        });
        jScrollPane1.setViewportView(tblPenalty);

        bodyPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(bodyPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void tblPenaltyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPenaltyMouseClicked
        int selectedViewRow = tblPenalty.getSelectedRow();
        if (selectedViewRow >= 0){
            int modelRow = tblPenalty.convertRowIndexToModel(selectedViewRow);
            PenaltyDTO p = tblPenalty.getSelectedPenalty();
            List<PenaltyDetailsDTO> detailList = penaltyBUS.getPenaltyDetails(p);
            tblPenaltyDetails.resetTable(detailList);
            BorrowDTO borrowDTO = penaltyBUS.getPenaltyBorrowSheet(p); //Lấy ra phiếu mượn
            ReaderDTO readerDTO = penaltyBUS.getPenaltyReader(p); //Lấy ra độc giả

            System.out.println(p.getEmployeeID());
            lblBorrowID.setText(borrowDTO.getId());
            if(p.getEmployeeID() != null ){
                Employee employee = employeeBUS.getEmployeeById(p.getEmployeeID());
                String employeeName = Controller.formatFullName(employee.getFirstName() + " " + employee.getLastName());
                lblEmployeeInfo.setText(employeeName + " - Mã nhân viên : " + employee.getId().toString()) ;
            }
            else {
                lblEmployeeInfo.setText("");
            }

            lblReaderID.setText(readerDTO.getId());
            lblReaderName.setText(Controller.formatFullName(readerDTO.getLastName() + " " + readerDTO.getFirstName()));
            lblReaderGender.setText(readerDTO.getGender().name());
            lblReaderPhone.setText(readerDTO.getPhone());
            lblReaderAddress.setText(readerDTO.getAddress());
        }
        else{
            clearDetailsInfo();
        }
    }//GEN-LAST:event_tblPenaltyMouseClicked

    private void tblPenaltyMouseExited(java.awt.event.MouseEvent evt) {
        tblPenaltyDetails.resetTable(null);
    }

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        PenaltyDTO p = tblPenalty.getSelectedPenalty();
        if(p == null){
            new AlertDialog(parentFrame,"Vui lòng chọn phiếu phạt cần chỉnh sửa").setVisible(true);
        }
        else {
            ReaderDTO reader = penaltyBUS.getPenaltyReader(p);
            List<PenaltyDetailsDTO> penaltyDetails = penaltyBUS.getPenaltyDetails(p);
            BorrowDTO borrowDTO = penaltyBUS.getPenaltyBorrowSheet(p);

            UpdatePenaltyDialog updatePenaltyDialog = new UpdatePenaltyDialog(parentFrame, true,
                    p, penaltyDetails, reader, borrowDTO);
            updatePenaltyDialog.setVisible(true);

        }
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void btnDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseClicked
        PenaltyDTO p = tblPenalty.getSelectedPenalty();
        if(p == null ){
            new AlertDialog(parentFrame, "Vui lòng chọn phiếu phạt cần xóa !").setVisible(true);
        }
        else{
            DeletePenaltyDialog deletePenaltyDialog = new DeletePenaltyDialog(parentFrame,true,p, false);
            deletePenaltyDialog.setVisible(true);
        }
    }//GEN-LAST:event_btnDeleteMouseClicked

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        filterTable();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void txtSearchPenaltyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchPenaltyActionPerformed
        filterTable();
    }//GEN-LAST:event_txtSearchPenaltyActionPerformed

    private void btnFilterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFilterMouseClicked
        txtSearchPenalty.setText("");
        PenaltyFilterDialog penaltyFilterDialog = new PenaltyFilterDialog(parentFrame, true);
        penaltyFilterDialog.setVisible(true);
        
    }//GEN-LAST:event_btnFilterMouseClicked

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        reloadTable();
    }//GEN-LAST:event_btnRefreshMouseClicked



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodyPanel;
    private GUI.Component.Button.ButtonDelete btnDelete;
    private GUI.Component.Button.ButtonExportExcel btnExportExcel;
    private GUI.Component.Button.ButtonExportPDF btnExportPDF;
    private GUI.Component.Button.ButtonFilter btnFilter;
    private javax.swing.ButtonGroup btnGroupStatus;
    private GUI.Component.Button.ButtonRefresh btnRefresh;
    private GUI.Component.Button.ButtonUpdate btnUpdate;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBorrowID;
    private javax.swing.JLabel lblBorrowTitle;
    private javax.swing.JLabel lblEmployeeInfo;
    private javax.swing.JLabel lblEmployeeTitle;
    private javax.swing.JLabel lblReaderAddress;
    private javax.swing.JLabel lblReaderGender;
    private javax.swing.JLabel lblReaderID;
    private javax.swing.JLabel lblReaderName;
    private javax.swing.JLabel lblReaderPhone;
    private javax.swing.JLabel lblSearchPenalty;
    private javax.swing.JLabel lblTitileReaderName;
    private javax.swing.JLabel lblTitleReaderAddress;
    private javax.swing.JLabel lblTitleReaderGender;
    private javax.swing.JLabel lblTitleReaderID;
    private javax.swing.JLabel lblTitleReaderPhone;
    private javax.swing.JPanel leftFooterPanel;
    private javax.swing.JPanel leftHeaderPanel;
    private javax.swing.JPanel readerDetailPanel;
    private javax.swing.JPanel rightFooterPanel;
    private javax.swing.JPanel rightHeaderPanel;
    private javax.swing.JPanel sheetDetailPanel;
    public static GUI.Component.Table.PenaltyTable tblPenalty;
    private GUI.Component.Table.PenaltyDetailsTable tblPenaltyDetails;
    private javax.swing.JPanel topSearchPanel;
    private javax.swing.JTextField txtSearchPenalty;
    // End of variables declaration//GEN-END:variables
}
