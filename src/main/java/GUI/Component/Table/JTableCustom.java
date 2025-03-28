package GUI.Component.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

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

        // Áp dụng font và màu sắc cho header
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 15), new Color(60, 80, 100));

        // Đăng ký renderer cho các kiểu dữ liệu cụ thể
        setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        setDefaultRenderer(String.class, new CustomTableCellRenderer());
        setDefaultRenderer(Long.class, new CustomTableCellRenderer());
        setDefaultRenderer(Float.class, new CustomTableCellRenderer());

        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setCustomGrid(new Color(200, 200, 200), 30);
    }

    // Ngăn không cho chỉnh sửa ô trong bảng
    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Không cho phép chỉnh sửa
    }

    // Renderer cho ô dữ liệu
    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                c.setBackground(new Color(100, 149, 237));
                c.setForeground(Color.WHITE);
            } else {
                c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : new Color(235, 238, 240));
                c.setForeground(Color.BLACK);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    private class CustomHeaderRenderer extends DefaultTableCellRenderer {
        private Color background;
        private Font font;

        public CustomHeaderRenderer(Color background, Font font) {
            this.background = background;
            this.font = font;
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBackground(background);
            label.setForeground(Color.WHITE);
            label.setFont(font);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
            return label;
        }

        public String getToolTipText(MouseEvent e) {
            int row = rowAtPoint(e.getPoint());
            return "Nhấn đúp chuột để xem chi tiết";
        }
    }

    public void setCustomGrid(Color gridColor, int rowHeight) {
        setGridColor(gridColor);
        setRowHeight(rowHeight);
    }

    public void setHeaderStyle(Font font, Color background) {
        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new CustomHeaderRenderer(background, font));
        header.setFont(font);
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



