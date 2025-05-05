package GUI.Component.Panel.Statistics;

import BUS.BookBUS;
import BUS.BorrowSheetBUS;
import BUS.PenaltyDetailsBUS;
import DTO.BookViewModel;
import DTO.Statistics.QuarterData;
import GUI.Component.Panel.Statistics.Components.BoxDashBoard;
import GUI.Component.Table.BookTable;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import com.toedter.calendar.JDateChooser;

public class BookStatisticsPanel extends JPanel {
    private BookBUS bookBUS;
    private BorrowSheetBUS borrowSheetBUS;
    private PenaltyDetailsBUS penaltyDetailsBUS;
    private BoxDashBoard totalBooksBox;
    private BoxDashBoard totalBorrowedBox;
    private BoxDashBoard totalPenaltiesBox;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;
    private JComboBox<String> yearComboBox;
    private JButton filterButton;
    private JButton resetFilterButton;
    private BookTable statisticsTable;
    private ChartPanel chartPanel;

    private static final String[] STATISTICS_HEADER = {
            "Mã sách", "Tên sách", "Thể loại", "Tác Giả", "Nhà xuất bản", "Số lượng"
    };

    public BookStatisticsPanel() {
        bookBUS = new BookBUS();
        borrowSheetBUS = new BorrowSheetBUS();
        penaltyDetailsBUS = new PenaltyDetailsBUS();
        initComponents();
        loadStatistics();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 1. Tổng quan (Overview)
        JPanel overviewPanel = new JPanel();
        overviewPanel.setLayout(new BoxLayout(overviewPanel, BoxLayout.X_AXIS));
        overviewPanel.setBorder(BorderFactory.createTitledBorder("Tổng quan"));
        overviewPanel.setBackground(new Color(240, 240, 240));
        overviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        overviewPanel.add(Box.createHorizontalGlue());

        totalBooksBox = new BoxDashBoard("Tổng số sách", "0");
        overviewPanel.add(totalBooksBox);
        overviewPanel.add(Box.createHorizontalStrut(10));

        totalBorrowedBox = new BoxDashBoard("Sách đang mượn", "0");
        overviewPanel.add(totalBorrowedBox);
        overviewPanel.add(Box.createHorizontalStrut(10));

        totalPenaltiesBox = new BoxDashBoard("Sách bị phạt", "0");
        overviewPanel.add(totalPenaltiesBox);

        overviewPanel.add(Box.createHorizontalGlue());

        // 2. Bộ lọc thống kê
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));
        filterPanel.setBackground(Color.WHITE);

        fromDateChooser = new JDateChooser();
        fromDateChooser.setDateFormatString("dd/MM/yyyy");
        toDateChooser = new JDateChooser();
        toDateChooser.setDateFormatString("dd/MM/yyyy");

        yearComboBox = new JComboBox<>();
        yearComboBox.addItem(""); // Thêm lựa chọn trống để không lọc theo năm
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 10; i--) {
            yearComboBox.addItem(String.valueOf(i));
        }

        filterButton = new JButton("Lọc");
        filterButton.addActionListener(e -> applyFilter());

        resetFilterButton = new JButton("Xóa bộ lọc");
        resetFilterButton.addActionListener(e -> resetFilter());

        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(fromDateChooser);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(toDateChooser);
        filterPanel.add(new JLabel("Năm:"));
        filterPanel.add(yearComboBox);
        filterPanel.add(filterButton);
        filterPanel.add(resetFilterButton);

        // 3. Biểu đồ thống kê
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(BorderFactory.createTitledBorder("Biểu đồ tỉ lệ sách theo thể loại"));
        chartPanel = new ChartPanel(null);
        chartContainer.add(chartPanel, BorderLayout.CENTER);

        // 4. Bảng chi tiết thống kê
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Chi tiết thống kê"));
        statisticsTable = new BookTable(STATISTICS_HEADER);

        // Đặt độ rộng cột tùy chỉnh
        statisticsTable.getColumnModel().getColumn(0).setPreferredWidth(80);   // Mã sách
        statisticsTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Tên sách
        statisticsTable.getColumnModel().getColumn(2).setPreferredWidth(120);  // Thể loại
        statisticsTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Số lượng trong kho
        statisticsTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Số lượng đã mượn
        statisticsTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Số lần bị phạt

        JScrollPane tableScrollPane = new JScrollPane(statisticsTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Sắp xếp giao diện
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(overviewPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chartContainer, tablePanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void loadStatistics() {
        List<BookViewModel> books = bookBUS.getAllBooksForDisplay();
        updateOverview(books);
        updateChart(books);
        updateTable(books);
    }

    private void applyFilter() {
        Date fromDate = fromDateChooser.getDate();
        Date toDate = toDateChooser.getDate();
        String selectedYear = (String) yearComboBox.getSelectedItem();

        List<BookViewModel> filteredBooks = bookBUS.getAllBooksForDisplay();

        // Lọc theo năm xuất bản
        if (selectedYear != null && !selectedYear.isEmpty()) {
            try {
                int year = Integer.parseInt(selectedYear);
                filteredBooks = filteredBooks.stream()
                        .filter(book -> book.getYearOfPublication() != null && book.getYearOfPublication().getValue() == year)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Năm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Lọc theo khoảng ngày (dựa trên phiếu mượn)
        if (fromDate != null && toDate != null) {
            if (fromDate.after(toDate)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                List<QuarterData> borrowData = borrowSheetBUS.getQuarterBookDataByDate(fromDate, toDate);
                Set<Long> borrowedBookIds = borrowData.stream()
                        .map(data -> (long) data.getId())
                        .collect(Collectors.toSet());
                filteredBooks = filteredBooks.stream()
                        .filter(book -> borrowedBookIds.contains(book.getId()))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc theo ngày: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        updateOverview(filteredBooks);
        updateChart(filteredBooks);
        updateTable(filteredBooks);
    }

    private void resetFilter() {
        // Xóa các giá trị bộ lọc
        fromDateChooser.setDate(null);
        toDateChooser.setDate(null);
        yearComboBox.setSelectedIndex(0); // Chọn giá trị trống

        // Tải lại thống kê tổng quan
        loadStatistics();
    }

    private void updateOverview(List<BookViewModel> books) {
        int totalBooks = books.size();
        int totalBorrowed = books.stream().mapToInt(BookViewModel::getBorrowedQuantity).sum();
        int totalPenalties = books.stream().mapToInt(this::getPenaltyCountByBookId).sum();

        totalBooksBox.setValue(String.valueOf(totalBooks));
        totalBorrowedBox.setValue(String.valueOf(totalBorrowed));
        totalPenaltiesBox.setValue(String.valueOf(totalPenalties));
    }

    private void updateChart(List<BookViewModel> books) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (BookViewModel book : books) {
            String category = book.getCategoryName();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Tỉ lệ sách theo thể loại",
                dataset,
                true,
                true,
                false
        );
        chartPanel.setChart(chart);
    }

    private void updateTable(List<BookViewModel> books) {
        statisticsTable.setBooks(books);
    }

    private int getPenaltyCountByBookId(BookViewModel book) {
        Long bookId = book.getId();
        return (int) PenaltyDetailsBUS.penaltyDetailsList.stream()
                .filter(detail -> detail.getBookID().equals(bookId))
                .count();
    }
}