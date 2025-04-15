package GUI.Component.Table;


import DTO.AuthorBookDTO;
import DTO.BookViewModel;

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
    
    public void resetTable(List<BookViewModel> list){
         tblModel.setRowCount(0);
         if(list == null){
             return;
         }
         for (BookViewModel book : list){
             Object[] data = {
                 book.getId(),
                 book.getName(),
                 book.getCategoryName()
             };
             
             tblModel.addRow(data);
         }
        this.revalidate();
        this.repaint();
    }
    
    
}
