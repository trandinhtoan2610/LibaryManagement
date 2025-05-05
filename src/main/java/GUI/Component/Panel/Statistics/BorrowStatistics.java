package GUI.Component.Panel.Statistics;

import BUS.BookBUS;
import BUS.BorrowSheetBUS;
import BUS.EmployeeBUS;
import BUS.ReaderBUS;
import DTO.BookViewModel;
import DTO.Employee;
import DTO.ReaderDTO;
import DTO.Statistics.MonthData;
import DTO.Statistics.QuarterData;
import DTO.Statistics.QuarterDataStringId;
import DTO.Statistics.StatusData;
import GUI.Component.Button.ButtonIcon;
import GUI.Component.Panel.Statistics.Components.*;
import com.google.common.eventbus.Subscribe;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.*;
import java.util.List;
public class BorrowStatistics extends JPanel{
    private BoxDashBoard sachtrongkho;
    private BoxDashBoard nhanvien;
    private BoxDashBoard docgia;
    private BoxDashBoard phieumuon;

    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final ReaderBUS readerBUS = new ReaderBUS();
    private final BorrowSheetBUS borrowSheetBUS = new BorrowSheetBUS();
    private final BookBUS bookBUS = new BookBUS();

    private JPanel westPanel;
    private JPanel centerPanel;


    private final EmployeeTableStatistics tableTheoNV = new EmployeeTableStatistics();
    private final ReaderTableStatistics tableTheoDG = new ReaderTableStatistics();
    private final BookBorrowedTableStatistics tableTheoSachChoMuon = new BookBorrowedTableStatistics();

    private final JDateChooser tuNgay = new JDateChooser();
    private final JDateChooser denNgay = new JDateChooser();
    private JComboBox<Integer> theoNam;

    private CustomBookChart chartPanel;
    private PieChart pieChartPanel;

