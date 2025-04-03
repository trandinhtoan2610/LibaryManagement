package GUI.Component.Table;


import DTO.AuthorBookDTO;

import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class AuthorProductsTable extends JTableCustom{
    private static final String[] columnsName = {"Mã sách", "Tên sách", "Thể loại"};
    private DefaultTableModel tblModel;
    
    public AuthorProductsTable(){
        super(new DefaultTableModel(columnsName ,0));
        this.tblModel= (DefaultTableModel)getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void resetTable(List<AuthorBookDTO> list){
         tblModel.setRowCount(0);
         if(list == null){
             return;
         }
         for (AuthorBookDTO book : list){
             Object[] data = {
                 book.getiD(),
                 book.getName(),
                 book.getCategory(),
             };
             
             tblModel.addRow(data);
         }
        this.revalidate();
        this.repaint();
    }
    
    
}
