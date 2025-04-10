package GUI.Component.Table;

import DTO.PenaltyDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PenaltyTable extends JTableCustom {
    private static final String[] HEADER = {
            "Mã Phiếu Phạt", "Mã Phiếu Mượn", "Thời Gian", "Tổng Tiền",
            "Trạng Thái"
    };
    private DefaultTableModel tableModel;
    private static List<PenaltyDTO> penaltyList;

    public PenaltyTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.penaltyList = new ArrayList<>();
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);
        //rowcolum

        setAutoCreateRowSorter(true);
    }

    public void setPenaltyList(List<PenaltyDTO> penaltyList) {
        if (penaltyList != null) {
            this.penaltyList = new ArrayList<>(penaltyList);
        } else {
            this.penaltyList = new ArrayList<>();
        }
        refreshTable();
    }

    public void addPenalty(PenaltyDTO penalty) {
        if (penalty != null) {
            penaltyList.add(penalty);
            refreshTable();
        }
    }

    public boolean updatePenalty(PenaltyDTO penalty) {
        PenaltyDTO selectedPenalty = getSelectedPenalty();
        if (selectedPenalty != null) {
            int index = penaltyList.indexOf(selectedPenalty);
            if (index != -1) {
                penaltyList.set(index, penalty);
                refreshTable();
                return true;
            }
        }
        return false;
    }

    public boolean removePenalty(PenaltyDTO penalty) {
        PenaltyDTO selectedPenalty = getSelectedPenalty();
        if (selectedPenalty != null) {
            return penaltyList.remove(selectedPenalty);
        }
        return false;
    }

    public PenaltyDTO getSelectedPenalty() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return penaltyList.get(modelRow);
        }
        return null;
    }
    public void refreshTable(){
        tableModel.setRowCount(0);
        for(PenaltyDTO penalty : penaltyList){
            Object[] rowData = {
                    penalty.getPenaltyId(),
                    penalty.getBorrowId(),
                    penalty.getPayDate(),
                    penalty.getTotalAmount(),
                    penalty.getPay()
            };
            tableModel.addRow(rowData);
        }
    }
}
