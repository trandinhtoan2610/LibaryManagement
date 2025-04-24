package GUI.Component.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
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

    public JTableCustom(Object[] header) {
        super(new Object[][]{}, header);
        initialize();
    }

    private void initialize() {
        setRowHeight(28);
        setGridColor(new Color(220, 220, 220));
        setShowGrid(true);
        setFillsViewportHeight(true);

        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 15), new Color(60, 80, 100));

        setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        setDefaultRenderer(String.class, new CustomTableCellRenderer());
        setDefaultRenderer(Long.class, new CustomTableCellRenderer());
        setDefaultRenderer(Float.class, new CustomTableCellRenderer());

        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setCustomGrid(new Color(200, 200, 200), 30);
        setCellAlignment();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                int row = rowAtPoint(p);
                int col = columnAtPoint(p);
                if (row >= 0 && col >= 0) {
                    // Giữ nguyên selection
                } else {
                    clearSelection();
                }
            }
        });

        addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
                    Window window = SwingUtilities.getWindowAncestor(JTableCustom.this);
                    if (window != null) {
                        window.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                if (!isClickInTable(e)) {
                                    clearSelection();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean isClickInTable(MouseEvent e) {
        Point tablePoint = SwingUtilities.convertPoint(
                e.getComponent(), e.getPoint(), this);
        if (!getVisibleRect().contains(tablePoint)) {
            return false;
        }
        int row = rowAtPoint(tablePoint);
        int col = columnAtPoint(tablePoint);
        return row >= 0 && col >= 0;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
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

    private void setCellAlignment() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        revalidate();
        repaint();
    }
}