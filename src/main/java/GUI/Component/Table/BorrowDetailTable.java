package GUI.Component.Table;


import DTO.BorrowDetailDTO;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class BorrowDetailTable extends JTableCustom{
    private static final String[] HEADER = {
            "Mã Phiếu Mượn", "Mã Sách", "Số Lượng", "Trạng Thái"
    };
    private DefaultTableModel tableModel;
    private static List<BorrowDetailDTO> borrowDetailDTOS;
    public BorrowDetailTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.borrowDetailDTOS = new ArrayList<>();
        setHeaderStyle(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14), new java.awt.Color(70, 130, 180));
        setCustomGrid(new java.awt.Color(220, 220, 220), 30);
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(25);
        getColumnModel().getColumn(3).setPreferredWidth(50);
        setAutoCreateRowSorter(true);
    }
    public void setBorrowDetails(List<BorrowDetailDTO> borrowDetailDTOS) {
        if(borrowDetailDTOS != null) {
            this.borrowDetailDTOS = new ArrayList<>(borrowDetailDTOS);
        }else{
            this.borrowDetailDTOS = new ArrayList<>();
        }
        refreshTable();
    }
    public void addBorrowDetail(BorrowDetailDTO borrowDetailDTO) {
        if (borrowDetailDTO != null) {
            borrowDetailDTOS.add(borrowDetailDTO);
            refreshTable();
        }
    }
    public boolean updateBorrowDetail(BorrowDetailDTO borrowDetailDTO) {
        BorrowDetailDTO selectedBorrowDetailDTO = getSelectedBorrowDetail();
        if (selectedBorrowDetailDTO != null) {
            int index = borrowDetailDTOS.indexOf(selectedBorrowDetailDTO);
            if (index != -1) {
                borrowDetailDTOS.set(index, borrowDetailDTO);
                refreshTable();
                return true;
            }
        }
        return false;
    }
    public BorrowDetailDTO getSelectedBorrowDetail() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            return borrowDetailDTOS.get(convertRowIndexToModel(selectedRow));
        }
        return null;
    }
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (BorrowDetailDTO borrowDetailDTO : borrowDetailDTOS) {
            tableModel.addRow(new Object[]{
                    borrowDetailDTO.getBorrowSheetId(),
                    borrowDetailDTO.getBookId(),
                    borrowDetailDTO.getQuantity(),
                    borrowDetailDTO.getStatus()
            });
        }
    }
}
