package GUI.Component.Table;

import BUS.SupplierBUS;
import DTO.SupplierDTO;
import GUI.Component.Table.JTableCustom;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SupplierTable extends JTableCustom {
    private static final String[] supplierTableHeader = {"Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ"};
    private DefaultTableModel tblModel;
    private SupplierBUS supplierBUS; 

    public SupplierTable() {
        super(new DefaultTableModel(supplierTableHeader, 0));
        this.tblModel = (DefaultTableModel) getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellAlignment();
        supplierBUS = new SupplierBUS();
        resetTable(); 
    }

    public void resetTable() {
        tblModel.setRowCount(0);
        supplierBUS.reloadSupplierList(); 
        List<SupplierDTO> suppliers = supplierBUS.supplierList;
        if (suppliers != null) {
            for (SupplierDTO s : suppliers) {
                Object[] rowData = {
                        s.getId(),
                        s.getName(),
                        s.getPhone(),
                        s.getAddress()
                };
                tblModel.addRow(rowData);
            }
        }
    }

    public void addSupplier(SupplierDTO s) {
        if (s != null) {
            supplierBUS.addSupplier(s); 
            resetTable();
        }
    }

    public SupplierDTO getSelectedSupplier() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            List<SupplierDTO> suppliers = supplierBUS.supplierList; // Get from BUS
            if (modelRow < suppliers.size()) {
                return suppliers.get(modelRow);
            }
        }
        return null;
    }

    public void deleteSupplier(SupplierDTO s) {
        if (s != null) {
            supplierBUS.deleteSupplier(s); // Delete using BUS
            resetTable();
        }
    }

    private void setCellAlignment() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    public void loadData(List<SupplierDTO> data) {
        tblModel.setRowCount(0); // Xóa dữ liệu cũ
        if (data != null) {
            for (SupplierDTO s : data) {
                Object[] rowData = {
                        s.getId(),
                        s.getName(),
                        s.getPhone(),
                        s.getAddress()
                };
                tblModel.addRow(rowData);
            }
        }
    }
}