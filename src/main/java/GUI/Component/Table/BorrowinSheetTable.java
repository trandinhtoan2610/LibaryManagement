package GUI.Component.Table;

import DTO.BorrowDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowinSheetTable extends JTableCustom {
    private static final String[] HEADER = {
            "Mã Phiếu Mượn", "Mã Nhân Viên", "Mã Độc Giả",
            "Ngày Mượn", "Ngày Hạn Trả", "Ngày Thực Trả", "Trạng Thái"
    };
    private DefaultTableModel tableModel;
    private static List<BorrowDTO> borrowDTOS;

    public BorrowinSheetTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.borrowDTOS = new java.util.ArrayList<>();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setHeaderStyle(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14), new java.awt.Color(70, 130, 180));
        setCustomGrid(new java.awt.Color(220, 220, 220), 30);
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(120);
        getColumnModel().getColumn(4).setPreferredWidth(120);
        getColumnModel().getColumn(5).setPreferredWidth(120);
        getColumnModel().getColumn(6).setPreferredWidth(80);
        setAutoCreateRowSorter(true);
    }

    public void setBorrows(List<BorrowDTO> borrowDTOS) {
        if (borrowDTOS != null) {
            this.borrowDTOS = new ArrayList<>(borrowDTOS);
        } else {
            this.borrowDTOS = new ArrayList<>();
        }
        refreshTable();
    }

    public void addBorrow(BorrowDTO borrowDTO) {
        if (borrowDTO != null) {
            borrowDTOS.add(borrowDTO);
            refreshTable();
        }
    }

    public boolean updateBorrow(BorrowDTO borrowDTO) {
        BorrowDTO selectedBorrowDTO = getSelectedBorrow();
        if (selectedBorrowDTO != null) {
            int index = borrowDTOS.indexOf(selectedBorrowDTO);
            if (index != -1) {
                borrowDTOS.set(index, borrowDTO);
                refreshTable();
                return true;
            }
        }
        return false;
    }

    public BorrowDTO getSelectedBorrow() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            return borrowDTOS.get(convertRowIndexToModel(selectedRow));
        }
        return null;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (BorrowDTO borrowDTO : borrowDTOS) {
            tableModel.addRow(new Object[]{
                    borrowDTO.getId(),
                    borrowDTO.getEmployeeId(),
                    borrowDTO.getReaderId(),
                    formatDate(borrowDTO.getBorrowedDate()),
                    formatDate(borrowDTO.getDuedate()),
                    formatDate(borrowDTO.getActualReturnDate()),
                    borrowDTO.getStatus(),
            });
        }
    }

    public void removeBorrow(BorrowDTO borrowDTO) {
        borrowDTOS.remove(borrowDTO);
        refreshTable();
    }
    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}

