package GUI.Component.Table;

import DTO.PenaltyDTO;
import DTO.PurchaseOrderDTO;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderTable extends JTableCustom{
    private static final String HEADER[] = {"Mã Phiếu Nhập", "Mã Nhà Cung Cấp", "Mã Nhân Viên", "Trạng Thái", "Ngày Mua", "Thành Tiền"};
    private DefaultTableModel tableModel;
    private static List<PurchaseOrderDTO> purchaseOrderDTOS;

    public PurchaseOrderTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        this.purchaseOrderDTOS = new ArrayList<>();
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);
        //rowcolum

        setAutoCreateRowSorter(true);
    }
    public void setPurchaseOrderDTOS(List<PurchaseOrderDTO> purchaseOrder) {
        if (purchaseOrder != null) {
            this.purchaseOrderDTOS = new ArrayList<>(purchaseOrder);
        } else {
            this.purchaseOrderDTOS = new ArrayList<>();
        }
        refreshTable();
    }

    public void addPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        if (purchaseOrder != null) {
            purchaseOrderDTOS.add(purchaseOrder);
            refreshTable();
        }
    }

    public boolean updatePurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        PurchaseOrderDTO selectedPurchaseOrder = getSelectedPurchaseOrder();
        if (selectedPurchaseOrder != null) {
            int index = purchaseOrderDTOS.indexOf(selectedPurchaseOrder);
            if (index != -1) {
                purchaseOrderDTOS.set(index, selectedPurchaseOrder);
                refreshTable();
                return true;
            }
        }
        return false;
    }

    public boolean removePurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        PurchaseOrderDTO selectedPurchaseOrder = getSelectedPurchaseOrder();
        if (selectedPurchaseOrder != null) {
            return purchaseOrderDTOS.remove(selectedPurchaseOrder);
        }
        return false;
    }

    public PurchaseOrderDTO getSelectedPurchaseOrder() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return purchaseOrderDTOS.get(modelRow);
        }
        return null;
    }
    public void refreshTable(){
        tableModel.setRowCount(0);
        for(PurchaseOrderDTO purchaseOrderDTO : purchaseOrderDTOS){
            Object[] rowData = {
                    purchaseOrderDTO.getId(),
                    purchaseOrderDTO.getSupplierId(),
                    purchaseOrderDTO.getEmployeeId(),
                    purchaseOrderDTO.getStatus(),
                    purchaseOrderDTO.getBuyDate(),
                    purchaseOrderDTO.getTotalAmount()
            };
            tableModel.addRow(rowData);
        }
    }
}
