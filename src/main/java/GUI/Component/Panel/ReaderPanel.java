package GUI.Component.Panel;

import BUS.ReaderBUS;
import DTO.ReaderDTO;
import GUI.Component.Dialog.AddnUpdateReaderDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeleteReaderDialog;
import GUI.Component.Table.JTableCustom;
import GUI.Component.Table.ReaderTable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import static java.awt.font.TextAttribute.FONT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class ReaderPanel extends javax.swing.JPanel {
    public static ReaderBUS readerBUS;
    Window parent = SwingUtilities.getWindowAncestor(this);
    private final AlertDialog fixReaderAlert = new AlertDialog(parent,"Vui lòng chọn độc giả cần sửa !");
    private final AlertDialog deleteReaderAlert = new AlertDialog(parent, "Vui lòng chọn độc giả cần xóa !");
    

       
    public ReaderPanel() {

        try {
            System.out.println("Khởi tạo ReaderPanel...");
            initComponents(); 
            btnGenderAll.setSelected(true);
            readerBUS = new ReaderBUS();
            if (ReaderBUS.readerList == null) {
                System.err.println("Lỗi: readerList là null, khởi tạo danh sách rỗng.");

            } else {
                System.out.println("Số lượng độc giả: " + ReaderBUS.readerList.size());
            }

            reloadReaderTable();
            //documentListener -> cập nhật table mỗi khi có thay đổi trên textfield 
            txtSearchName.getDocument().addDocumentListener(new DocumentListener(){
                
                @Override
                public void insertUpdate(DocumentEvent e) {
                    loadTableFilter();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    loadTableFilter();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    loadTableFilter();
                }


            });

        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }


    public void reloadReaderTable() {  
        tblReader.resetTable();
    }
    
    //Lấy ra chuỗi giới tính để tìm kiếm :
    public String getGender(){
        if(btnGenderAll.isSelected())
            return "";
        else
            return btnMale.isSelected() ? "Nam" : "Nữ";    
    }
    
    //Hàm thay đổi bảng khi tìm kiếm kết hợp 2 yếu tố :
    public void loadTableFilter(){
        
        String txt = txtSearchName.getText();
        if(txt == "") //Chỉ tìm theo giới tính
            tblReader.filterTable("", getGender(), "", "");
        else{
            if(cbSearchList.getSelectedIndex()==0) //combobox ở index 0 -> Tìm theo tên và giới tính
                tblReader.filterTable(txt,getGender(),"","");
            else if(cbSearchList.getSelectedIndex()==1) // combobox ở index 1 -> Tìm theo số điện thoại và giới tính
                tblReader.filterTable("",getGender(),txt,"");
            else //Lựa chọn còn lại -> Tìm theo địa chỉ và giới tính
                tblReader.filterTable("",getGender(),"",txt);
        }
        tblReader.clearSelection();
    }
    
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupGender = new javax.swing.ButtonGroup();
        NavbarReaderPanel = new javax.swing.JPanel();
        LeftNavbarReader = new javax.swing.JPanel();
        buttonAdd = new GUI.Component.Button.ButtonAdd();
        buttonDelete = new GUI.Component.Button.ButtonDelete();
        buttonUpdate = new GUI.Component.Button.ButtonUpdate();
        buttonExportExcel = new GUI.Component.Button.ButtonExportExcel();
        buttonImportExcel1 = new GUI.Component.Button.ButtonImportExcel();
        RightNavbarReader = new javax.swing.JPanel();
        searchFooterPanel = new javax.swing.JPanel();
        lblGender = new javax.swing.JLabel();
        btnGenderAll = new javax.swing.JRadioButton();
        btnMale = new javax.swing.JRadioButton();
        btnFemale = new javax.swing.JRadioButton();
        searchHeaderPanel = new javax.swing.JPanel();
        lblSearchTitle = new javax.swing.JLabel();
        searchBodyPanel = new javax.swing.JPanel();
        cbSearchList = new javax.swing.JComboBox<>();
        txtSearchName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReader = new GUI.Component.Table.ReaderTable();

        setLayout(new java.awt.BorderLayout(10, 10));

        NavbarReaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        NavbarReaderPanel.setPreferredSize(new java.awt.Dimension(1105, 100));
        NavbarReaderPanel.setLayout(new java.awt.BorderLayout());

        LeftNavbarReader.setBackground(new java.awt.Color(255, 255, 255));
        LeftNavbarReader.setPreferredSize(new java.awt.Dimension(666, 78));
        LeftNavbarReader.setRequestFocusEnabled(false);
        LeftNavbarReader.setLayout(new java.awt.GridLayout(1, 6, 5, 6));

        buttonAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonAddMouseClicked(evt);
            }
        });
        LeftNavbarReader.add(buttonAdd);

        buttonDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonDeleteMouseClicked(evt);
            }
        });
        LeftNavbarReader.add(buttonDelete);

        buttonUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonUpdateMouseClicked(evt);
            }
        });
        LeftNavbarReader.add(buttonUpdate);
        LeftNavbarReader.add(buttonExportExcel);
        LeftNavbarReader.add(buttonImportExcel1);

        NavbarReaderPanel.add(LeftNavbarReader, java.awt.BorderLayout.LINE_START);

        RightNavbarReader.setBackground(new java.awt.Color(255, 255, 255));
        RightNavbarReader.setLayout(new java.awt.BorderLayout());

        searchFooterPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchFooterPanel.setPreferredSize(new java.awt.Dimension(578, 30));
        searchFooterPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 5));

        lblGender.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGender.setText("Giới tính :");
        searchFooterPanel.add(lblGender);

        btnGroupGender.add(btnGenderAll);
        btnGenderAll.setText("Tất cả");
        btnGenderAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGenderAllMouseClicked(evt);
            }
        });
        btnGenderAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenderAllActionPerformed(evt);
            }
        });
        searchFooterPanel.add(btnGenderAll);

        btnGroupGender.add(btnMale);
        btnMale.setText("Nam");
        btnMale.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMaleMouseClicked(evt);
            }
        });
        btnMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaleActionPerformed(evt);
            }
        });
        searchFooterPanel.add(btnMale);

        btnGroupGender.add(btnFemale);
        btnFemale.setText("Nữ");
        btnFemale.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFemaleMouseClicked(evt);
            }
        });
        btnFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFemaleActionPerformed(evt);
            }
        });
        searchFooterPanel.add(btnFemale);

        RightNavbarReader.add(searchFooterPanel, java.awt.BorderLayout.PAGE_END);

        searchHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchHeaderPanel.setPreferredSize(new java.awt.Dimension(578, 30));
        searchHeaderPanel.setLayout(new java.awt.BorderLayout());

        lblSearchTitle.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblSearchTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearchTitle.setText("Tìm kiếm");
        searchHeaderPanel.add(lblSearchTitle, java.awt.BorderLayout.PAGE_END);

        RightNavbarReader.add(searchHeaderPanel, java.awt.BorderLayout.PAGE_START);

        searchBodyPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchBodyPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));

        cbSearchList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Họ và tên", "Số điện thoại", "Địa chỉ", " " }));
        cbSearchList.setPreferredSize(new java.awt.Dimension(102, 30));
        cbSearchList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSearchListItemStateChanged(evt);
            }
        });
        cbSearchList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSearchListActionPerformed(evt);
            }
        });
        searchBodyPanel.add(cbSearchList);

        txtSearchName.setPreferredSize(new java.awt.Dimension(250, 30));
        txtSearchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchNameActionPerformed(evt);
            }
        });
        searchBodyPanel.add(txtSearchName);

        RightNavbarReader.add(searchBodyPanel, java.awt.BorderLayout.CENTER);

        NavbarReaderPanel.add(RightNavbarReader, java.awt.BorderLayout.CENTER);

        add(NavbarReaderPanel, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setViewportView(tblReader);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    //Nút thêm -> Hiện dialog thêm
    private void buttonAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonAddMouseClicked
        AddnUpdateReaderDialog addDialog = new AddnUpdateReaderDialog(null,true,"add",this,null);
        addDialog.setVisible(true);
    }//GEN-LAST:event_buttonAddMouseClicked

    //Nút xóa -> Hiện dialog xóa
    private void buttonDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonDeleteMouseClicked
        ReaderDTO r = tblReader.getSelectedReader();
        if(r == null ){
            deleteReaderAlert.setVisible(true);
        }
        else {
            DeleteReaderDialog confirmDeleteDialog = new DeleteReaderDialog(null, true, r, this);
            confirmDeleteDialog.setVisible(true);
        }
    }//GEN-LAST:event_buttonDeleteMouseClicked

    //Nút cập nhật -> Hiện dialog cập nhật
    private void buttonUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonUpdateMouseClicked
        if(tblReader.getSelectedReader() == null ){
            fixReaderAlert.setVisible(true);
        }
        else{
            ReaderDTO reader = tblReader.getSelectedReader();
            AddnUpdateReaderDialog updateDialog = new AddnUpdateReaderDialog(null, true, "update" , this, reader );
            updateDialog.setVisible(true);
        }
    }//GEN-LAST:event_buttonUpdateMouseClicked

    private void txtSearchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchNameActionPerformed

    private void cbSearchListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSearchListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSearchListActionPerformed

    private void btnMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaleActionPerformed

    }//GEN-LAST:event_btnMaleActionPerformed

    private void btnFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFemaleActionPerformed

    }//GEN-LAST:event_btnFemaleActionPerformed

    private void btnMaleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaleMouseClicked
        loadTableFilter();
    }//GEN-LAST:event_btnMaleMouseClicked

    private void btnGenderAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenderAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGenderAllActionPerformed
    
    //reset textfield mỗi khi có thay đổi lựa chọn combobox :
    private void cbSearchListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSearchListItemStateChanged
        txtSearchName.setText("");
    }//GEN-LAST:event_cbSearchListItemStateChanged

    private void btnGenderAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGenderAllMouseClicked
        loadTableFilter();
    }//GEN-LAST:event_btnGenderAllMouseClicked

    private void btnFemaleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFemaleMouseClicked
        loadTableFilter();
    }//GEN-LAST:event_btnFemaleMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LeftNavbarReader;
    private javax.swing.JPanel NavbarReaderPanel;
    private javax.swing.JPanel RightNavbarReader;
    private javax.swing.JRadioButton btnFemale;
    private javax.swing.JRadioButton btnGenderAll;
    private javax.swing.ButtonGroup btnGroupGender;
    private javax.swing.JRadioButton btnMale;
    private GUI.Component.Button.ButtonAdd buttonAdd;
    private GUI.Component.Button.ButtonDelete buttonDelete;
    private GUI.Component.Button.ButtonExportExcel buttonExportExcel;
    private GUI.Component.Button.ButtonImportExcel buttonImportExcel1;
    private GUI.Component.Button.ButtonUpdate buttonUpdate;
    private javax.swing.JComboBox<String> cbSearchList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblSearchTitle;
    private javax.swing.JPanel searchBodyPanel;
    private javax.swing.JPanel searchFooterPanel;
    private javax.swing.JPanel searchHeaderPanel;
    private GUI.Component.Table.ReaderTable tblReader;
    private javax.swing.JTextField txtSearchName;
    // End of variables declaration//GEN-END:variables
}
