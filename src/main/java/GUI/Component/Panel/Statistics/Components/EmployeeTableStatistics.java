package GUI.Component.Panel.Statistics.Components;

import GUI.Component.Table.JTableCustom;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class EmployeeTableStatistics extends JTableCustom {
    private final static String[] HEADER = {
            "Nhân Viên", "Quý 1", "Quý 2", "Quý 3", "Quý 4", "Tổng Cộng"
    };
    private DefaultTableModel tableModel;
    public EmployeeTableStatistics() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);
        setAutoCreateRowSorter(true);
    }
}
