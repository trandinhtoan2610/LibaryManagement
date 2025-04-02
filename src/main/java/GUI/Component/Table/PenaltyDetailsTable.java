package GUI.Component.Table;

import DTO.BorrowDetailDTO;
import DTO.PenaltyDetailsDTO;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PenaltyDetailsTable extends JTableCustom{
    private static final String[] HEADER = {
            "Mã Phiếu Phạt", "Vi Phạm", "Tiền Phạt"
    };
    private DefaultTableModel tableModel;
    private static List<PenaltyDetailsDTO> penaltyDetailsDTOS;
    public PenaltyDetailsTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.penaltyDetailsDTOS = new ArrayList<>();
        setHeaderStyle(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14), new java.awt.Color(70, 130, 180));
        setCustomGrid(new java.awt.Color(220, 220, 220), 30);
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        setAutoCreateRowSorter(true);
    }
    public void setPenaltyDetails(List<PenaltyDetailsDTO> penaltyDetailsDTOS) {
        if(penaltyDetailsDTOS != null) {
            this.penaltyDetailsDTOS = new ArrayList<>(penaltyDetailsDTOS);
        }else{
            this.penaltyDetailsDTOS = new ArrayList<>();
        }
        refreshTable();
    }
    public void addPenaltyDetail(PenaltyDetailsDTO penaltyDetailsDTO) {
        if (penaltyDetailsDTO != null) {
            penaltyDetailsDTOS.add(penaltyDetailsDTO);
            refreshTable();
        }
    }
    public boolean updatePenaltyDetail(PenaltyDetailsDTO penaltyDetailsDTO) {
        PenaltyDetailsDTO selectedPenaltyDetailDTO = getSelectedPenaltyDetail();
        if (selectedPenaltyDetailDTO != null) {
            int index = penaltyDetailsDTOS.indexOf(selectedPenaltyDetailDTO);
            if (index != -1) {
                penaltyDetailsDTOS.set(index, penaltyDetailsDTO);
                refreshTable();
                return true;
            }
        }
        return false;
    }
    public PenaltyDetailsDTO getSelectedPenaltyDetail() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            return penaltyDetailsDTOS.get(convertRowIndexToModel(selectedRow));
        }
        return null;
    }
    public boolean removePenaltyDetail(PenaltyDetailsDTO penaltyDetailsDTO) {
        PenaltyDetailsDTO selectedPenaltyDetailDTO = getSelectedPenaltyDetail();
        if (selectedPenaltyDetailDTO != null) {
            int index = penaltyDetailsDTOS.indexOf(selectedPenaltyDetailDTO);
            if (index != -1) {
                penaltyDetailsDTOS.remove(index);
                refreshTable();
                return true;
            }
        }
        return false;
    }
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (PenaltyDetailsDTO penaltyDetailsDTO : penaltyDetailsDTOS) {
            tableModel.addRow(new Object[]{
                    penaltyDetailsDTO.getPenaltyId(),
                    penaltyDetailsDTO.getPunish(),
                    penaltyDetailsDTO.getSubAmount()
            });
        }
    }
}


