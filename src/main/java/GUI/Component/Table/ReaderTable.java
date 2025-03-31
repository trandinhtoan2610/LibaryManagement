package GUI.Component.Table;

import BUS.ReaderBUS;
import DTO.ReaderDTO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ReaderTable extends JTableCustom {
    private static final String[] readerTableHeader = {"Mã độc giả", "Họ và tên", "Giới tính", "Số điện thoại", "Địa chỉ"};
    
    private DefaultTableModel tblModel;
    
  
    
    public ReaderTable(){
        super(new DefaultTableModel(readerTableHeader, 0));
        this.tblModel= (DefaultTableModel)getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellAlignment(); // Thiết lập căn chỉnh văn bản
        
    }
    
   
    
    public void resetTable(){
        tblModel.setRowCount(0); // Xóa dữ liệu cũ

        for (ReaderDTO r : ReaderBUS.readerList){
            Object[] rowData = {
                r.getId(),
                r.getLastName() + ' ' + r.getFirstName(),
                r.getGender().toString().toUpperCase().equals("MALE") ? "Nam" : "Nữ",
                r.getPhone(),
                r.getAddress()
            };
            tblModel.addRow(rowData);
        }
    }
    
    public void addReader(ReaderDTO r){
        if(r!=null){
            ReaderBUS.readerList.add(r);
            resetTable();
        }
    }
    
    public ReaderDTO getSelectedReader(){
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return ReaderBUS.readerList.get(modelRow);
        } 
        return null;
    }
    
    public void deleteReader(ReaderDTO r){
        if(r != null){
            ReaderBUS.readerList.remove(r);
            resetTable();
        }
    }
    
    private void setCellAlignment() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
             
        for (int i = 0 ; i < 5 ; i++){
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
}
