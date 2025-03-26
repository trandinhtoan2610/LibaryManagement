package GUI.Component.Table;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class JTableCustom extends JTable {
    public JTableCustom() {
        super();
        initialize();
    }
    public JTableCustom(TableModel dm) {
        super(dm);
        initialize();
    }
    public JTableCustom(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        initialize();
    }
    private void initialize() {
        setRowHeight(28);
        setGridColor(new Color(220, 220, 220));
        setShowGrid(true);
        setFillsViewportHeight(true);
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(70, 90, 110));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 32));
        setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setCustomGrid(new Color(200, 200, 200), 30);
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 15), new Color(60, 80, 100));
    }

    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                c.setBackground(new Color(100, 149, 237));
                c.setForeground(Color.WHITE);
            } else {
                c.setBackground(row % 2 == 0 ?
                        new Color(245, 245, 245) :
                        new Color(235, 238, 240));
                c.setForeground(Color.BLACK);
            }

            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    public void setCustomGrid(Color gridColor, int rowHeight) {
        setGridColor(gridColor);
        setRowHeight(rowHeight);
    }

    public void setHeaderStyle(Font font, Color background) {
        JTableHeader header = getTableHeader();
        header.setFont(font);
        header.setBackground(background);
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Table Demo");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        Object[][] data = {
                {"John", "Doe", 25},
                {"Jane", "Smith", 30},
                {"Alice", "Johnson", 28}
        };
        String[] columnNames = {"First Name", "Last Name", "Age"};
        JTableCustom table = new JTableCustom(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}