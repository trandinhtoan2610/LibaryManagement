package GUI.Component.Panel.Statistics;

import BUS.*;
import GUI.Component.Button.ButtonIcon;
import GUI.Component.Panel.Statistics.Components.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class PurchaseOrderStatistics extends JPanel {
    private BoxDashBoard soPhieuNhap;
    private BoxDashBoard nhaCungCap;
    private BoxDashBoard tongChi;
    private BoxDashBoard sachDaNhap;

    private JComboBox<Integer> theoNam;

    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final ReaderBUS readerBUS = new ReaderBUS();
    private final PenaltyBUS penaltyBUS = new PenaltyBUS();
    private final BookBUS bookBUS = new BookBUS();
    private final SupplierBUS supplierBUS = new SupplierBUS();

    private final EmployeeTableStatistics employeeTableStatistics = new EmployeeTableStatistics();
    private final ReaderTableStatistics readerTableStatistics = new ReaderTableStatistics();
    private final BookBorrowedTableStatistics bookBorrowedTableStatistics = new BookBorrowedTableStatistics();

    private final JDateChooser tuNgay = new JDateChooser();
    private final JDateChooser denNgay = new JDateChooser();

    private JPanel westPanel;
    private JPanel centerPanel;

    private CustomBookChart barChart;
    private PieChart pieChart;
    public PurchaseOrderStatistics() {
        setMainUI();
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
    private JPanel header() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        headerPanel.add(Box.createHorizontalGlue());

        sachDaNhap = new BoxDashBoard("Sách đã nhập", "0");
        headerPanel.add(sachDaNhap);
        headerPanel.add(Box.createHorizontalStrut(10));

        nhaCungCap = new BoxDashBoard("Nhà cung cấp", "0");
        headerPanel.add(nhaCungCap);
        headerPanel.add(Box.createHorizontalStrut(10));

        soPhieuNhap = new BoxDashBoard("Số phiếu nhập", "0");
        headerPanel.add(soPhieuNhap);
        headerPanel.add(Box.createHorizontalStrut(10));

        tongChi = new BoxDashBoard("Tổng chi", "0");
        headerPanel.add(tongChi);

        headerPanel.add(Box.createHorizontalGlue());
        return headerPanel;
    }
    private void actionHeader(){

    }
    private JPanel mainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Thống kê theo nhân viên", new JScrollPane(employeeTableStatistics));
        tabbedPane.addTab("Thống kê theo nhà cung cấp", new JScrollPane(readerTableStatistics));
        tabbedPane.addTab("Sách đã mua", new JScrollPane(bookBorrowedTableStatistics));

        mainContent.add(tabbedPane, BorderLayout.CENTER);
        return mainContent;
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
        datePanel.add(applyDateFilterBtn);

        JPanel filterPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel2.setBorder(BorderFactory.createTitledBorder("Lọc theo năm"));
        JPanel quarterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel label3 = new JLabel("Năm: ");
        quarterPanel.add(label3);

        theoNam = new JComboBox<>();
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

        quarterPanel.add(theoNam);

        filterPanel.add(datePanel);
        filterPanel2.add(quarterPanel);

        containerPanel.add(filterPanel);
        containerPanel.add(filterPanel2);

        return containerPanel;
    }
    private JPanel createPieChart() {
        JPanel piePanel = new JPanel(new BorderLayout());
        piePanel.setBorder(BorderFactory.createTitledBorder("Tỉ lệ nhà cung cấp"));

        pieChart = new PieChart();
        pieChart.setChartType(PieChart.PeiChartType.DONUT_CHART);
        piePanel.add(pieChart, BorderLayout.CENTER);
        return piePanel;
    }
    private JPanel createBarChart() {
        barChart = new CustomBookChart(new LinkedHashMap<>(), "Thống kê tổng chi theo tháng");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(barChart, BorderLayout.CENTER);
        return panel;
    }
}
