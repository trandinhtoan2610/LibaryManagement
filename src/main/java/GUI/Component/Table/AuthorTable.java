/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Component.Table;
import BUS.AuthorBUS;
import DTO.AuthorDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AuthorTable extends JTableCustom {
    private static final String[] columnsName = {"Mã tác giả", "Họ và tên", "Số lượng tác phẩm"};
    private DefaultTableModel tblModel;
    
    public AuthorTable(){
        super(new DefaultTableModel(columnsName ,0));
        this.tblModel= (DefaultTableModel)getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void resetTable(){
        tblModel.setRowCount(0);
        
        for (AuthorDTO a : AuthorBUS.authorDTOList){
            Object[] rowData = {
                a.getId(),
                a.getLastName() + ' ' + a.getFirstName(),
                a.getProductQuantity()
            };
            
            tblModel.addRow(rowData);
        }
    }
    
    public AuthorDTO getSelectedAuthor(){
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return AuthorBUS.authorDTOList.get(modelRow);
        } 
        return null;
    }
}
