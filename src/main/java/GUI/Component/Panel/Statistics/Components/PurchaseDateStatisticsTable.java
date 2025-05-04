package GUI.Component.Panel.Statistics.Components;

import BUS.SupplierBUS;
import DTO.Statistics.PurchaseDateData;
import DTO.SupplierDTO;
import GUI.Component.Table.JTableCustom;
import GUI.Controller.Controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PurchaseDateStatisticsTable extends JTableCustom  {
    private static final String[] tblHeader = {"Ngày nhập", "Mã phiếu nhập", "Nhà cung cấp", "Tiền nhập", "số lượng sách nhập"};
    private DefaultTableModel tableModel;
    private List<PurchaseDateData> dataList;
    private SupplierBUS supplierBUS;

    public PurchaseDateStatisticsTable(){
        super(new DefaultTableModel(tblHeader,0));
        this.tableModel = (DefaultTableModel) getModel();
        dataList = new ArrayList<>();
        supplierBUS = new SupplierBUS();
    }

    public void setList(List<PurchaseDateData> list){
        this.dataList = list;
    }

    public void renderDateTable(){
        if(dataList == null) return;
        tableModel.setRowCount(0);
        for(PurchaseDateData data : dataList){
            SupplierDTO supplierDTO = supplierBUS.getSupplierById(data.getSupplierID());

            Object[] rowData = {
                    Controller.formatDate(data.getPurchaseDate()),
                    data.getPurchaseID(),
                    supplierDTO.getName(),
                    Controller.formatVND(data.getPurchaseFee()),
                    data.getBookQuantity()
            };
            tableModel.addRow(rowData);
        }

    }


}
