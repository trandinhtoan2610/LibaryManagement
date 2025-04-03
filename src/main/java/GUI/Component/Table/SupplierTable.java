package GUI.Component.Table;

import BUS.SupplierBUS;
import DTO.SupplierDTO;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SupplierTable extends JTableCustom {
    private static final String[] supplierTableHeader = {"Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ"};

    private DefaultTableModel tblModel;

    public SupplierTable() {
        super(new DefaultTableModel(supplierTableHeader, 0));
        this.tblModel = (DefaultTableModel) getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellAlignment(); 
    }

    public void resetTable() {
        tblModel.setRowCount(0); 
        for (SupplierDTO s : SupplierBUS.supplierList) {
            Object[] rowData = {
                    s.getId(),
                    s.getName(),
                    s.getPhone(),
                    s.getAddress()
            };
            tblModel.addRow(rowData);
        }
    }

    public void addSupplier(SupplierDTO s) {
        if (s != null) {
            SupplierBUS.supplierList.add(s);
            resetTable();
        }
    }

    public SupplierDTO getSelectedSupplier() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return SupplierBUS.supplierList.get(modelRow);
        }
        return null;
    }

    public void deleteSupplier(SupplierDTO s) {
        if (s != null) {
            SupplierBUS.supplierList.remove(s);
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
}