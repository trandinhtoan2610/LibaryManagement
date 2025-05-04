package GUI.Component.Panel.Statistics.Components;

import DTO.Statistics.PenaltyTimeData;
import GUI.Component.Table.JTableCustom;
import GUI.Controller.Controller;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PenaltyMonthStatisticsTable extends JTableCustom {
    private final static String[] tblHeader = {"Tháng", "Số phiếu phạt", "Độc giả vi phạm", "Sách tổn thất", "Tiền phạt"};
    private DefaultTableModel tableModel;
    private List<PenaltyTimeData> penaltyTimeDataList;

    public PenaltyMonthStatisticsTable(){
        super(new DefaultTableModel(tblHeader,0));
        this.tableModel = (DefaultTableModel) getModel();
        this.penaltyTimeDataList = new ArrayList<>();
    }

    public void setList(List<PenaltyTimeData> list){
        this.penaltyTimeDataList = list;
    }

    public void renderMonthsTable(int page) {
        if (penaltyTimeDataList == null || penaltyTimeDataList.isEmpty()) return;
        int lastIndex = page * 3;
        int startIndex = lastIndex-3;
        tableModel.setRowCount(0);
        int totalSheet = 0; int totalReader = 0; int totalLostBook = 0; Long totalFee = 0L;

        for (int i = startIndex ; i < lastIndex ; i++){
            PenaltyTimeData p = penaltyTimeDataList.get(i);
            Object[] rowData = {
                    "Tháng " + p.getTime(),
                    p.getPenaltySheetQuantity(),
                    p.getPenaltyReaderQuantity(),
                    p.getLostBookQuantity(),
                    Controller.formatVND(p.getPenaltyFee()),
            };
            tableModel.addRow(rowData);

            totalSheet += p.getPenaltySheetQuantity();
            totalReader += p.getPenaltyReaderQuantity();
            totalLostBook += p.getLostBookQuantity();
            totalFee += p.getPenaltyFee();
        }

        Object[] totalData = {
                "Tổng cộng", totalSheet, totalReader, totalLostBook, Controller.formatVND(totalFee)
        };
        tableModel.addRow(totalData);
    }


}