    public BorrowStatistics() {
        setMainUI();
        if (theoNam != null && theoNam.getItemCount() > 0) {
            theoNam.setSelectedIndex(0);
            actionEmployeeTable();
            actionReaderTable();
            actionBookTable();
        }
        EventBusManager.getEventBus().register(this);
    }
    @Subscribe
    public void handleBorrowDataChanged(DataRefreshListener event) {
        SwingUtilities.invokeLater(() -> refreshData());
    }
    @Override
    public void removeNotify() {
        EventBusManager.getEventBus().unregister(this);
        super.removeNotify();
    }
    private void setMainUI() {
        setLayout(new BorderLayout(5, 5));

        add(header(), BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(10, 10));

        mainContent.add(createFilterPanel(), BorderLayout.NORTH);

        JPanel dataPanel = new JPanel(new BorderLayout(10, 10));

        westPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        westPanel.add(createPieChart());
        westPanel.add(createBarChart());
        westPanel.setPreferredSize(new Dimension(400, 600));
        dataPanel.add(westPanel, BorderLayout.WEST);

        centerPanel = mainContent();
        dataPanel.add(centerPanel, BorderLayout.CENTER);

        mainContent.add(dataPanel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    private void actionEmployeeTable() {
        try {
            if (theoNam.getSelectedIndex() == -1) {
                DefaultTableModel model = (DefaultTableModel) tableTheoNV.getModel();
                model.setRowCount(0);
                return;
            }
            int year = (Integer) theoNam.getSelectedItem();
            List<QuarterData> data = borrowSheetBUS.getQuarterEmloyeeData(year);
            Map<Long, int[]> employeeStats = new HashMap<>();
            Map<Long, String> employeesName = new HashMap<>();

            for (Employee e : EmployeeBUS.employeeList) {
                employeeStats.put(e.getId(), new int[4]);
                employeesName.put(e.getId(), e.getFirstName() + " " + e.getLastName());
            }

            for (QuarterData d : data) {
                int quarter = d.getQuarter() - 1;
                long employeeId = d.getId();
                int soluong = d.getSoluong();

                if (employeeStats.containsKey(employeeId)) {
                    employeeStats.get(employeeId)[quarter] = soluong;
                }
            }

            DefaultTableModel model = (DefaultTableModel) tableTheoNV.getModel();
            model.setRowCount(0);
            int finalTotal = 0, quarter1 = 0, quarter2 = 0, quarter3 = 0, quarter4 = 0;
            for (Map.Entry<Long, int[]> e : employeeStats.entrySet()) {
                long employeeId = e.getKey();
                int[] quarter = e.getValue();
                String employeeName = employeesName.get(employeeId);

                int total = 0;
                for (int qty : quarter) {
                    total += qty;
                }
                finalTotal += total;
                quarter1 += quarter[0];
                quarter2 += quarter[1];
                quarter3 += quarter[2];
                quarter4 += quarter[3];
                Object[] rowdata = {
                        employeeName,
                        quarter[0],
                        quarter[1],
                        quarter[2],
                        quarter[3],
                        total
                };
                model.addRow(rowdata);
            }
            Object[] rowdata = {
                    "Tổng Cộng",
                    quarter1,
                    quarter2,
                    quarter3,
                    quarter4,
                    finalTotal
            };
            model.addRow(rowdata);
            DefaultTableCellRenderer compositeRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (value != null && value.equals("Tổng Cộng")) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            };

            for (int i = 0; i < tableTheoNV.getColumnCount(); i++) {
                tableTheoNV.getColumnModel().getColumn(i).setCellRenderer(compositeRenderer);
            }
            tableTheoNV.getColumnModel().getColumn(0).setPreferredWidth(150);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            sorter.setComparator(model.getColumnCount() - 1, Comparator.reverseOrder());
            tableTheoNV.setRowSorter(sorter);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thống kê", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionReaderTable() {
        try {
            if (theoNam.getSelectedIndex() == -1) {
                DefaultTableModel model = (DefaultTableModel) tableTheoDG.getModel();
                model.setRowCount(0);
                return;
            }
            int year = (Integer) theoNam.getSelectedItem();
            List<QuarterDataStringId> data = borrowSheetBUS.getQuarterReaderData(year);
            Map<String, int[]> readerStats = new HashMap<>();
            Map<String, String> readerNames = new HashMap<>();

            for (ReaderDTO r : ReaderBUS.readerList) {
                readerStats.put(r.getId(), new int[4]);
                readerNames.put(r.getId(), r.getLastName() + " " + r.getFirstName());
            }

            for (QuarterDataStringId d : data) {
                int quarter = d.getQuarter() - 1;
                String readerId = d.getId();
                int soluong = d.getSoluong();

                if (readerStats.containsKey(readerId)) {
                    readerStats.get(readerId)[quarter] = soluong;
                }
            }

            DefaultTableModel model = (DefaultTableModel) tableTheoDG.getModel();
            model.setRowCount(0);
            int finalTotal = 0, quarter1 = 0, quarter2 = 0, quarter3 = 0, quarter4 = 0;
            for (Map.Entry<String, int[]> r : readerStats.entrySet()) {
                String readerId = r.getKey();
                int[] quarter = r.getValue();
                String readerName = readerNames.get(readerId);

                int total = 0;
                for (int qty : quarter) {
                    total += qty;
                }
                finalTotal += total;
                quarter1 += quarter[0];
                quarter2 += quarter[1];
                quarter3 += quarter[2];
                quarter4 += quarter[3];
                Object[] rowdata = {
                        readerName,
                        quarter[0],
                        quarter[1],
                        quarter[2],
                        quarter[3],
                        total
                };
                model.addRow(rowdata);

            }
            Object[] rowdata = {
                    "Tổng Cộng",
                    quarter1,
                    quarter2,
                    quarter3,
                    quarter4,
                    finalTotal
            };
            model.addRow(rowdata);

            DefaultTableCellRenderer compositeRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (value != null && value.equals("Tổng Cộng")) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            };

            for (int i = 0; i < tableTheoDG.getColumnCount(); i++) {
                tableTheoDG.getColumnModel().getColumn(i).setCellRenderer(compositeRenderer);
            }
            tableTheoDG.getColumnModel().getColumn(0).setPreferredWidth(150);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            sorter.setComparator(model.getColumnCount() - 1, Comparator.reverseOrder());
            tableTheoDG.setRowSorter(sorter);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thống kê", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionBookTable() {
        try {
            if (theoNam.getSelectedIndex() == -1) {
                DefaultTableModel model = (DefaultTableModel) tableTheoSachChoMuon.getModel();
                model.setRowCount(0);
                return;
            }
            int year = (Integer) theoNam.getSelectedItem();
            List<QuarterData> data = borrowSheetBUS.getQuarterBookData(year);
            Map<Long, int[]> bookStats = new HashMap<>();
            Map<Long, String> bookNames = new HashMap<>();

            for (BookViewModel b : bookBUS.getAllBooksForDisplay()) {
                bookStats.put(b.getId(), new int[4]);
                bookNames.put(b.getId(), b.getName());
            }
            for (QuarterData d : data) {
                int quarter = d.getQuarter() - 1;
                long bookId = d.getId();
                int soluong = d.getSoluong();

                if (bookStats.containsKey(bookId)) {
                    bookStats.get(bookId)[quarter] = soluong;
                }
            }
            DefaultTableModel model = (DefaultTableModel) tableTheoSachChoMuon.getModel();
            model.setRowCount(0);
            int finalTotal = 0, quarter1 = 0, quarter2 = 0, quarter3 = 0, quarter4 = 0;
            for (Map.Entry<Long, int[]> r : bookStats.entrySet()) {
                long bookId = r.getKey();
                int[] quarter = r.getValue();
                String bookName = bookNames.get(bookId);

                int total = 0;
                for (int qty : quarter) {
                    total += qty;
                }
                finalTotal += total;
                quarter1 += quarter[0];
                quarter2 += quarter[1];
                quarter3 += quarter[2];
                quarter4 += quarter[3];
                Object[] rowdata = {
                        bookName,
                        quarter[0],
                        quarter[1],
                        quarter[2],
                        quarter[3],
                        total
                };
                model.addRow(rowdata);

            }
            Object[] rowdata = {
                    "Tổng Cộng",
                    quarter1,
                    quarter2,
                    quarter3,
                    quarter4,
                    finalTotal
            };
            model.addRow(rowdata);

            DefaultTableCellRenderer compositeRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (value != null && value.equals("Tổng Cộng")) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            };

            for (int i = 0; i < tableTheoSachChoMuon.getColumnCount(); i++) {
                tableTheoSachChoMuon.getColumnModel().getColumn(i).setCellRenderer(compositeRenderer);
            }
            tableTheoSachChoMuon.getColumnModel().getColumn(0).setPreferredWidth(150);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            sorter.setComparator(model.getColumnCount() - 1, Comparator.reverseOrder());
            tableTheoSachChoMuon.setRowSorter(sorter);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thống kê", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionBookTableByDate() {
        try {
            Date startDate = tuNgay.getDate();
            Date endDate = denNgay.getDate();
            if (startDate == null || endDate == null) {
                return;
            }
            if (startDate.after(endDate)) {
                return;
            }
            List<QuarterData> data = borrowSheetBUS.getQuarterBookDataByDate(startDate, endDate);
            if (data == null) {
                DefaultTableModel model = (DefaultTableModel) tableTheoSachChoMuon.getModel();
                model.setRowCount(0);
                return;
            }
            Map<Long, int[]> bookStats = new HashMap<>();
            Map<Long, String> bookNames = new HashMap<>();

            for (BookViewModel b : bookBUS.getAllBooksForDisplay()) {
                bookStats.put(b.getId(), new int[4]);
                bookNames.put(b.getId(), b.getName());
            }
            for (QuarterData d : data) {
                int quarter = d.getQuarter() - 1;
                long bookId = d.getId();
                int soluong = d.getSoluong();

                if (bookStats.containsKey(bookId)) {
                    bookStats.get(bookId)[quarter] = soluong;
                }
            }
            DefaultTableModel model = (DefaultTableModel) tableTheoSachChoMuon.getModel();
            model.setRowCount(0);
            int finalTotal = 0, quarter1 = 0, quarter2 = 0, quarter3 = 0, quarter4 = 0;

            for (Map.Entry<Long, int[]> r : bookStats.entrySet()) {
                long bookId = r.getKey();
                int[] quarter = r.getValue();
                String bookName = bookNames.get(bookId);

                int total = 0;
                for (int qty : quarter) {
                    total += qty;
                }
                finalTotal += total;
                quarter1 += quarter[0];
                quarter2 += quarter[1];
                quarter3 += quarter[2];
                quarter4 += quarter[3];
                Object[] rowdata = {
                        bookName,
                        quarter[0],
                        quarter[1],
                        quarter[2],
                        quarter[3],
                        total
                };
                model.addRow(rowdata);

            }
            Object[] rowdata = {
                    "Tổng Cộng",
                    quarter1,
                    quarter2,
                    quarter3,
                    quarter4,
                    finalTotal
            };
            model.addRow(rowdata);

            DefaultTableCellRenderer compositeRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (value != null && value.equals("Tổng Cộng")) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            };

            for (int i = 0; i < tableTheoSachChoMuon.getColumnCount(); i++) {
                tableTheoSachChoMuon.getColumnModel().getColumn(i).setCellRenderer(compositeRenderer);
            }
            tableTheoSachChoMuon.getColumnModel().getColumn(0).setPreferredWidth(150);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            sorter.setComparator(model.getColumnCount() - 1, Comparator.reverseOrder());
            tableTheoSachChoMuon.setRowSorter(sorter);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thống kê", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionReaderTableByDate() {
        try {
            Date startDate = tuNgay.getDate();
            Date endDate = denNgay.getDate();
            if (startDate == null || endDate == null) {
                return;
            }
            if (startDate.after(endDate)) {
                return;
            }
            List<QuarterDataStringId> data = borrowSheetBUS.getQuarterReaderDataByDate(startDate, endDate);
            if (data == null) {
                DefaultTableModel model = (DefaultTableModel) tableTheoSachChoMuon.getModel();
                model.setRowCount(0);
                return;
            }
            Map<String, int[]> readerStats = new HashMap<>();
            Map<String, String> readerNames = new HashMap<>();

            for (ReaderDTO r : ReaderBUS.readerList) {
                readerStats.put(r.getId(), new int[4]);
                readerNames.put(r.getId(), r.getLastName() + " " + r.getFirstName());
            }

            for (QuarterDataStringId d : data) {
                int quarter = d.getQuarter() - 1;
                String readerId = d.getId();
                int soluong = d.getSoluong();

                if (readerStats.containsKey(readerId)) {
                    readerStats.get(readerId)[quarter] = soluong;
                }
            }

            DefaultTableModel model = (DefaultTableModel) tableTheoDG.getModel();
            model.setRowCount(0);

            int finalTotal = 0, quarter1 = 0, quarter2 = 0, quarter3 = 0, quarter4 = 0;

            for (Map.Entry<String, int[]> r : readerStats.entrySet()) {
                String readerId = r.getKey();
                int[] quarter = r.getValue();
                String readerName = readerNames.get(readerId);

                int total = 0;
                for (int qty : quarter) {
                    total += qty;
                }
                finalTotal += total;
                quarter1 += quarter[0];
                quarter2 += quarter[1];
                quarter3 += quarter[2];
                quarter4 += quarter[3];
                Object[] rowdata = {
                        readerName,
                        quarter[0],
                        quarter[1],
                        quarter[2],
                        quarter[3],
                        total
                };
                model.addRow(rowdata);

            }
            Object[] rowdata = {
                    "Tổng Cộng",
                    quarter1,
                    quarter2,
                    quarter3,
                    quarter4,
                    finalTotal
            };
            model.addRow(rowdata);

            DefaultTableCellRenderer compositeRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (value != null && value.equals("Tổng Cộng")) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            };

            for (int i = 0; i < tableTheoDG.getColumnCount(); i++) {
                tableTheoDG.getColumnModel().getColumn(i).setCellRenderer(compositeRenderer);
            }
            tableTheoDG.getColumnModel().getColumn(0).setPreferredWidth(150);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            sorter.setComparator(model.getColumnCount() - 1, Comparator.reverseOrder());
            tableTheoDG.setRowSorter(sorter);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thống kê", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionEmployeeTableByDate() {
        try {
            Date startDate = tuNgay.getDate();
            Date endDate = denNgay.getDate();
            if (startDate == null || endDate == null) {
                return;
            }
            if (startDate.after(endDate)) {
                return;
            }
            List<QuarterData> data = borrowSheetBUS.getQuarterEmployeeDataByDate(startDate, endDate);
            if (data == null) {
                DefaultTableModel model = (DefaultTableModel) tableTheoSachChoMuon.getModel();
                model.setRowCount(0);
                return;
            }
            Map<Long, int[]> employeeStats = new HashMap<>();
            Map<Long, String> employeesName = new HashMap<>();

            for (Employee e : EmployeeBUS.employeeList) {
                employeeStats.put(e.getId(), new int[4]);
                employeesName.put(e.getId(), e.getFirstName() + " " + e.getLastName());
            }

            for (QuarterData d : data) {
                int quarter = d.getQuarter() - 1;
                long employeeId = d.getId();
                int soluong = d.getSoluong();

                if (employeeStats.containsKey(employeeId)) {
                    employeeStats.get(employeeId)[quarter] = soluong;
                }
            }

            DefaultTableModel model = (DefaultTableModel) tableTheoNV.getModel();
            model.setRowCount(0);
            int finalTotal = 0, quarter1 = 0, quarter2 = 0, quarter3 = 0, quarter4 = 0;

            for (Map.Entry<Long, int[]> e : employeeStats.entrySet()) {
                long employeeId = e.getKey();
                int[] quarter = e.getValue();
                String employeeName = employeesName.get(employeeId);

                int total = 0;
                for (int qty : quarter) {
                    total += qty;
                }
                finalTotal += total;
                quarter1 += quarter[0];
                quarter2 += quarter[1];
                quarter3 += quarter[2];
                quarter4 += quarter[3];
                Object[] rowdata = {
                        employeeName,
                        quarter[0],
                        quarter[1],
                        quarter[2],
                        quarter[3],
                        total
                };
                model.addRow(rowdata);

            }
            Object[] rowdata = {
                    "Tổng Cộng",
                    quarter1,
                    quarter2,
                    quarter3,
                    quarter4,
                    finalTotal
            };
            model.addRow(rowdata);

            DefaultTableCellRenderer compositeRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (value != null && value.equals("Tổng Cộng")) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            };

            for (int i = 0; i < tableTheoNV.getColumnCount(); i++) {
                tableTheoNV.getColumnModel().getColumn(i).setCellRenderer(compositeRenderer);
            }
            tableTheoNV.getColumnModel().getColumn(0).setPreferredWidth(150);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            sorter.setComparator(model.getColumnCount() - 1, Comparator.reverseOrder());
            tableTheoNV.setRowSorter(sorter);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thống kê", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel header() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        headerPanel.add(Box.createHorizontalGlue());

        sachtrongkho = new BoxDashBoard("Sách trong kho", Integer.toString(bookBUS.getCountBook()));
        headerPanel.add(sachtrongkho);
        headerPanel.add(Box.createHorizontalStrut(10));

        nhanvien = new BoxDashBoard("Nhân viên", Integer.toString(employeeBUS.countEmployee()));
        headerPanel.add(nhanvien);
        headerPanel.add(Box.createHorizontalStrut(10));

        docgia = new BoxDashBoard("Độc giả", Integer.toString(readerBUS.getCountReader()));
        headerPanel.add(docgia);
        headerPanel.add(Box.createHorizontalStrut(10));

        phieumuon = new BoxDashBoard("Phiếu mượn", Integer.toString(borrowSheetBUS.getCountBorrowSheet()));
        headerPanel.add(phieumuon);

        headerPanel.add(Box.createHorizontalGlue());
        return headerPanel;
    }

    private JPanel createFilterPanel() {
        JPanel containerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Lọc theo từ ngày đến ngày"));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel label = new JLabel("Từ Ngày:");
        datePanel.add(label);
        tuNgay.setPreferredSize(new Dimension(150, 30));
        tuNgay.setDateFormatString("dd/MM/yyyy");
        datePanel.add(tuNgay);
        datePanel.add(Box.createHorizontalStrut(20));
        JLabel label1 = new JLabel("Đến Ngày:");
        datePanel.add(label1);
        denNgay.setPreferredSize(new Dimension(150, 30));
        denNgay.setDateFormatString("dd/MM/yyyy");
        datePanel.add(denNgay);
        datePanel.add(Box.createHorizontalStrut(20));
        ButtonIcon applyDateFilterBtn = new ButtonIcon("/icons/search.svg");
        applyDateFilterBtn.setPreferredSize(new Dimension(40, 40));
        applyDateFilterBtn.setFocusPainted(false);
        applyDateFilterBtn.setBorderPainted(false);
        applyDateFilterBtn.setBorder(null);
        applyDateFilterBtn.setContentAreaFilled(false);
        applyDateFilterBtn.setHoverBackgroundColor(new Color(240, 240, 240));
        applyDateFilterBtn.setPressedBackgroundColor(new Color(240, 240, 240));
        applyDateFilterBtn.putClientProperty("JButton.buttonType", "toolBarButton");
        applyDateFilterBtn.addActionListener(e -> {
            if (tuNgay.getDate() == null || denNgay.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn cả 2 ngày", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tuNgay.getDate().after(denNgay.getDate())){
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không thể sau ngày kết thúc", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            theoNam.setSelectedIndex(-1);
            actionEmployeeTableByDate();
            actionReaderTableByDate();
            actionBookTableByDate();
            actionPieChart();
            actionBarchart();
        });
        datePanel.add(applyDateFilterBtn);

        JPanel filterPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel2.setBorder(BorderFactory.createTitledBorder("Lọc theo năm"));
        JPanel quarterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel label3 = new JLabel("Năm: ");
        quarterPanel.add(label3);

        List<Integer> searchOptionsY = actionByYear();
        theoNam = new JComboBox<>();

        updateYearComboBox(searchOptionsY);

        theoNam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        theoNam.setBackground(Color.WHITE);
        theoNam.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        theoNam.addActionListener(e -> {
            if (theoNam.getSelectedIndex() != -1) {
                tuNgay.setDate(null);
                denNgay.setDate(null);
            }
            actionEmployeeTable();
            actionReaderTable();
            actionBookTable();
            actionBarchart();
            actionPieChart();
        });

        quarterPanel.add(theoNam);

        filterPanel.add(datePanel);
        filterPanel2.add(quarterPanel);

        containerPanel.add(filterPanel);
        containerPanel.add(filterPanel2);

        return containerPanel;
    }

    private void updateYearComboBox(List<Integer> years) {
        DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
        if (years.isEmpty()) {
            model.addElement(0);
        } else {
            for (Integer year : years) {
                model.addElement(year);
            }
        }
        theoNam.setModel(model);
    }

    private void refreshYearComboBox() {
        List<Integer> newYears = actionByYear();
        updateYearComboBox(newYears);
    }

    private List<Integer> actionByYear() {
        List<Integer> tmp = borrowSheetBUS.getListYear();
        return tmp;
    }

    private JPanel mainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        if (theoNam != null) {
            theoNam.setSelectedItem(currentYear);
            actionEmployeeTable();
            actionReaderTable();
            actionBookTable();
        }

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Thống kê theo nhân viên", new JScrollPane(tableTheoNV));
        tabbedPane.addTab("Thống kê theo độc giả", new JScrollPane(tableTheoDG));
        tabbedPane.addTab("Sách đã cho mượn", new JScrollPane(tableTheoSachChoMuon));

        mainContent.add(tabbedPane, BorderLayout.CENTER);
        return mainContent;
    }

    private JPanel createPieChart() {
        JPanel piePanel = new JPanel(new BorderLayout());
        piePanel.setBorder(BorderFactory.createTitledBorder("Tỉ lệ phiếu"));

        pieChartPanel = new PieChart();
        pieChartPanel.setChartType(PieChart.PeiChartType.DONUT_CHART);
        piePanel.add(pieChartPanel, BorderLayout.CENTER);

        actionPieChart();

        return piePanel;
    }

    private void actionPieChart() {
        List<StatusData> statusDataList;

        if (theoNam.getSelectedIndex() == -1) {
            statusDataList = borrowSheetBUS.getStatusDataByDate(tuNgay.getDate(), denNgay.getDate());
        } else {
            int year = (Integer) theoNam.getSelectedItem();
            statusDataList = borrowSheetBUS.getStatusDataByYear(year);
        }
        if (statusDataList == null) {
            pieChartPanel.clearData();
            pieChartPanel.repaint();
            return;
        }
        int muon = 0, tra = 0, phat = 0;
        for (StatusData statusData : statusDataList) {
            switch (statusData.getStatus()) {
                case Đang_Mượn -> muon++;
                case Đã_Trả -> tra++;
                case Phạt -> phat++;
            }
        }
        pieChartPanel.clearData();
        pieChartPanel.addData(new ModelPieChart("Đang Mượn", muon, new Color(65, 105, 225))); // RoyalBlue
        pieChartPanel.addData(new ModelPieChart("Đã Trả", tra, new Color(34, 139, 34))); // ForestGreen
        pieChartPanel.addData(new ModelPieChart("Phạt", phat, new Color(220, 20, 60))); // Crimson
        pieChartPanel.repaint();
    }

    private JPanel createBarChart() {
        chartPanel = new CustomBookChart(new LinkedHashMap<>(), "Thống kê mượn sách theo tháng");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private void actionBarchart() {
        Map<String, Integer> bookData = new LinkedHashMap<>();
        boolean hasData = false;
        if (theoNam.getSelectedIndex() == -1) {
            List<MonthData> monthDataByDate = borrowSheetBUS.getMonthDataByDate(tuNgay.getDate(), denNgay.getDate());
            for (int i =1 ;i<= 12;i++){
                bookData.put(String.valueOf(i), 0);
            }
            for (MonthData monthData : monthDataByDate) {
                bookData.put(String.valueOf(monthData.getThang()), monthData.getSoluong());
                if (monthData.getSoluong() > 0) {
                    hasData = true;
                }
            }
            if (!hasData) {
                chartPanel.updateData(new LinkedHashMap<>(), "Không có dữ liệu");
                return;
            }
            chartPanel.updateData(bookData, "Thống kê mượn sách theo tháng");
            return;
        }
        int year = (Integer) theoNam.getSelectedItem();
        List<MonthData> monthDataList = borrowSheetBUS.getMonthDataByYear(year);
        for (int i = 1; i <= 12; i++) {
            bookData.put(String.valueOf(i), 0);
        }
        for (MonthData monthData : monthDataList) {
            bookData.put(String.valueOf(monthData.getThang()), monthData.getSoluong());
        }
        chartPanel.updateData(bookData, "Thống kê mượn sách theo tháng - Năm " + year);
    }
    public void refreshData() {
        sachtrongkho.setValue(Integer.toString(bookBUS.getCountBook()));
        nhanvien.setValue(Integer.toString(employeeBUS.countEmployee()));
        docgia.setValue(Integer.toString(readerBUS.getCountReader()));
        phieumuon.setValue(Integer.toString(borrowSheetBUS.getCountBorrowSheet()));

        if (theoNam.getSelectedIndex() == -1) {
            actionEmployeeTableByDate();
            actionReaderTableByDate();
            actionBookTableByDate();
        } else {
            actionEmployeeTable();
            actionReaderTable();
            actionBookTable();
        }
        refreshYearComboBox();
        actionByYear();
        actionPieChart();
        actionBarchart();
    }
}