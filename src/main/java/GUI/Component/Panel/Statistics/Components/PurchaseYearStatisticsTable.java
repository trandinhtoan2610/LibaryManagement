package GUI.Component.Panel.Statistics.Components;

import DTO.Statistics.PurchaseTimeData;
import GUI.Component.Table.JTableCustom;
import GUI.Controller.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class PurchaseYearStatisticsTable extends JTableCustom {
    private final static String[] tblHeader = {"Năm", "Tiền nhập", "Sách được nhập", "Số phiếu hoàn thành", "Số phiếu bị hủy" };
    private DefaultTableModel tableModel;
    private List<PurchaseTimeData> yearList;

    public PurchaseYearStatisticsTable(){
        super(new DefaultTableModel(tblHeader, 0));
        this.tableModel = (DefaultTableModel) getModel();
        yearList = new ArrayList<>();
    }

    public void setList(List<PurchaseTimeData> list){
        yearList = list;
    }

    public void renderYearsTable(){
        if(yearList == null) return;

        Long totalFee = 0L; int totalBookQuantity = 0; int doneSheet = 0; int cancelSheet = 0;
        tableModel.setRowCount(0);

        for(PurchaseTimeData p : yearList) {
            Object[] rowData = {
                    "Năm " + p.getTime(),
                    Controller.formatVND(p.getPurchaseFee()),
                    p.getBookQuantity(),
                    p.getDoneSheet(),
                    p.getCancelSheet()
            };
            tableModel.addRow(rowData);

            totalFee += p.getPurchaseFee();
            totalBookQuantity+= p.getBookQuantity();
            doneSheet += p.getDoneSheet();
            cancelSheet += p.getCancelSheet();
        }
        Object[] totalData = {
                "Tổng cộng", Controller.formatVND(totalFee), totalBookQuantity, doneSheet, cancelSheet
        };

        tableModel.addRow(totalData);
    }
}
