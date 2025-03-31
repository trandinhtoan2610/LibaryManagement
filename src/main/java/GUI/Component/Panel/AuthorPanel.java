
package GUI.Component.Panel;

import BUS.AuthorBUS;
import DTO.AuthorDTO;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.UpdateAuthorDialog;
import java.awt.Window;
import javax.swing.SwingUtilities;


public class AuthorPanel extends javax.swing.JPanel {
    AuthorBUS authorBUS;
    Window parent = SwingUtilities.getWindowAncestor(this);
    AlertDialog updateAlertDialog = new AlertDialog(parent,"Vui lòng chọn tác giả cần sửa");
    
    
    public AuthorPanel() {
        try {
            System.out.println("Khởi tạo Author Panel...");
            initComponents();
            authorBUS = new AuthorBUS();
            if(AuthorBUS.authorDTOList == null )
                System.out.println("Danh sách đang rỗng, chưa được khởi tạo !!");
            else
                System.out.println("Số lượng tác giả : " + AuthorBUS.authorDTOList.size());
            
            reloadTable();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void reloadTable(){
        tblAuthor.resetTable();
    }

   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        navbarAuthorPanel = new javax.swing.JPanel();
        leftNavbarAuthorPanel = new javax.swing.JPanel();
        btnUpdate = new GUI.Component.Button.ButtonUpdate();
        btnDetails = new GUI.Component.Button.ButtonDetails();
        btnExportExcel = new GUI.Component.Button.ButtonExportExcel();
        footerAuthorPannel = new javax.swing.JPanel();
        tblContainer = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAuthorProducts = new javax.swing.JTable();
        leftFooterPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAuthor = new GUI.Component.Table.AuthorTable();

        setBackground(new java.awt.Color(102, 153, 255));
        setLayout(new java.awt.BorderLayout(0, 4));

        navbarAuthorPanel.setBackground(new java.awt.Color(255, 255, 255));
        navbarAuthorPanel.setLayout(new java.awt.BorderLayout(5, 5));

        leftNavbarAuthorPanel.setBackground(new java.awt.Color(255, 255, 255));
        leftNavbarAuthorPanel.setPreferredSize(new java.awt.Dimension(400, 100));
        leftNavbarAuthorPanel.setLayout(new java.awt.GridLayout(1, 0));

        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });
        leftNavbarAuthorPanel.add(btnUpdate);
        leftNavbarAuthorPanel.add(btnDetails);
        leftNavbarAuthorPanel.add(btnExportExcel);

        navbarAuthorPanel.add(leftNavbarAuthorPanel, java.awt.BorderLayout.LINE_START);

        add(navbarAuthorPanel, java.awt.BorderLayout.PAGE_START);

        footerAuthorPannel.setBackground(new java.awt.Color(255, 255, 255));
        footerAuthorPannel.setPreferredSize(new java.awt.Dimension(100, 200));
        footerAuthorPannel.setLayout(new java.awt.BorderLayout());

        tblContainer.setPreferredSize(new java.awt.Dimension(700, 100));
        tblContainer.setLayout(new java.awt.BorderLayout());

        tblAuthorProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã sách", "Tên sách", "Thể loại"
            }
        ));
        jScrollPane2.setViewportView(tblAuthorProducts);

        tblContainer.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        footerAuthorPannel.add(tblContainer, java.awt.BorderLayout.LINE_END);

        leftFooterPanel.setBackground(new java.awt.Color(255, 255, 255));
        footerAuthorPannel.add(leftFooterPanel, java.awt.BorderLayout.CENTER);

        add(footerAuthorPannel, java.awt.BorderLayout.PAGE_END);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(tblAuthor);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        AuthorDTO a = tblAuthor.getSelectedAuthor();
        if(a == null)
            updateAlertDialog.setVisible(true);
        else{
            UpdateAuthorDialog updateDialog = new UpdateAuthorDialog(null,true,a,this);
            updateDialog.setVisible(true);
        }
    }//GEN-LAST:event_btnUpdateMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.Component.Button.ButtonDetails btnDetails;
    private GUI.Component.Button.ButtonExportExcel btnExportExcel;
    private GUI.Component.Button.ButtonUpdate btnUpdate;
    private javax.swing.JPanel footerAuthorPannel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel leftFooterPanel;
    private javax.swing.JPanel leftNavbarAuthorPanel;
    private javax.swing.JPanel navbarAuthorPanel;
    private GUI.Component.Table.AuthorTable tblAuthor;
    private javax.swing.JTable tblAuthorProducts;
    private javax.swing.JPanel tblContainer;
    // End of variables declaration//GEN-END:variables
}
