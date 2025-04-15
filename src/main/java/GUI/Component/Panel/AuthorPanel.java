
package GUI.Component.Panel;

import BUS.AuthorBUS;
import BUS.BookBUS;
import DTO.AuthorDTO;
import DTO.AuthorBookDTO;
import DTO.BookViewModel;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.UpdateAuthorDialog;
import java.awt.Window;
import javax.swing.SwingUtilities;
import GUI.Controller.Controller;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class AuthorPanel extends javax.swing.JPanel {
    private AuthorBUS authorBUS;
    private BookBUS bookBUS;
    Window parent = SwingUtilities.getWindowAncestor(this);
    AlertDialog updateAlertDialog = new AlertDialog(parent,"Vui lòng chọn tác giả cần sửa");
    
    
    public AuthorPanel() {
        try {
            System.out.println("Khởi tạo Author Panel...");
            initComponents();
            authorBUS = new AuthorBUS();
            bookBUS = new BookBUS();
            if(AuthorBUS.authorDTOList == null )
                System.out.println("Danh sách đang rỗng, chưa được khởi tạo !!");
            else
                System.out.println("Số lượng tác giả : " + AuthorBUS.authorDTOList.size());
            
            reloadTable();
            authorProductsTable1.resetTable(null);
            lblAuthorName.setText("");
            lblSubTitle.setText("");
            //documentListener -> load lại table mỗi khi có thay đổi trên textfield
            txtSearchAuthorName.getDocument().addDocumentListener(new DocumentListener(){
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    //Cập nhật lại bảng sau khi thêm, sửa, xóa :
    public static void reloadTable(){
        tblAuthor.resetTable();
    }
    
    //Cập nhật lại bảng sau khi lọc theo tìm kiếm : 
    public void loadTableFilter(){
        String searchedName = txtSearchAuthorName.getText();
        tblAuthor.filterTable(searchedName);
        tblAuthor.clearSelection();
        authorProductsTable1.resetTable(null);
        lblAuthorName.setText("");
        lblSubTitle.setText("");
           
    }
    

   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        navbarAuthorPanel = new javax.swing.JPanel();
        leftNavbarAuthorPanel = new javax.swing.JPanel();
        btnUpdate = new GUI.Component.Button.ButtonUpdate();
        btnExportExcel = new GUI.Component.Button.ButtonExportExcel();
        buttonImportExcel1 = new GUI.Component.Button.ButtonImportExcel();
        rightNavbarAuthorPanel = new javax.swing.JPanel();
        headerSearchPanel = new javax.swing.JPanel();
        lblSearchTitle = new javax.swing.JLabel();
        bodySearchPanel = new javax.swing.JPanel();
        lblSearchByName = new javax.swing.JLabel();
        txtSearchAuthorName = new javax.swing.JTextField();
        footerSearchPanel = new javax.swing.JPanel();
        footerAuthorPannel = new javax.swing.JPanel();
        tblProductsPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        authorProductsTable1 = new GUI.Component.Table.AuthorProductsTable();
        jPanel3 = new javax.swing.JPanel();
        lblFootAuthor = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblAuthorName = new javax.swing.JLabel();
        lblSubTitle = new javax.swing.JLabel();
        tblAuthorPanel = new javax.swing.JPanel();
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
        leftNavbarAuthorPanel.add(btnExportExcel);
        leftNavbarAuthorPanel.add(buttonImportExcel1);

        navbarAuthorPanel.add(leftNavbarAuthorPanel, java.awt.BorderLayout.LINE_START);

        rightNavbarAuthorPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightNavbarAuthorPanel.setLayout(new java.awt.GridLayout(3, 0));

        headerSearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerSearchPanel.setLayout(new java.awt.BorderLayout());

        lblSearchTitle.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblSearchTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearchTitle.setText("Tìm kiếm ");
        headerSearchPanel.add(lblSearchTitle, java.awt.BorderLayout.CENTER);

        rightNavbarAuthorPanel.add(headerSearchPanel);

        bodySearchPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblSearchByName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSearchByName.setText("Tìm theo tên :");
        lblSearchByName.setPreferredSize(new java.awt.Dimension(100, 30));
        bodySearchPanel.add(lblSearchByName);

        txtSearchAuthorName.setPreferredSize(new java.awt.Dimension(300, 30));
        bodySearchPanel.add(txtSearchAuthorName);

        rightNavbarAuthorPanel.add(bodySearchPanel);

        footerSearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightNavbarAuthorPanel.add(footerSearchPanel);

        navbarAuthorPanel.add(rightNavbarAuthorPanel, java.awt.BorderLayout.CENTER);

        add(navbarAuthorPanel, java.awt.BorderLayout.PAGE_START);

        footerAuthorPannel.setBackground(new java.awt.Color(255, 255, 255));
        footerAuthorPannel.setPreferredSize(new java.awt.Dimension(100, 200));
        footerAuthorPannel.setLayout(new java.awt.BorderLayout());

        tblProductsPanel.setPreferredSize(new java.awt.Dimension(850, 100));
        tblProductsPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setViewportView(authorProductsTable1);

        tblProductsPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        footerAuthorPannel.add(tblProductsPanel, java.awt.BorderLayout.LINE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(2, 0));

        lblFootAuthor.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        lblFootAuthor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFootAuthor.setText("DANH SÁCH TÁC PHẨM CỦA TÁC GIẢ");
        lblFootAuthor.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel3.add(lblFootAuthor);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblAuthorName.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        lblAuthorName.setForeground(new java.awt.Color(255, 0, 51));
        lblAuthorName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAuthorName.setText("<<Tác giả>>");
        lblAuthorName.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel1.add(lblAuthorName);

        lblSubTitle.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        lblSubTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSubTitle.setText("có trong thư viện");
        jPanel1.add(lblSubTitle);

        jPanel3.add(jPanel1);

        footerAuthorPannel.add(jPanel3, java.awt.BorderLayout.CENTER);

        add(footerAuthorPannel, java.awt.BorderLayout.PAGE_END);

        tblAuthorPanel.setBackground(new java.awt.Color(255, 255, 255));
        tblAuthorPanel.setLayout(new java.awt.BorderLayout());

        tblAuthor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAuthorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAuthor);

        tblAuthorPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(tblAuthorPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    //Nút cập nhật -> Dialog cập nhật :
    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        AuthorDTO a = tblAuthor.getSelectedAuthor();
        if(a == null)
            updateAlertDialog.setVisible(true);
        else{
            UpdateAuthorDialog updateDialog = new UpdateAuthorDialog(null,true,a,this);
            updateDialog.setVisible(true);
        }
    }//GEN-LAST:event_btnUpdateMouseClicked
    
    //Chọn 1 dòng trong bảng tác giả -> hiển thị các tác phẩm của tác giả ở bảng dưới : 
    private void tblAuthorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAuthorMouseClicked
        AlertDialog add = new AlertDialog(parent,"ngu");
        add.setVisible(true);
        int selectedRow = tblAuthor.getSelectedRow();
        if(selectedRow >= 0 ){        
           AuthorDTO a = tblAuthor.getSelectedAuthor();
           Long authorID = a.getId();
           String fullName =  Controller.formatFullName(a.getLastName() + ' ' + a.getFirstName());
           lblAuthorName.setText(fullName);
           lblSubTitle.setText("có trong thư viện");
           List<BookViewModel> bookList = bookBUS.getBooksByAuthorID(authorID);
           authorProductsTable1.resetTable(bookList);
           
        }
    }//GEN-LAST:event_tblAuthorMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.Component.Table.AuthorProductsTable authorProductsTable1;
    private javax.swing.JPanel bodySearchPanel;
    private GUI.Component.Button.ButtonExportExcel btnExportExcel;
    private GUI.Component.Button.ButtonUpdate btnUpdate;
    private GUI.Component.Button.ButtonImportExcel buttonImportExcel1;
    private javax.swing.JPanel footerAuthorPannel;
    private javax.swing.JPanel footerSearchPanel;
    private javax.swing.JPanel headerSearchPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAuthorName;
    private javax.swing.JLabel lblFootAuthor;
    private javax.swing.JLabel lblSearchByName;
    private javax.swing.JLabel lblSearchTitle;
    private javax.swing.JLabel lblSubTitle;
    private javax.swing.JPanel leftNavbarAuthorPanel;
    private javax.swing.JPanel navbarAuthorPanel;
    private javax.swing.JPanel rightNavbarAuthorPanel;
    public static GUI.Component.Table.AuthorTable tblAuthor;
    private javax.swing.JPanel tblAuthorPanel;
    private javax.swing.JPanel tblProductsPanel;
    private javax.swing.JTextField txtSearchAuthorName;
    // End of variables declaration//GEN-END:variables
}
