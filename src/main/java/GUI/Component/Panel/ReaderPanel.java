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
        readerBUS = new ReaderBUS();
        if (ReaderBUS.readerList == null) {
            System.err.println("Lỗi: readerList là null, khởi tạo danh sách rỗng.");
            
        } else {
            System.out.println("Số lượng độc giả: " + ReaderBUS.readerList.size());
        }

        reloadReaderTable();
    } catch (Exception e) {
        e.printStackTrace(); 
    }
}


    public void reloadReaderTable() {  
        tblReader.resetTable();
    }
    
   
        
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonExportExcel1 = new GUI.Component.Button.ButtonExportExcel();
        NavbarReaderPanel = new javax.swing.JPanel();
        LeftNavbarReader = new javax.swing.JPanel();
        buttonAdd = new GUI.Component.Button.ButtonAdd();
        buttonDelete = new GUI.Component.Button.ButtonDelete();
        buttonUpdate = new GUI.Component.Button.ButtonUpdate();
        buttonExportExcel = new GUI.Component.Button.ButtonExportExcel();
        RightNavbarReader = new javax.swing.JPanel();
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

        NavbarReaderPanel.add(LeftNavbarReader, java.awt.BorderLayout.LINE_START);

        RightNavbarReader.setBackground(new java.awt.Color(255, 255, 255));
        NavbarReaderPanel.add(RightNavbarReader, java.awt.BorderLayout.CENTER);

        add(NavbarReaderPanel, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setViewportView(tblReader);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonAddMouseClicked
        AddnUpdateReaderDialog addDialog = new AddnUpdateReaderDialog(null,true,"add",this,null);
        addDialog.setVisible(true);
    }//GEN-LAST:event_buttonAddMouseClicked

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LeftNavbarReader;
    private javax.swing.JPanel NavbarReaderPanel;
    private javax.swing.JPanel RightNavbarReader;
    private GUI.Component.Button.ButtonAdd buttonAdd;
    private GUI.Component.Button.ButtonDelete buttonDelete;
    private GUI.Component.Button.ButtonExportExcel buttonExportExcel;
    private GUI.Component.Button.ButtonExportExcel buttonExportExcel1;
    private GUI.Component.Button.ButtonUpdate buttonUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private GUI.Component.Table.ReaderTable tblReader;
    // End of variables declaration//GEN-END:variables
}
