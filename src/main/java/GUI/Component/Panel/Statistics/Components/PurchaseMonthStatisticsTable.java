package GUI.Component.Panel.Statistics.Components;

import DTO.Statistics.PurchaseTimeData;
import GUI.Component.Table.JTableCustom;
import GUI.Controller.Controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PurchaseMonthStatisticsTable extends JTableCustom {
    private final static String[] tblHeader = {"Tháng", "Tiền nhập", "Sách được nhập", "Số phiếu hoàn thành", "Số phiếu bị hủy"};
    private DefaultTableModel tableModel;
    private List<PurchaseTimeData> dataList;


    public PurchaseMonthStatisticsTable(){
        super(new DefaultTableModel(tblHeader,0));
        this.tableModel = (DefaultTableModel) getModel();
        this.dataList = new ArrayList<>();
    }

    public void setList(List<PurchaseTimeData> list){
        this.dataList = list;
    }

    public void renderMonthsTable(int page) {
        if (dataList == null || dataList.isEmpty()) return;
        int lastIndex = page * 3;
        int startIndex = lastIndex-3;
        tableModel.setRowCount(0);
        Long totalFee = 0L; int totalBookQuantity = 0; int doneSheet = 0; int cancelSheet = 0;

        for (int i = startIndex ; i < lastIndex ; i++) {
            PurchaseTimeData p = dataList.get(i);
            Object[] rowData = {
                    "Tháng " + p.getTime(),
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
