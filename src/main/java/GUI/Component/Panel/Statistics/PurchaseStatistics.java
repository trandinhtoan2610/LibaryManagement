package GUI.Component.Panel.Statistics;

import BUS.PenaltyBUS;
import BUS.PurchaseStatisticsBUS;
import DTO.Statistics.*;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Panel.Statistics.Components.*;
import GUI.Controller.Controller;
import com.google.common.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PurchaseStatistics extends javax.swing.JPanel {
    private final static Color pageDefaultColor = new Color(204,204,204);
    private final static Color pageHoverColor = new Color(104,204,204);
    private final static Color tabHoverColor = new Color(255,255,204);
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private JPanel selectedPage;
    private JPanel selectedTab;

    Window parent = SwingUtilities.getWindowAncestor(this);
    private boolean typeStatus;
    private List<String> years;
    private static String countPurchaseSheet;
    private static String countSupplier;
    private static Long purchaseTotalFee;
    private static String countPurchaseBook;
    
    private static PurchaseStatisticsBUS purchaseStatsBUS;
    private static PenaltyBUS penaltyBUS;
    private Map<String, List<StatisticsPreciousData<Long>> > employeeStats;
    private Map<String, List<StatisticsPreciousData<String>> > supplierStats;
    private Map<String, List<StatisticsPreciousData<Long>> > bookStats;
    private Map<String, List<PurchaseTimeData>> monthStats;
    private List<PurchaseTimeData> yearsDataList;
    private List<PurchaseDateData> dateDataList;

    private FeeMonthBarChart monthBarChart;
    private FeeYearBarChart yearBarChart;
    private PieChart datePieChart;

    private CardLayout dataCardLayout;
    private CardLayout subDataCardLayout;
    private CardLayout timeInputCardLayout;



    public PurchaseStatistics() {
        initComponents();
        initialize();
    }

    public void initialize(){
        //CardLayout :
        dataCardLayout = (CardLayout)  dataPanel.getLayout();
        subDataCardLayout = (CardLayout) subDataPanel.getLayout();
        timeInputCardLayout = (CardLayout) timeInputPanel.getLayout();

        //List :
        yearsDataList = new ArrayList<>();
        dateDataList = new ArrayList<>();

        //Panel :
        selectedPage = new JPanel();
        selectedTab = new JPanel();

        purchaseStatsBUS = new PurchaseStatisticsBUS();

        //Map - HashMap :
        employeeStats = new HashMap<>();
        supplierStats = new HashMap<>();
        bookStats = new HashMap<>();
        monthStats = new HashMap<>();

        txtStartDate.setDateFormatString("dd/MM/yyyy");
        txtEndDate.setDateFormatString("dd/MM/yyyy");
        reloadYears();
        renderBox();
        loadPreciousData();
        preciousChartPanel.setLayout(new BorderLayout());
        yearChartPanel.setLayout(new BorderLayout());
        dateChartPanel.setLayout(new BorderLayout());
        initYearPanel();
        initDatePanel();
        renderPreciousPanel();
        firstRenderPanel();

        EventBusManager.getEventBus().register(this);
    }

    @Subscribe
    public void onPurchaseChanged(PurchaseChangeEvent event) {
        reloadYears();
        renderBox();
        loadPreciousData();
        renderPreciousPanel();
        initDatePanel();
        initYearPanel();
    }

    public void renderPreciousPanel() {
        // Kiểm tra nếu combobox không có item
        if (cboxChooseYear.getItemCount() == 0) {
            // Xử lý trường hợp không có năm nào
            tblEmployee.setList(new ArrayList<>());
            tblSupplier.setList(new ArrayList<>());
            tblBook.setList(new ArrayList<>());
            tblMonth.setList(new ArrayList<>());
            resetPreciousPanel();
            return;
        }

        // Nếu có item thì lấy năm được chọn
        String year = cboxChooseYear.getSelectedItem().toString();

        try {
            List<StatisticsPreciousData<Long>> employeeList = purchaseStatsBUS.getEmployeePreciousData(year);
            List<StatisticsPreciousData<String>> supplierList = purchaseStatsBUS.getSupplierPreciousData(year);
            List<StatisticsPreciousData<Long>> bookList = purchaseStatsBUS.getBookPreciousData(year);
            List<PurchaseTimeData> monthList = purchaseStatsBUS.getPurchaseMonthData(year);

            tblEmployee.setList(employeeList != null ? employeeList : new ArrayList<>());
            tblSupplier.setList(supplierList != null ? supplierList : new ArrayList<>());
            tblBook.setList(bookList != null ? bookList : new ArrayList<>());
            tblMonth.setList(monthList != null ? monthList : new ArrayList<>());

            resetPreciousPanel();
            showMonthBarChart(year);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void reloadYears() {
        try {
            years = purchaseStatsBUS.getActiveYears();
            cboxChooseYear.removeAllItems();

            if (years != null && !years.isEmpty()) {
                for (String year : years) {
                    cboxChooseYear.addItem(year);
                }
                cboxChooseYear.setSelectedIndex(0);
            }
            // Nếu không có năm nào thì combobox sẽ trống nhưng không báo lỗi
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách năm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Hàm render các ô thông tin chung :
    public void renderBox(){
        countPurchaseSheet = purchaseStatsBUS.countSheet();
        purchaseTotalFee = purchaseStatsBUS.sumPurchaseFee();
        countSupplier = purchaseStatsBUS.countSupplier();
        countPurchaseBook = purchaseStatsBUS.countBook();

        lblPurchaseBoxContent.setText(countPurchaseSheet);
        lblSupplierBoxContent.setText(countSupplier);
        lblBookBoxContent.setText(countPurchaseBook);
        String totalFee = Controller.formatVND(purchaseTotalFee);
        lblPurchaseFeeBoxContent.setText(totalFee);
    }

    //Lấy thống kê các quý theo từng năm :
    public void loadPreciousData(){
        for(String year : years){
            String key = year;
            List<StatisticsPreciousData<Long>> employeeList = purchaseStatsBUS.getEmployeePreciousData(year);
            List<StatisticsPreciousData<String>> supplierList = purchaseStatsBUS.getSupplierPreciousData(year);
            List<StatisticsPreciousData<Long>> bookList = purchaseStatsBUS.getBookPreciousData(year);
            List<PurchaseTimeData> monthDataList = purchaseStatsBUS.getPurchaseMonthData(year);

            employeeStats.put(key, employeeList);
            supplierStats.put(key, supplierList);
            bookStats.put(key, bookList);
            monthStats.put(key,monthDataList);
        }
    }

    public void initYearPanel(){
        txtStartYear.setYear(2020);
        txtEndYear.setYear(2025);
        renderYearPanel("2020","2025");
    }

    public void renderYearPanel(String startYear, String endYear){
        yearsDataList = purchaseStatsBUS.getPurchaseYearData(startYear, endYear);
        lblStartYear.setText(startYear);
        lblEndYear.setText(endYear);
        tblYear.setList(yearsDataList);
        tblYear.renderYearsTable();
        showYearBarChart();
    }


    public void initDatePanel(){
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);
        Date endDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date beginDate = Date.from(sevenDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        renderDatePanel(beginDate,endDate);

    }

    public void renderDatePanel(Date startDate, Date finishDate){
        String beginDate = dateFormat.format(startDate);
        String endDate = dateFormat.format(finishDate);
        dateDataList = purchaseStatsBUS.getPurchaseDateData(beginDate, endDate);
        tblDate.setList(dateDataList);
        tblDate.renderDateTable();
        lblStartDateTitle.setText(Controller.formatDate(startDate));
        lblEndDateTitle.setText(Controller.formatDate(finishDate));
        txtStartDate.setDate(startDate);
        txtEndDate.setDate(finishDate);

        showDatePieChart();
    }

    public void showMonthBarChart(String year){
        preciousChartPanel.removeAll(); // Xóa chart cũ
        monthBarChart = new FeeMonthBarChart(monthStats.get(year), "purchaseFee");
        preciousChartPanel.add(monthBarChart, BorderLayout.CENTER);
        preciousChartPanel.revalidate();
        preciousChartPanel.repaint();
    }

    public void showYearBarChart(){
        yearChartPanel.removeAll();
        yearBarChart = new FeeYearBarChart(yearsDataList, true);
        yearChartPanel.add(yearBarChart, BorderLayout.CENTER);
        yearChartPanel.revalidate();
        yearChartPanel.repaint();
    }

    public void showDatePieChart(){
        Long sumDateFee = 0L;
        for(PurchaseDateData d : dateDataList){
            sumDateFee += d.getPurchaseFee();
        }
        String beginDate = Controller.formatDate(txtStartDate.getDate());
        String endDate = Controller.formatDate(txtEndDate.getDate());
        String title = beginDate + " đến " + endDate;
        double totalDateFee = (double) sumDateFee;
        double otherDateFee = (double) purchaseTotalFee - totalDateFee; // Tổng tiền phạt từ trước đến nay.
        dateChartPanel.removeAll();
        datePieChart = new PieChart();
        datePieChart.setChartType(PieChart.PeiChartType.DONUT_CHART);
        datePieChart.addData(new ModelPieChart(title,totalDateFee, new Color(66, 133, 244)));
        datePieChart.addData(new ModelPieChart("Tổng chi các ngày khác", otherDateFee,new Color(53, 244, 0)));
        dateChartPanel.add(datePieChart, BorderLayout.CENTER);
        dateChartPanel.revalidate();
        dateChartPanel.repaint();
    }


    /// Các function GUI :
    //click vào các TAB : Nhân viên, Độc giả, Sách :
    public void tabSelected(JPanel selectTab, String alias){
        selectedTab.setBorder(null);
        selectedTab.setBackground(Color.white);

        selectedTab = selectTab;
        CardLayout cardLayout = (CardLayout) preciousTablePanel.getLayout();
        cardLayout.show(preciousTablePanel, alias);
        selectedTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        selectedTab.setBackground(new Color(204,234,234));
        selectedTab.revalidate();
        selectedTab.repaint();

    }

    public void pageSelected(JPanel pagePanel, JLabel pageTitle){
        if(pagePanel == selectedPage) return;

        selectedPage.setBackground(pageDefaultColor);
        lblPageOne.setForeground(Color.BLACK);
        lblPageTwo.setForeground(Color.BLACK);
        lblPageThree.setForeground(Color.BLACK);
        lblPageFour.setForeground(Color.BLACK);

        pagePanel.setBackground(new Color(0,153,51));
        selectedPage = pagePanel;
        pageTitle.setForeground(Color.WHITE);
    }

    public void pageHover(JPanel page){
        if(page == selectedPage) return;
        page.setBackground(pageHoverColor);
    }

    public void pageClearHover(JPanel page){
        if(page == selectedPage) return;
        page.setBackground(pageDefaultColor);
    }

    public void tabHover(JPanel tab){
        if(tab == selectedTab) return;
        tab.setBackground(tabHoverColor);
        tab.setOpaque(true);
    }

    public void tabClearHover(JPanel tab){
        if(tab == selectedTab) return;
        tab.setBackground(Color.white);
    }

    public void radTimeStateChange(){
        if(radMonth.isSelected()){
            dataCardLayout.show(dataPanel,"PRECIOUS");
            subDataCardLayout.show(subDataPanel,"PRECIOUS");
            timeInputCardLayout.show(timeInputPanel, "PRECIOUS");
        }

        if(radYear.isSelected()){
            dataCardLayout.show(dataPanel,"YEAR");
            subDataCardLayout.show(subDataPanel,"YEAR");
            timeInputCardLayout.show(timeInputPanel,"YEAR");
        }

        if(radDate.isSelected()){
            dataCardLayout.show(dataPanel,"DATE");
            subDataCardLayout.show(subDataPanel,"DATE");
            timeInputCardLayout.show(timeInputPanel,"DATE");
        }
    }

    public void resetPreciousPanel(){
        radCountAmount.setSelected(true);
        tblEmployee.renderPenaltySheetTable();
        tblSupplier.renderPurchaseSheetTable();
        tblBook.renderPurchaseQuantiyTable();
        tabSelected(employeeTab,"EMPLOYEE");

        pageSelected(firstPageTab, lblPageOne);
        tblMonth.renderMonthsTable(1);
    }

    public void firstRenderPanel(){
        radMonth.setSelected(true);
        timeInputCardLayout.show(timeInputPanel, "PRECIOUS");
        dataCardLayout.show(dataPanel,"PRECIOUS");
        subDataCardLayout.show(subDataPanel, "PRECIOUS");
    }


    //Các hàm kiểm tra ràng buộc :
    public boolean checkYearField(){
        int startYear = txtStartYear.getYear();
        int endYear = txtEndYear.getYear();

        if(endYear < startYear){
            new AlertDialog(parent, "Vui lòng nhập các năm hợp lệ").setVisible(true);
            return false;
        }
        return true;
    }

    public boolean checkDateField(){
        Date startDate = txtStartDate.getDate();
        Date endDate = txtEndDate.getDate();

        if(endDate.before(startDate)){
            new AlertDialog(parent,"Vui lòng nhập các ngày hợp lệ !").setVisible(true);
            return false;
        }
        return true;
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupTime = new javax.swing.ButtonGroup();
        btnGroupPenalty = new javax.swing.ButtonGroup();
        headerPanel = new javax.swing.JPanel();
        blankPanel = new javax.swing.JPanel();
        blankPanel2 = new javax.swing.JPanel();
        blankPanel3 = new javax.swing.JPanel();
        boxContainerPanel = new javax.swing.JPanel();
        purchaseBoxPanel = new javax.swing.JPanel();
        purchaseBoxHeader = new javax.swing.JPanel();
        lblPurchaseHeader = new javax.swing.JLabel();
        purchaseBoxContent = new javax.swing.JPanel();
        lblPurchaseBoxContent = new javax.swing.JLabel();
        purchaseFeePanel = new javax.swing.JPanel();
        purchaseFeeBoxHeader = new javax.swing.JPanel();
        lblPurchaseFeeHeader = new javax.swing.JLabel();
        purchaseFeeBoxContent = new javax.swing.JPanel();
        lblPurchaseFeeBoxContent = new javax.swing.JLabel();
        supplierBoxPanel = new javax.swing.JPanel();
        supplierBoxHeader = new javax.swing.JPanel();
        lblSupplierHeader = new javax.swing.JLabel();
        supplierBoxContent = new javax.swing.JPanel();
        lblSupplierBoxContent = new javax.swing.JLabel();
        bookBoxPanel = new javax.swing.JPanel();
        bookBoxHeader = new javax.swing.JPanel();
        lblBookHeader = new javax.swing.JLabel();
        bookBoxContent = new javax.swing.JPanel();
        lblBookBoxContent = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        leftContentPanel = new javax.swing.JPanel();
        searchTimePanel = new javax.swing.JPanel();
        radioTimePanel = new javax.swing.JPanel();
        radYearPanel = new javax.swing.JPanel();
        radYear = new javax.swing.JRadioButton();
        radMonthPanel = new javax.swing.JPanel();
        radMonth = new javax.swing.JRadioButton();
        radDatePanel = new javax.swing.JPanel();
        radDate = new javax.swing.JRadioButton();
        timeInputPanel = new javax.swing.JPanel();
        yearInputPanel = new javax.swing.JPanel();
        yearBtnPanel = new javax.swing.JPanel();
        btnSearchYear = new javax.swing.JButton();
        btnRefreshYear = new javax.swing.JButton();
        yearChooserPanel = new javax.swing.JPanel();
        startYearPanel = new javax.swing.JPanel();
        lblSearchStartYear = new javax.swing.JLabel();
        txtStartYear = new com.toedter.calendar.JYearChooser();
        endYearPanel = new javax.swing.JPanel();
        lblSearchEndYear = new javax.swing.JLabel();
        txtEndYear = new com.toedter.calendar.JYearChooser();
        preciousInputPanel = new javax.swing.JPanel();
        lblChooseYear = new javax.swing.JLabel();
        cboxChooseYear = new javax.swing.JComboBox<>();
        dateInputPanel = new javax.swing.JPanel();
        dateButtonPanel = new javax.swing.JPanel();
        btnSearchDate = new javax.swing.JButton();
        btnRefreshDate = new javax.swing.JButton();
        dateChooserPanel = new javax.swing.JPanel();
        startDatePanel = new javax.swing.JPanel();
        lblStartDate = new javax.swing.JLabel();
        txtStartDate = new com.toedter.calendar.JDateChooser();
        endDatePanel = new javax.swing.JPanel();
        lblEndDate = new javax.swing.JLabel();
        txtEndDate = new com.toedter.calendar.JDateChooser();
        subDataPanel = new javax.swing.JPanel();
        dateChartPanel = new javax.swing.JPanel();
        yearChartPanel = new javax.swing.JPanel();
        preciousChartPanel = new javax.swing.JPanel();
        dataPanel = new javax.swing.JPanel();
        datePanel = new javax.swing.JPanel();
        dateHeaderPanel = new javax.swing.JPanel();
        lblDateTitle1 = new javax.swing.JLabel();
        lblStartDateTitle = new javax.swing.JLabel();
        lblDateTitle2 = new javax.swing.JLabel();
        lblEndDateTitle = new javax.swing.JLabel();
        dateTablePanel = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        tblDate = new GUI.Component.Panel.Statistics.Components.PurchaseDateStatisticsTable();
        yearPanel = new javax.swing.JPanel();
        yearPanelTitle = new javax.swing.JPanel();
        lblYearHeaderTitle1 = new javax.swing.JLabel();
        lblStartYear = new javax.swing.JLabel();
        lblYearHeaderTitle2 = new javax.swing.JLabel();
        lblEndYear = new javax.swing.JLabel();
        yearTablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblYear = new GUI.Component.Panel.Statistics.Components.PurchaseYearStatisticsTable();
        preciousPanel = new javax.swing.JPanel();
        monthDataPanel = new javax.swing.JPanel();
        preciousMonthFooter = new javax.swing.JPanel();
        lblPreciousPage = new javax.swing.JLabel();
        firstPageTab = new javax.swing.JPanel();
        lblPageOne = new javax.swing.JLabel();
        secondePageTab = new javax.swing.JPanel();
        lblPageTwo = new javax.swing.JLabel();
        thirdPageTab = new javax.swing.JPanel();
        lblPageThree = new javax.swing.JLabel();
        fourthPageTab = new javax.swing.JPanel();
        lblPageFour = new javax.swing.JLabel();
        monthTablePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMonth = new GUI.Component.Panel.Statistics.Components.PurchaseMonthStatisticsTable();
        preciousDataPanel = new javax.swing.JPanel();
        preciousDataHeader = new javax.swing.JPanel();
        preciousTaskbar = new javax.swing.JPanel();
        employeeTab = new javax.swing.JPanel();
        lblEmployeeTab = new javax.swing.JLabel();
        supplierTab = new javax.swing.JPanel();
        lblSupplierTab = new javax.swing.JLabel();
        bookTab = new javax.swing.JPanel();
        lblBookTab = new javax.swing.JLabel();
        radPenaltyPanel = new javax.swing.JPanel();
        radCountAmount = new javax.swing.JRadioButton();
        radSumFee = new javax.swing.JRadioButton();
        preciousTablePanel = new javax.swing.JPanel();
        bookTablePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBook = new GUI.Component.Panel.Statistics.Components.PurchaseBookTable();
        supplierTablePanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblSupplier = new GUI.Component.Panel.Statistics.Components.PurchaseSupplierTable();
        employeeTablePanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblEmployee = new GUI.Component.Panel.Statistics.Components.PenaltyEmployeeTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setPreferredSize(new java.awt.Dimension(1097, 160));
        headerPanel.setLayout(new java.awt.BorderLayout());

        blankPanel.setBackground(new java.awt.Color(255, 255, 255));
        blankPanel.setPreferredSize(new java.awt.Dimension(1164, 15));
        blankPanel.setRequestFocusEnabled(false);

        javax.swing.GroupLayout blankPanelLayout = new javax.swing.GroupLayout(blankPanel);
        blankPanel.setLayout(blankPanelLayout);
        blankPanelLayout.setHorizontalGroup(
            blankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1124, Short.MAX_VALUE)
        );
        blankPanelLayout.setVerticalGroup(
            blankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        headerPanel.add(blankPanel, java.awt.BorderLayout.PAGE_START);

        blankPanel2.setBackground(new java.awt.Color(255, 255, 255));
        blankPanel2.setPreferredSize(new java.awt.Dimension(5, 145));

        javax.swing.GroupLayout blankPanel2Layout = new javax.swing.GroupLayout(blankPanel2);
        blankPanel2.setLayout(blankPanel2Layout);
        blankPanel2Layout.setHorizontalGroup(
            blankPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        blankPanel2Layout.setVerticalGroup(
            blankPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 145, Short.MAX_VALUE)
        );

        headerPanel.add(blankPanel2, java.awt.BorderLayout.LINE_START);

        blankPanel3.setBackground(new java.awt.Color(255, 255, 255));
        blankPanel3.setPreferredSize(new java.awt.Dimension(5, 145));

        javax.swing.GroupLayout blankPanel3Layout = new javax.swing.GroupLayout(blankPanel3);
        blankPanel3.setLayout(blankPanel3Layout);
        blankPanel3Layout.setHorizontalGroup(
            blankPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        blankPanel3Layout.setVerticalGroup(
            blankPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 145, Short.MAX_VALUE)
        );

        headerPanel.add(blankPanel3, java.awt.BorderLayout.LINE_END);

        boxContainerPanel.setBackground(new java.awt.Color(255, 255, 255));
        boxContainerPanel.setLayout(new java.awt.GridLayout(1, 0, 15, 0));

        purchaseBoxPanel.setLayout(new java.awt.BorderLayout());

        purchaseBoxHeader.setBackground(new java.awt.Color(255, 102, 102));
        purchaseBoxHeader.setPreferredSize(new java.awt.Dimension(277, 40));
        purchaseBoxHeader.setLayout(new java.awt.BorderLayout());

        lblPurchaseHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPurchaseHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblPurchaseHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPurchaseHeader.setText("Phiếu nhập");
        purchaseBoxHeader.add(lblPurchaseHeader, java.awt.BorderLayout.CENTER);

        purchaseBoxPanel.add(purchaseBoxHeader, java.awt.BorderLayout.PAGE_START);

        purchaseBoxContent.setBackground(new java.awt.Color(255, 51, 51));
        purchaseBoxContent.setLayout(new java.awt.BorderLayout());

        lblPurchaseBoxContent.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPurchaseBoxContent.setForeground(new java.awt.Color(255, 255, 255));
        lblPurchaseBoxContent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPurchaseBoxContent.setText("jLabel1");
        purchaseBoxContent.add(lblPurchaseBoxContent, java.awt.BorderLayout.CENTER);

        purchaseBoxPanel.add(purchaseBoxContent, java.awt.BorderLayout.CENTER);

        boxContainerPanel.add(purchaseBoxPanel);

        purchaseFeePanel.setLayout(new java.awt.BorderLayout());

        purchaseFeeBoxHeader.setBackground(new java.awt.Color(10, 220, 160));
        purchaseFeeBoxHeader.setPreferredSize(new java.awt.Dimension(277, 40));
        purchaseFeeBoxHeader.setLayout(new java.awt.BorderLayout());

        lblPurchaseFeeHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPurchaseFeeHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblPurchaseFeeHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPurchaseFeeHeader.setText("Tổng chi");
        purchaseFeeBoxHeader.add(lblPurchaseFeeHeader, java.awt.BorderLayout.CENTER);

        purchaseFeePanel.add(purchaseFeeBoxHeader, java.awt.BorderLayout.PAGE_START);

        purchaseFeeBoxContent.setBackground(new java.awt.Color(0, 204, 153));
        purchaseFeeBoxContent.setLayout(new java.awt.BorderLayout());

        lblPurchaseFeeBoxContent.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPurchaseFeeBoxContent.setForeground(new java.awt.Color(255, 255, 255));
        lblPurchaseFeeBoxContent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPurchaseFeeBoxContent.setText("jLabel3");
        purchaseFeeBoxContent.add(lblPurchaseFeeBoxContent, java.awt.BorderLayout.CENTER);

        purchaseFeePanel.add(purchaseFeeBoxContent, java.awt.BorderLayout.CENTER);

        boxContainerPanel.add(purchaseFeePanel);

        supplierBoxPanel.setLayout(new java.awt.BorderLayout());

        supplierBoxHeader.setBackground(new java.awt.Color(1, 160, 214));
        supplierBoxHeader.setPreferredSize(new java.awt.Dimension(277, 40));
        supplierBoxHeader.setLayout(new java.awt.BorderLayout());

        lblSupplierHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblSupplierHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblSupplierHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierHeader.setText("Nhà cung cấp");
        supplierBoxHeader.add(lblSupplierHeader, java.awt.BorderLayout.CENTER);

        supplierBoxPanel.add(supplierBoxHeader, java.awt.BorderLayout.PAGE_START);

        supplierBoxContent.setBackground(new java.awt.Color(0, 153, 204));
        supplierBoxContent.setLayout(new java.awt.BorderLayout());

        lblSupplierBoxContent.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblSupplierBoxContent.setForeground(new java.awt.Color(255, 255, 255));
        lblSupplierBoxContent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierBoxContent.setText("jLabel2");
        supplierBoxContent.add(lblSupplierBoxContent, java.awt.BorderLayout.CENTER);

        supplierBoxPanel.add(supplierBoxContent, java.awt.BorderLayout.CENTER);

        boxContainerPanel.add(supplierBoxPanel);

        bookBoxPanel.setLayout(new java.awt.BorderLayout());

        bookBoxHeader.setBackground(new java.awt.Color(204, 102, 255));
        bookBoxHeader.setPreferredSize(new java.awt.Dimension(277, 40));
        bookBoxHeader.setLayout(new java.awt.BorderLayout());

        lblBookHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblBookHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblBookHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBookHeader.setText("Sách đã nhập");
        bookBoxHeader.add(lblBookHeader, java.awt.BorderLayout.CENTER);

        bookBoxPanel.add(bookBoxHeader, java.awt.BorderLayout.PAGE_START);

        bookBoxContent.setBackground(new java.awt.Color(204, 51, 255));
        bookBoxContent.setLayout(new java.awt.BorderLayout());

        lblBookBoxContent.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblBookBoxContent.setForeground(new java.awt.Color(255, 255, 255));
        lblBookBoxContent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBookBoxContent.setText("jLabel4");
        bookBoxContent.add(lblBookBoxContent, java.awt.BorderLayout.CENTER);

        bookBoxPanel.add(bookBoxContent, java.awt.BorderLayout.CENTER);

        boxContainerPanel.add(bookBoxPanel);

        headerPanel.add(boxContainerPanel, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.PAGE_START);

        contentPanel.setLayout(new java.awt.BorderLayout());

        leftContentPanel.setPreferredSize(new java.awt.Dimension(400, 522));
        leftContentPanel.setLayout(new java.awt.BorderLayout());

        searchTimePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchTimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Thống kê theo thời gian"));
        searchTimePanel.setPreferredSize(new java.awt.Dimension(300, 100));
        searchTimePanel.setLayout(new java.awt.BorderLayout());

        radioTimePanel.setPreferredSize(new java.awt.Dimension(130, 77));
        radioTimePanel.setLayout(new java.awt.GridLayout(3, 0));

        radYearPanel.setBackground(new java.awt.Color(255, 255, 255));
        radYearPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        radYear.setBackground(new java.awt.Color(255, 255, 255));
        btnGroupTime.add(radYear);
        radYear.setText("Theo năm");
        radYear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radYearMouseClicked(evt);
            }
        });
        radYearPanel.add(radYear);

        radioTimePanel.add(radYearPanel);

        radMonthPanel.setBackground(new java.awt.Color(255, 255, 255));
        radMonthPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        radMonth.setBackground(new java.awt.Color(255, 255, 255));
        btnGroupTime.add(radMonth);
        radMonth.setSelected(true);
        radMonth.setText("Theo tháng / quý");
        radMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radMonthMouseClicked(evt);
            }
        });
        radMonthPanel.add(radMonth);

        radioTimePanel.add(radMonthPanel);

        radDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        radDatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        radDate.setBackground(new java.awt.Color(255, 255, 255));
        btnGroupTime.add(radDate);
        radDate.setText("Từ ngày -> ngày");
        radDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radDateMouseClicked(evt);
            }
        });
        radDatePanel.add(radDate);

        radioTimePanel.add(radDatePanel);

        searchTimePanel.add(radioTimePanel, java.awt.BorderLayout.LINE_START);

        timeInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        timeInputPanel.setLayout(new java.awt.CardLayout());

        yearInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        yearInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("năm -> năm"));
        yearInputPanel.setLayout(new java.awt.BorderLayout());

        yearBtnPanel.setBackground(new java.awt.Color(255, 255, 255));
        yearBtnPanel.setPreferredSize(new java.awt.Dimension(60, 54));
        yearBtnPanel.setLayout(new java.awt.GridLayout(2, 0, 0, 2));

        btnSearchYear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Hinh/Search.png"))); // NOI18N
        btnSearchYear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchYearMouseClicked(evt);
            }
        });
        yearBtnPanel.add(btnSearchYear);

        btnRefreshYear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Hinh/Refresh.png"))); // NOI18N
        btnRefreshYear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshYearMouseClicked(evt);
            }
        });
        yearBtnPanel.add(btnRefreshYear);

        yearInputPanel.add(yearBtnPanel, java.awt.BorderLayout.LINE_END);

        yearChooserPanel.setLayout(new java.awt.GridLayout(2, 0));

        startYearPanel.setBackground(new java.awt.Color(255, 255, 255));
        startYearPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        lblSearchStartYear.setText("Từ năm :");
        lblSearchStartYear.setPreferredSize(new java.awt.Dimension(54, 16));
        startYearPanel.add(lblSearchStartYear);
        startYearPanel.add(txtStartYear);

        yearChooserPanel.add(startYearPanel);

        endYearPanel.setBackground(new java.awt.Color(255, 255, 255));
        endYearPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        lblSearchEndYear.setText("Đến năm :");
        endYearPanel.add(lblSearchEndYear);
        endYearPanel.add(txtEndYear);

        yearChooserPanel.add(endYearPanel);

        yearInputPanel.add(yearChooserPanel, java.awt.BorderLayout.CENTER);

        timeInputPanel.add(yearInputPanel, "YEAR");

        preciousInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        preciousInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tháng / Quý"));
        preciousInputPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 15));

        lblChooseYear.setText("Chọn năm :");
        lblChooseYear.setPreferredSize(new java.awt.Dimension(65, 16));
        preciousInputPanel.add(lblChooseYear);

        cboxChooseYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboxChooseYear.setPreferredSize(new java.awt.Dimension(80, 25));
        cboxChooseYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxChooseYearItemStateChanged(evt);
            }
        });
        preciousInputPanel.add(cboxChooseYear);

        timeInputPanel.add(preciousInputPanel, "PRECIOUS");

        dateInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        dateInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("ngày -> ngày"));
        dateInputPanel.setLayout(new java.awt.BorderLayout());

        dateButtonPanel.setBackground(new java.awt.Color(255, 255, 255));
        dateButtonPanel.setPreferredSize(new java.awt.Dimension(60, 64));
        dateButtonPanel.setLayout(new java.awt.GridLayout(2, 0, 0, 2));

        btnSearchDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Hinh/Search.png"))); // NOI18N
        btnSearchDate.setPreferredSize(new java.awt.Dimension(50, 31));
        btnSearchDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchDateMouseClicked(evt);
            }
        });
        dateButtonPanel.add(btnSearchDate);

        btnRefreshDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Hinh/Refresh.png"))); // NOI18N
        btnRefreshDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshDateMouseClicked(evt);
            }
        });
        btnRefreshDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshDateActionPerformed(evt);
            }
        });
        dateButtonPanel.add(btnRefreshDate);

        dateInputPanel.add(dateButtonPanel, java.awt.BorderLayout.LINE_END);

        dateChooserPanel.setBackground(new java.awt.Color(255, 255, 255));
        dateChooserPanel.setLayout(new java.awt.GridLayout(2, 0));

        startDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        startDatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        lblStartDate.setText("Từ ngày :");
        lblStartDate.setPreferredSize(new java.awt.Dimension(56, 16));
        startDatePanel.add(lblStartDate);

        txtStartDate.setPreferredSize(new java.awt.Dimension(110, 22));
        startDatePanel.add(txtStartDate);

        dateChooserPanel.add(startDatePanel);

        endDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        endDatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        lblEndDate.setText("Đến ngày :");
        endDatePanel.add(lblEndDate);

        txtEndDate.setPreferredSize(new java.awt.Dimension(110, 22));
        endDatePanel.add(txtEndDate);

        dateChooserPanel.add(endDatePanel);

        dateInputPanel.add(dateChooserPanel, java.awt.BorderLayout.CENTER);

        timeInputPanel.add(dateInputPanel, "DATE");

        searchTimePanel.add(timeInputPanel, java.awt.BorderLayout.CENTER);

        leftContentPanel.add(searchTimePanel, java.awt.BorderLayout.PAGE_START);

        subDataPanel.setBackground(new java.awt.Color(255, 255, 255));
        subDataPanel.setLayout(new java.awt.CardLayout());

        dateChartPanel.setBackground(new java.awt.Color(255, 255, 255));
        dateChartPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tỉ lệ chi tiêu"));

        javax.swing.GroupLayout dateChartPanelLayout = new javax.swing.GroupLayout(dateChartPanel);
        dateChartPanel.setLayout(dateChartPanelLayout);
        dateChartPanelLayout.setHorizontalGroup(
            dateChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );
        dateChartPanelLayout.setVerticalGroup(
            dateChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );

        subDataPanel.add(dateChartPanel, "DATE");

        yearChartPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout yearChartPanelLayout = new javax.swing.GroupLayout(yearChartPanel);
        yearChartPanel.setLayout(yearChartPanelLayout);
        yearChartPanelLayout.setHorizontalGroup(
            yearChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        yearChartPanelLayout.setVerticalGroup(
            yearChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
        );

        subDataPanel.add(yearChartPanel, "YEAR");

        preciousChartPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout preciousChartPanelLayout = new javax.swing.GroupLayout(preciousChartPanel);
        preciousChartPanel.setLayout(preciousChartPanelLayout);
        preciousChartPanelLayout.setHorizontalGroup(
            preciousChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        preciousChartPanelLayout.setVerticalGroup(
            preciousChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
        );

        subDataPanel.add(preciousChartPanel, "PRECIOUS");

        leftContentPanel.add(subDataPanel, java.awt.BorderLayout.CENTER);

        contentPanel.add(leftContentPanel, java.awt.BorderLayout.LINE_START);

        dataPanel.setBackground(new java.awt.Color(255, 255, 255));
        dataPanel.setLayout(new java.awt.CardLayout());

        datePanel.setBackground(new java.awt.Color(255, 255, 255));
        datePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách phiếu nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 14))); // NOI18N
        datePanel.setLayout(new java.awt.BorderLayout());

        dateHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        dateHeaderPanel.setPreferredSize(new java.awt.Dimension(760, 50));
        dateHeaderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 7, 10));

        lblDateTitle1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblDateTitle1.setText("Từ ngày");
        lblDateTitle1.setPreferredSize(new java.awt.Dimension(70, 25));
        dateHeaderPanel.add(lblDateTitle1);

        lblStartDateTitle.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblStartDateTitle.setText("10/10/2025");
        dateHeaderPanel.add(lblStartDateTitle);

        lblDateTitle2.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblDateTitle2.setText("đến ngày ");
        dateHeaderPanel.add(lblDateTitle2);

        lblEndDateTitle.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblEndDateTitle.setText("12/12/2025");
        dateHeaderPanel.add(lblEndDateTitle);

        datePanel.add(dateHeaderPanel, java.awt.BorderLayout.PAGE_START);

        dateTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        dateTablePanel.setLayout(new java.awt.BorderLayout());

        scroll.setViewportView(tblDate);

        dateTablePanel.add(scroll, java.awt.BorderLayout.CENTER);

        datePanel.add(dateTablePanel, java.awt.BorderLayout.CENTER);

        dataPanel.add(datePanel, "DATE");

        yearPanel.setBackground(new java.awt.Color(255, 255, 255));
        yearPanel.setLayout(new java.awt.BorderLayout());

        yearPanelTitle.setBackground(new java.awt.Color(255, 255, 255));
        yearPanelTitle.setPreferredSize(new java.awt.Dimension(724, 50));

        lblYearHeaderTitle1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblYearHeaderTitle1.setText("Thống kê phiếu phạt từ năm");
        yearPanelTitle.add(lblYearHeaderTitle1);

        lblStartYear.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblStartYear.setText("2020");
        lblStartYear.setPreferredSize(new java.awt.Dimension(42, 25));
        yearPanelTitle.add(lblStartYear);

        lblYearHeaderTitle2.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblYearHeaderTitle2.setText("đến năm");
        yearPanelTitle.add(lblYearHeaderTitle2);

        lblEndYear.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblEndYear.setText("2025");
        lblEndYear.setPreferredSize(new java.awt.Dimension(42, 25));
        yearPanelTitle.add(lblEndYear);

        yearPanel.add(yearPanelTitle, java.awt.BorderLayout.PAGE_START);

        yearTablePanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(tblYear);

        yearTablePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        yearPanel.add(yearTablePanel, java.awt.BorderLayout.CENTER);

        dataPanel.add(yearPanel, "YEAR");

        preciousPanel.setBackground(new java.awt.Color(255, 255, 255));
        preciousPanel.setLayout(new java.awt.BorderLayout());

        monthDataPanel.setBackground(new java.awt.Color(255, 255, 255));
        monthDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Theo tháng"));
        monthDataPanel.setPreferredSize(new java.awt.Dimension(787, 240));
        monthDataPanel.setLayout(new java.awt.BorderLayout());

        preciousMonthFooter.setBackground(new java.awt.Color(255, 255, 255));
        preciousMonthFooter.setPreferredSize(new java.awt.Dimension(828, 40));
        preciousMonthFooter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));

        lblPreciousPage.setText("Quý :");
        preciousMonthFooter.add(lblPreciousPage);

        firstPageTab.setBackground(new java.awt.Color(204, 204, 204));
        firstPageTab.setPreferredSize(new java.awt.Dimension(30, 30));
        firstPageTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                firstPageTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                firstPageTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                firstPageTabMouseExited(evt);
            }
        });
        firstPageTab.setLayout(new java.awt.BorderLayout());

        lblPageOne.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPageOne.setText("1");
        firstPageTab.add(lblPageOne, java.awt.BorderLayout.CENTER);

        preciousMonthFooter.add(firstPageTab);

        secondePageTab.setBackground(new java.awt.Color(204, 204, 204));
        secondePageTab.setPreferredSize(new java.awt.Dimension(30, 30));
        secondePageTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                secondePageTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                secondePageTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                secondePageTabMouseExited(evt);
            }
        });
        secondePageTab.setLayout(new java.awt.BorderLayout());

        lblPageTwo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPageTwo.setText("2");
        secondePageTab.add(lblPageTwo, java.awt.BorderLayout.CENTER);

        preciousMonthFooter.add(secondePageTab);

        thirdPageTab.setBackground(new java.awt.Color(204, 204, 204));
        thirdPageTab.setPreferredSize(new java.awt.Dimension(30, 30));
        thirdPageTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                thirdPageTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                thirdPageTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                thirdPageTabMouseExited(evt);
            }
        });
        thirdPageTab.setLayout(new java.awt.BorderLayout());

        lblPageThree.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPageThree.setText("3");
        thirdPageTab.add(lblPageThree, java.awt.BorderLayout.CENTER);

        preciousMonthFooter.add(thirdPageTab);

        fourthPageTab.setBackground(new java.awt.Color(204, 204, 204));
        fourthPageTab.setPreferredSize(new java.awt.Dimension(30, 30));
        fourthPageTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fourthPageTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fourthPageTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fourthPageTabMouseExited(evt);
            }
        });
        fourthPageTab.setLayout(new java.awt.BorderLayout());

        lblPageFour.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPageFour.setText("4");
        fourthPageTab.add(lblPageFour, java.awt.BorderLayout.CENTER);

        preciousMonthFooter.add(fourthPageTab);

        monthDataPanel.add(preciousMonthFooter, java.awt.BorderLayout.PAGE_END);

        monthTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        monthTablePanel.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setViewportView(tblMonth);

        monthTablePanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        monthDataPanel.add(monthTablePanel, java.awt.BorderLayout.CENTER);

        preciousPanel.add(monthDataPanel, java.awt.BorderLayout.PAGE_START);

        preciousDataPanel.setBackground(new java.awt.Color(255, 255, 255));
        preciousDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Theo quý"));
        preciousDataPanel.setLayout(new java.awt.BorderLayout());

        preciousDataHeader.setBackground(new java.awt.Color(255, 255, 255));
        preciousDataHeader.setPreferredSize(new java.awt.Dimension(828, 30));
        preciousDataHeader.setLayout(new java.awt.BorderLayout());

        preciousTaskbar.setBackground(new java.awt.Color(255, 255, 255));
        preciousTaskbar.setPreferredSize(new java.awt.Dimension(400, 30));
        preciousTaskbar.setLayout(new java.awt.GridLayout(1, 3));

        employeeTab.setBackground(new java.awt.Color(255, 255, 255));
        employeeTab.setLayout(new java.awt.BorderLayout());

        lblEmployeeTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeTab.setText("Nhân viên xử lý");
        lblEmployeeTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEmployeeTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblEmployeeTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblEmployeeTabMouseExited(evt);
            }
        });
        employeeTab.add(lblEmployeeTab, java.awt.BorderLayout.CENTER);

        preciousTaskbar.add(employeeTab);

        supplierTab.setBackground(new java.awt.Color(255, 255, 255));
        supplierTab.setLayout(new java.awt.BorderLayout());

        lblSupplierTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplierTab.setText("Nhà cung cấp");
        lblSupplierTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSupplierTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSupplierTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSupplierTabMouseExited(evt);
            }
        });
        supplierTab.add(lblSupplierTab, java.awt.BorderLayout.CENTER);

        preciousTaskbar.add(supplierTab);

        bookTab.setBackground(new java.awt.Color(255, 255, 255));
        bookTab.setLayout(new java.awt.BorderLayout());

        lblBookTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBookTab.setText("Sách được nhập");
        lblBookTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBookTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblBookTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblBookTabMouseExited(evt);
            }
        });
        bookTab.add(lblBookTab, java.awt.BorderLayout.CENTER);

        preciousTaskbar.add(bookTab);

        preciousDataHeader.add(preciousTaskbar, java.awt.BorderLayout.LINE_START);

        radPenaltyPanel.setBackground(new java.awt.Color(255, 255, 255));
        radPenaltyPanel.setPreferredSize(new java.awt.Dimension(250, 30));

        btnGroupPenalty.add(radCountAmount);
        radCountAmount.setSelected(true);
        radCountAmount.setText("Số lượng");
        radCountAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radCountAmountMouseClicked(evt);
            }
        });
        radPenaltyPanel.add(radCountAmount);

        btnGroupPenalty.add(radSumFee);
        radSumFee.setText("Tiền nhập");
        radSumFee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radSumFeeMouseClicked(evt);
            }
        });
        radPenaltyPanel.add(radSumFee);

        preciousDataHeader.add(radPenaltyPanel, java.awt.BorderLayout.LINE_END);

        preciousDataPanel.add(preciousDataHeader, java.awt.BorderLayout.PAGE_START);

        preciousTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        preciousTablePanel.setLayout(new java.awt.CardLayout());

        bookTablePanel.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setViewportView(tblBook);

        bookTablePanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        preciousTablePanel.add(bookTablePanel, "LOSTBOOK");

        supplierTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        supplierTablePanel.setLayout(new java.awt.BorderLayout());

        jScrollPane4.setViewportView(tblSupplier);

        supplierTablePanel.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        preciousTablePanel.add(supplierTablePanel, "READER");

        employeeTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        employeeTablePanel.setLayout(new java.awt.BorderLayout());

        jScrollPane5.setViewportView(tblEmployee);

        employeeTablePanel.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        preciousTablePanel.add(employeeTablePanel, "EMPLOYEE");

        preciousDataPanel.add(preciousTablePanel, java.awt.BorderLayout.CENTER);

        preciousPanel.add(preciousDataPanel, java.awt.BorderLayout.CENTER);

        dataPanel.add(preciousPanel, "PRECIOUS");

        contentPanel.add(dataPanel, java.awt.BorderLayout.CENTER);

        add(contentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshDateActionPerformed

    private void radYearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radYearMouseClicked
        radTimeStateChange();
    }//GEN-LAST:event_radYearMouseClicked

    private void radMonthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radMonthMouseClicked
        radTimeStateChange();
    }//GEN-LAST:event_radMonthMouseClicked

    private void radDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radDateMouseClicked
        radTimeStateChange();
    }//GEN-LAST:event_radDateMouseClicked

    private void cboxChooseYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxChooseYearItemStateChanged
        if(cboxChooseYear.getSelectedItem() == null) return;

        String year = cboxChooseYear.getSelectedItem().toString();
        List<StatisticsPreciousData<Long>> employeeListInYear = employeeStats.get(year);
        List<StatisticsPreciousData<String>> supplierListInYear = supplierStats.get(year);
        List<StatisticsPreciousData<Long>> bookListInYear = bookStats.get(year);
        List<PurchaseTimeData> monthList = monthStats.get(year);

        tblEmployee.setList(employeeListInYear);
        tblSupplier.setList(supplierListInYear);
        tblBook.setList(bookListInYear);
        tblMonth.setList(monthList);

        resetPreciousPanel();
        showMonthBarChart(year);
    }//GEN-LAST:event_cboxChooseYearItemStateChanged

    private void radCountAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radCountAmountMouseClicked
        tblEmployee.renderPenaltySheetTable();
        tblSupplier.renderPurchaseSheetTable();
        tblBook.renderPurchaseQuantiyTable();
    }//GEN-LAST:event_radCountAmountMouseClicked

    private void radSumFeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radSumFeeMouseClicked
        tblEmployee.renderPenaltyFeeTable();
        tblSupplier.renderPurchaseFeeTable();
        tblBook.renderPurchaseFeeTable();
    }//GEN-LAST:event_radSumFeeMouseClicked

    private void firstPageTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_firstPageTabMouseClicked
        pageSelected(firstPageTab, lblPageOne);
        tblMonth.renderMonthsTable(1);
    }//GEN-LAST:event_firstPageTabMouseClicked

    private void secondePageTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_secondePageTabMouseClicked
        pageSelected(secondePageTab, lblPageTwo);
        tblMonth.renderMonthsTable(2);

    }//GEN-LAST:event_secondePageTabMouseClicked

    private void thirdPageTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thirdPageTabMouseClicked
        pageSelected(thirdPageTab, lblPageThree);
        tblMonth.renderMonthsTable(3);
    }//GEN-LAST:event_thirdPageTabMouseClicked

    private void fourthPageTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fourthPageTabMouseClicked
        pageSelected(fourthPageTab, lblPageFour);
        tblMonth.renderMonthsTable(4);
    }//GEN-LAST:event_fourthPageTabMouseClicked

    private void firstPageTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_firstPageTabMouseEntered
        pageHover(firstPageTab);
    }//GEN-LAST:event_firstPageTabMouseEntered

    private void secondePageTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_secondePageTabMouseEntered
        pageHover(secondePageTab);
    }//GEN-LAST:event_secondePageTabMouseEntered

    private void thirdPageTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thirdPageTabMouseEntered
        pageHover(thirdPageTab);
    }//GEN-LAST:event_thirdPageTabMouseEntered

    private void fourthPageTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fourthPageTabMouseEntered
        pageHover(fourthPageTab);
    }//GEN-LAST:event_fourthPageTabMouseEntered

    private void firstPageTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_firstPageTabMouseExited
        pageClearHover(firstPageTab);
    }//GEN-LAST:event_firstPageTabMouseExited

    private void secondePageTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_secondePageTabMouseExited
        pageClearHover(secondePageTab);
    }//GEN-LAST:event_secondePageTabMouseExited

    private void thirdPageTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thirdPageTabMouseExited
        pageClearHover(thirdPageTab);
    }//GEN-LAST:event_thirdPageTabMouseExited

    private void fourthPageTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fourthPageTabMouseExited
        pageClearHover(fourthPageTab);
    }//GEN-LAST:event_fourthPageTabMouseExited

    private void btnRefreshYearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshYearMouseClicked
        initYearPanel();
    }//GEN-LAST:event_btnRefreshYearMouseClicked

    private void btnSearchYearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchYearMouseClicked
        if(checkYearField()){
            String startYear = Integer.toString(txtStartYear.getYear());
            String endYear = Integer.toString(txtEndYear.getYear());

            renderYearPanel(startYear, endYear);
        }


    }//GEN-LAST:event_btnSearchYearMouseClicked

    private void btnSearchDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchDateMouseClicked
        if(checkDateField()) {
            Date startDate = txtStartDate.getDate();
            Date endDate = txtEndDate.getDate();
            renderDatePanel(startDate, endDate);
        }


    }//GEN-LAST:event_btnSearchDateMouseClicked

    private void btnRefreshDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshDateMouseClicked
        initDatePanel();
    }//GEN-LAST:event_btnRefreshDateMouseClicked

    private void lblEmployeeTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeTabMouseClicked
        tabSelected(employeeTab, "EMPLOYEE");
    }//GEN-LAST:event_lblEmployeeTabMouseClicked

    private void lblEmployeeTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeTabMouseEntered
        tabHover(employeeTab);
    }//GEN-LAST:event_lblEmployeeTabMouseEntered

    private void lblEmployeeTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeTabMouseExited
        tabClearHover(employeeTab);
    }//GEN-LAST:event_lblEmployeeTabMouseExited

    private void lblSupplierTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierTabMouseClicked
        tabSelected(supplierTab, "READER");
    }//GEN-LAST:event_lblSupplierTabMouseClicked

    private void lblSupplierTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierTabMouseEntered
        tabHover(supplierTab);
    }//GEN-LAST:event_lblSupplierTabMouseEntered

    private void lblSupplierTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSupplierTabMouseExited
        tabClearHover(supplierTab);
    }//GEN-LAST:event_lblSupplierTabMouseExited

    private void lblBookTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBookTabMouseClicked
        tabSelected(bookTab, "LOSTBOOK");
    }//GEN-LAST:event_lblBookTabMouseClicked

    private void lblBookTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBookTabMouseEntered
        tabHover(bookTab);
    }//GEN-LAST:event_lblBookTabMouseEntered

    private void lblBookTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBookTabMouseExited
        tabClearHover(bookTab);
    }//GEN-LAST:event_lblBookTabMouseExited


    private void radPenaltySheetStateChanged(javax.swing.event.ChangeEvent evt) {

    }

    private void radSearchYearStateChanged(javax.swing.event.ChangeEvent evt) {

    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel blankPanel;
    private javax.swing.JPanel blankPanel2;
    private javax.swing.JPanel blankPanel3;
    private javax.swing.JPanel bookBoxContent;
    private javax.swing.JPanel bookBoxHeader;
    private javax.swing.JPanel bookBoxPanel;
    private javax.swing.JPanel bookTab;
    private javax.swing.JPanel bookTablePanel;
    private javax.swing.JPanel boxContainerPanel;
    private javax.swing.ButtonGroup btnGroupPenalty;
    private javax.swing.ButtonGroup btnGroupTime;
    private javax.swing.JButton btnRefreshDate;
    private javax.swing.JButton btnRefreshYear;
    private javax.swing.JButton btnSearchDate;
    private javax.swing.JButton btnSearchYear;
    private javax.swing.JComboBox<String> cboxChooseYear;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JPanel dateButtonPanel;
    private javax.swing.JPanel dateChartPanel;
    private javax.swing.JPanel dateChooserPanel;
    private javax.swing.JPanel dateHeaderPanel;
    private javax.swing.JPanel dateInputPanel;
    private javax.swing.JPanel datePanel;
    private javax.swing.JPanel dateTablePanel;
    private javax.swing.JPanel employeeTab;
    private javax.swing.JPanel employeeTablePanel;
    private javax.swing.JPanel endDatePanel;
    private javax.swing.JPanel endYearPanel;
    private javax.swing.JPanel firstPageTab;
    private javax.swing.JPanel fourthPageTab;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblBookBoxContent;
    private javax.swing.JLabel lblBookHeader;
    private javax.swing.JLabel lblBookTab;
    private javax.swing.JLabel lblChooseYear;
    private javax.swing.JLabel lblDateTitle1;
    private javax.swing.JLabel lblDateTitle2;
    private javax.swing.JLabel lblEmployeeTab;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblEndDateTitle;
    private javax.swing.JLabel lblEndYear;
    private javax.swing.JLabel lblPageFour;
    private javax.swing.JLabel lblPageOne;
    private javax.swing.JLabel lblPageThree;
    private javax.swing.JLabel lblPageTwo;
    private javax.swing.JLabel lblPreciousPage;
    private javax.swing.JLabel lblPurchaseBoxContent;
    private javax.swing.JLabel lblPurchaseFeeBoxContent;
    private javax.swing.JLabel lblPurchaseFeeHeader;
    private javax.swing.JLabel lblPurchaseHeader;
    private javax.swing.JLabel lblSearchEndYear;
    private javax.swing.JLabel lblSearchStartYear;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JLabel lblStartDateTitle;
    private javax.swing.JLabel lblStartYear;
    private javax.swing.JLabel lblSupplierBoxContent;
    private javax.swing.JLabel lblSupplierHeader;
    private javax.swing.JLabel lblSupplierTab;
    private javax.swing.JLabel lblYearHeaderTitle1;
    private javax.swing.JLabel lblYearHeaderTitle2;
    private javax.swing.JPanel leftContentPanel;
    private javax.swing.JPanel monthDataPanel;
    private javax.swing.JPanel monthTablePanel;
    private javax.swing.JPanel preciousChartPanel;
    private javax.swing.JPanel preciousDataHeader;
    private javax.swing.JPanel preciousDataPanel;
    private javax.swing.JPanel preciousInputPanel;
    private javax.swing.JPanel preciousMonthFooter;
    private javax.swing.JPanel preciousPanel;
    private javax.swing.JPanel preciousTablePanel;
    private javax.swing.JPanel preciousTaskbar;
    private javax.swing.JPanel purchaseBoxContent;
    private javax.swing.JPanel purchaseBoxHeader;
    private javax.swing.JPanel purchaseBoxPanel;
    private javax.swing.JPanel purchaseFeeBoxContent;
    private javax.swing.JPanel purchaseFeeBoxHeader;
    private javax.swing.JPanel purchaseFeePanel;
    private javax.swing.JRadioButton radCountAmount;
    private javax.swing.JRadioButton radDate;
    private javax.swing.JPanel radDatePanel;
    private javax.swing.JRadioButton radMonth;
    private javax.swing.JPanel radMonthPanel;
    private javax.swing.JPanel radPenaltyPanel;
    private javax.swing.JRadioButton radSumFee;
    private javax.swing.JRadioButton radYear;
    private javax.swing.JPanel radYearPanel;
    private javax.swing.JPanel radioTimePanel;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JPanel searchTimePanel;
    private javax.swing.JPanel secondePageTab;
    private javax.swing.JPanel startDatePanel;
    private javax.swing.JPanel startYearPanel;
    private javax.swing.JPanel subDataPanel;
    private javax.swing.JPanel supplierBoxContent;
    private javax.swing.JPanel supplierBoxHeader;
    private javax.swing.JPanel supplierBoxPanel;
    private javax.swing.JPanel supplierTab;
    private javax.swing.JPanel supplierTablePanel;
    private GUI.Component.Panel.Statistics.Components.PurchaseBookTable tblBook;
    private GUI.Component.Panel.Statistics.Components.PurchaseDateStatisticsTable tblDate;
    private GUI.Component.Panel.Statistics.Components.PenaltyEmployeeTable tblEmployee;
    private GUI.Component.Panel.Statistics.Components.PurchaseMonthStatisticsTable tblMonth;
    private GUI.Component.Panel.Statistics.Components.PurchaseSupplierTable tblSupplier;
    private GUI.Component.Panel.Statistics.Components.PurchaseYearStatisticsTable tblYear;
    private javax.swing.JPanel thirdPageTab;
    private javax.swing.JPanel timeInputPanel;
    private com.toedter.calendar.JDateChooser txtEndDate;
    private com.toedter.calendar.JYearChooser txtEndYear;
    private com.toedter.calendar.JDateChooser txtStartDate;
    private com.toedter.calendar.JYearChooser txtStartYear;
    private javax.swing.JPanel yearBtnPanel;
    private javax.swing.JPanel yearChartPanel;
    private javax.swing.JPanel yearChooserPanel;
    private javax.swing.JPanel yearInputPanel;
    private javax.swing.JPanel yearPanel;
    private javax.swing.JPanel yearPanelTitle;
    private javax.swing.JPanel yearTablePanel;
    // End of variables declaration//GEN-END:variables
}
