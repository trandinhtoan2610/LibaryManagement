package GUI.Component.Table;


import BUS.BookBUS;
import DTO.BookViewModel;
import DTO.BorrowDetailDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class BorrowDetailTable extends JTableCustom{
    private static final String[] HEADER = {
            "Tên Sách", "Số Lượng", "Trạng Thái"
    };
    private static final BookBUS bookBUS = new BookBUS();
    private DefaultTableModel tableModel;
    private static List<BorrowDetailDTO> borrowDetailDTOS;
    public BorrowDetailTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.borrowDetailDTOS = new ArrayList<>();
        setHeaderStyle(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14), new java.awt.Color(70, 130, 180));
        setCustomGrid(new java.awt.Color(220, 220, 220), 30);
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(100);
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
            BookViewModel bookViewModel = bookBUS.getBookByIdForDisplay(borrowDetailDTO.getBookId());
            tableModel.addRow(new Object[]{
                    bookViewModel.getName(),
                    borrowDetailDTO.getQuantity(),
                    borrowDetailDTO.getStatus()
            });
        }
    }
}
