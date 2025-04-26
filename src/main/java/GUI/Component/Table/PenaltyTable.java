package GUI.Component.Table;

import BUS.PenaltyBUS;
import DTO.Enum.PayStatus;
import DTO.PenaltyDTO;
import GUI.Controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class PenaltyTable extends JTableCustom {
    private static final String[] tblHeader = {"Mã phiếu phạt", "Ngày phạt", "Tổng tiền phạt", "Ngày nộp phạt", "Trạng thái"};
    private DefaultTableModel tblModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private List<PenaltyDTO> displayList;

    public PenaltyTable(){
        super(new DefaultTableModel(tblHeader, 0));
        this.tblModel = (DefaultTableModel)getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        PenaltyBUS penaltyBUS = new PenaltyBUS();
        rowSorter = new TableRowSorter<>(tblModel);
        this.setRowSorter(rowSorter);
    }

    public void resetTable(){
        tblModel.setRowCount(0);
        displayList = new ArrayList<>(PenaltyBUS.penaltyList);
        for (PenaltyDTO p : displayList){
            Object[] rowData = {
                    p.getId(),
                    Controller.formatDate(p.getPenaltyDate()),
                    Controller.formatVND(p.getTotalAmount()),
                    p.getPayDate() == null ? "" : Controller.formatDate(p.getPayDate()),
                    p.getPayStatus().equals(PayStatus.Chưa_Thanh_Toán) ? "Chưa thanh toán" : "Đã thanh toán"
            };

            tblModel.addRow(rowData);
        }
    }

    public PenaltyDTO getSelectedPenalty() {
        int viewRow = getSelectedRow();
        if (viewRow == -1) return null;
        int modelRow = convertRowIndexToModel(viewRow);
        return displayList.get(modelRow); // hoặc getModel().getValueAt(modelRow, ...)
    }


    public void filterTable(String ID){
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        if(!ID.isEmpty()){
            filters.add(RowFilter.regexFilter("(?i)"+ID,0));
        }

        if (filters.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    public void advanceFilterTable(List<PenaltyDTO> filterList){
        tblModel.setRowCount(0);
        if(filterList == null) return;
        displayList = new ArrayList<>(filterList);
        for (PenaltyDTO p : filterList){
            Object[] rowData = {
                    p.getId(),
                    Controller.formatDate(p.getPenaltyDate()),
                    Controller.formatVND(p.getTotalAmount()),
                    p.getPayDate() == null ? "" : Controller.formatDate(p.getPayDate()),
                    p.getPayStatus().equals(PayStatus.Chưa_Thanh_Toán) ? "Chưa thanh toán" : "Đã thanh toán"
            };

            tblModel.addRow(rowData);
        }
    }
}