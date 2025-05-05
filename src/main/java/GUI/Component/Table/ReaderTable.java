package GUI.Component.Table;

import BUS.ReaderBUS;
import DTO.ReaderDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class ReaderTable extends JTableCustom {
    private static final String[] readerTableHeader = {"Mã độc giả", "Họ và tên", "Giới tính", "Số điện thoại", "Địa chỉ"};
    private DefaultTableModel tblModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    public ReaderTable(){
        super(new DefaultTableModel(readerTableHeader, 0));
        this.tblModel= (DefaultTableModel)getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //setCellAlignment();
        rowSorter = new TableRowSorter<>(tblModel);
        this.setRowSorter(rowSorter);
        
    }
    public void resetTable(){
        tblModel.setRowCount(0); // Xóa dữ liệu cũ

        for (ReaderDTO r : ReaderBUS.readerList){
            if (r.isActive()) {
                Object[] rowData = {
                        r.getId(),
                        r.getLastName() + ' ' + r.getFirstName(),
                        r.getGender().toString(),
                        r.getPhone(),
                        r.getAddress(),
                };
                tblModel.addRow(rowData);
            }
        }
    }
    
//    public void addReader(ReaderDTO r){
//        if(r!=null){
//            ReaderBUS.readerList.add(r);
//            resetTable();
//        }
//    }
    
    public ReaderDTO getSelectedReader(){
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return ReaderBUS.readerList.get(modelRow);
        } 
        return null;
    }
    
//    public void deleteReader(ReaderDTO r){
//        if(r != null){
//            ReaderBUS.readerList.remove(r);
//            resetTable();
//        }
//    }
    
//    private void setCellAlignment() {
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//
//        for (int i = 0 ; i < 5 ; i++){
//            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//        }
//    }
    
    public void filterTable(String name, String phone, String address, String gender) {
    List<RowFilter<Object, Object>> filters = new ArrayList<>();

    
   

    // Các bộ lọc khác
    if (!name.isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + name, 1));
    }

    if (!phone.isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + phone, 3));
    }

    if (!address.isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + address, 4));
    }

    if (!gender.isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + gender, 2));
    }

    // Áp dụng bộ lọc
    if (filters.isEmpty()) {
        rowSorter.setRowFilter(null);
    } else {
        rowSorter.setRowFilter(RowFilter.andFilter(filters));
    }
}


}
