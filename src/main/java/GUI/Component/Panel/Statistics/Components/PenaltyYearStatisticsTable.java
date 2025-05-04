package GUI.Component.Panel.Statistics.Components;

import DTO.Statistics.PenaltyTimeData;
import GUI.Component.Table.JTableCustom;
import GUI.Controller.Controller;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PenaltyYearStatisticsTable extends JTableCustom {
    private static final String[] tblHeader = {"Năm", "Số phiếu phạt", "Độc giả vi phạm", "Sách tổn thất", "Tiền phạt"};
    private DefaultTableModel tableModel;
    private List<PenaltyTimeData> yearList;

    public PenaltyYearStatisticsTable(){
        super(new DefaultTableModel(tblHeader,0));
        this.tableModel = (DefaultTableModel) getModel();
        yearList = new ArrayList<>();
    }

    public void setList(List<PenaltyTimeData> list){
        this.yearList = list;
    }

    public void renderYearsTable(){
        if(yearList == null) return;
        int totalSheet = 0; int totalReader = 0; int totalLostBook = 0; Long totalFee = 0L;

        tableModel.setRowCount(0);
        for(PenaltyTimeData p : yearList){
            Object[] rowData = {
                    p.getTime(),
                    p.getPenaltySheetQuantity(),
                    p.getPenaltyReaderQuantity(),
                    p.getLostBookQuantity(),
                    Controller.formatVND(p.getPenaltyFee())
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
