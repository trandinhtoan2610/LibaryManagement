package GUI.Component.Panel;

import BUS.ReaderBUS;
import DTO.ReaderDTO;
import GUI.Component.Dialog.AddnUpdateReaderDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeleteReaderDialog;
import GUI.Component.Table.JTableCustom;
import GUI.Component.Table.ReaderTable;
import GUI.ExcelxPDF.ExcelMaster;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;

import static java.awt.font.TextAttribute.FONT;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class ReaderPanel extends javax.swing.JPanel {
    public static ReaderBUS readerBUS;
    Window parent = SwingUtilities.getWindowAncestor(this);
    private final AlertDialog fixReaderAlert = new AlertDialog(parent,"Vui lòng chọn độc giả cần sửa !");
    private final AlertDialog deleteReaderAlert = new AlertDialog(parent, "Vui lòng chọn độc giả cần xóa !");
    private JFrame parentFrame;
    

       
    public ReaderPanel() {
        parentFrame = new JFrame();
        try {
            System.out.println("Khởi tạo ReaderPanel...");
            initComponents(); 
            
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
            
            txtSearchPhone.getDocument().addDocumentListener(new DocumentListener() {
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
            
            txtSearchAddress.getDocument().addDocumentListener(new DocumentListener() {
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


    public static void reloadReaderTable() {
        tblReader.resetTable();
    }
    
    
    private void exportExcelData() {
        FlatLightLaf.setup();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setSelectedFile(new File("DanhSachDocGia.xlsx"));

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Đảm bảo có đuôi .xlsx
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            if (readerBUS.exportToExcel(filePath)) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Xuất Excel thành công!\nFile: " + filePath,
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Xuất Excel thất bại",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    //Hàm thay đổi bảng khi có tìm kiếm :
    public void loadTableFilter(){
       String name = txtSearchName.getText();
       String phone = txtSearchPhone.getText();
       String address = txtSearchAddress.getText();
       String gender = cbSearchGender.getSelectedIndex() == 0 ? "" : cbSearchGender.getSelectedItem().toString();
     
       tblReader.filterTable(name, phone, address, gender);
       
    }
    
    
    //Hàm thay đổi bảng khi tìm kiếm kết hợp 2 yếu tố :
    
    
 
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
        searchAddressPanel = new javax.swing.JPanel();
        lblSearchAddress = new javax.swing.JLabel();
        txtSearchAddress = new javax.swing.JTextField();
        searchHeaderPanel = new javax.swing.JPanel();
        searchNamePanel = new javax.swing.JPanel();
        lblSearchName = new javax.swing.JLabel();
        txtSearchName = new javax.swing.JTextField();
        searchGenderPanel = new javax.swing.JPanel();
        lblSearchGender = new javax.swing.JLabel();
        cbSearchGender = new javax.swing.JComboBox<>();
        searchBodyPanel = new javax.swing.JPanel();
        searchPhonePanel = new javax.swing.JPanel();
        lblSearchPhone = new javax.swing.JLabel();
        txtSearchPhone = new javax.swing.JTextField();
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

        buttonExportExcel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonExportExcelMouseClicked(evt);
            }
        });
        LeftNavbarReader.add(buttonExportExcel);
        LeftNavbarReader.add(buttonImportExcel1);

        NavbarReaderPanel.add(LeftNavbarReader, java.awt.BorderLayout.LINE_START);

        RightNavbarReader.setBackground(new java.awt.Color(255, 255, 255));
        RightNavbarReader.setLayout(new java.awt.BorderLayout());

        searchFooterPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchFooterPanel.setPreferredSize(new java.awt.Dimension(578, 35));
        searchFooterPanel.setLayout(new java.awt.GridLayout(1, 0));

        searchAddressPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchAddressPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));

        lblSearchAddress.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSearchAddress.setText("Địa chỉ :");
        lblSearchAddress.setPreferredSize(new java.awt.Dimension(50, 25));
        searchAddressPanel.add(lblSearchAddress);

        txtSearchAddress.setPreferredSize(new java.awt.Dimension(370, 25));
        searchAddressPanel.add(txtSearchAddress);

        searchFooterPanel.add(searchAddressPanel);

        RightNavbarReader.add(searchFooterPanel, java.awt.BorderLayout.PAGE_END);

        searchHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchHeaderPanel.setPreferredSize(new java.awt.Dimension(578, 35));
        searchHeaderPanel.setLayout(new java.awt.GridLayout(1, 2));

        searchNamePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchNamePanel.setPreferredSize(new java.awt.Dimension(300, 100));

        lblSearchName.setText("Họ và tên :");
        searchNamePanel.add(lblSearchName);

        txtSearchName.setPreferredSize(new java.awt.Dimension(180, 25));
        searchNamePanel.add(txtSearchName);

        searchHeaderPanel.add(searchNamePanel);

        searchGenderPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchGenderPanel.setPreferredSize(new java.awt.Dimension(2000, 100));

        lblSearchGender.setText("Giới tính :");
        searchGenderPanel.add(lblSearchGender);

        cbSearchGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Nam", "Nữ" }));
        cbSearchGender.setPreferredSize(new java.awt.Dimension(102, 25));
        cbSearchGender.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSearchGenderItemStateChanged(evt);
            }
        });
        searchGenderPanel.add(cbSearchGender);

        searchHeaderPanel.add(searchGenderPanel);

        RightNavbarReader.add(searchHeaderPanel, java.awt.BorderLayout.PAGE_START);

        searchBodyPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchBodyPanel.setPreferredSize(new java.awt.Dimension(200, 20));
        searchBodyPanel.setLayout(new java.awt.GridLayout(1, 2));

        searchPhonePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchPhonePanel.setName(""); // NOI18N
        searchPhonePanel.setPreferredSize(new java.awt.Dimension(300, 100));
        searchPhonePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        lblSearchPhone.setText(" Số ĐT :     ");
        searchPhonePanel.add(lblSearchPhone);

        txtSearchPhone.setPreferredSize(new java.awt.Dimension(180, 25));
        searchPhonePanel.add(txtSearchPhone);

        searchBodyPanel.add(searchPhonePanel);

        RightNavbarReader.add(searchBodyPanel, java.awt.BorderLayout.CENTER);

        NavbarReaderPanel.add(RightNavbarReader, java.awt.BorderLayout.CENTER);

        add(NavbarReaderPanel, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setViewportView(tblReader);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    //Nút thêm -> Hiện dialog thêm
    private void buttonAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonAddMouseClicked
        AddnUpdateReaderDialog addDialog = new AddnUpdateReaderDialog(null,true,"add",null);
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
            AddnUpdateReaderDialog updateDialog = new AddnUpdateReaderDialog(null, true, "update" , reader );
            updateDialog.setVisible(true);
        }
    }//GEN-LAST:event_buttonUpdateMouseClicked

    private void cbSearchGenderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSearchGenderItemStateChanged
        loadTableFilter();
    }//GEN-LAST:event_cbSearchGenderItemStateChanged

    private void buttonExportExcelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonExportExcelMouseClicked
        exportExcelData();
    }//GEN-LAST:event_buttonExportExcelMouseClicked
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LeftNavbarReader;
    private javax.swing.JPanel NavbarReaderPanel;
    private javax.swing.JPanel RightNavbarReader;
    private javax.swing.ButtonGroup btnGroupGender;
    private GUI.Component.Button.ButtonAdd buttonAdd;
    private GUI.Component.Button.ButtonDelete buttonDelete;
    private GUI.Component.Button.ButtonExportExcel buttonExportExcel;
    private GUI.Component.Button.ButtonImportExcel buttonImportExcel1;
    private GUI.Component.Button.ButtonUpdate buttonUpdate;
    private javax.swing.JComboBox<String> cbSearchGender;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSearchAddress;
    private javax.swing.JLabel lblSearchGender;
    private javax.swing.JLabel lblSearchName;
    private javax.swing.JLabel lblSearchPhone;
    private javax.swing.JPanel searchAddressPanel;
    private javax.swing.JPanel searchBodyPanel;
    private javax.swing.JPanel searchFooterPanel;
    private javax.swing.JPanel searchGenderPanel;
    private javax.swing.JPanel searchHeaderPanel;
    private javax.swing.JPanel searchNamePanel;
    private javax.swing.JPanel searchPhonePanel;
    public static GUI.Component.Table.ReaderTable tblReader;
    private javax.swing.JTextField txtSearchAddress;
    private javax.swing.JTextField txtSearchName;
    private javax.swing.JTextField txtSearchPhone;
    // End of variables declaration//GEN-END:variables
}
