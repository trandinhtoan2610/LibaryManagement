package GUI.Component.Panel.Statistics.Components;

import DTO.Statistics.PenaltyTimeData;
import DTO.Statistics.PurchaseTimeData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FeeMonthBarChart extends JPanel {

    // Constructor dùng cho tiền phạt theo tháng
    public FeeMonthBarChart(List<PenaltyTimeData> dataList) {
        DefaultCategoryDataset dataset = createPenaltyDataset(dataList);
        setupChart(dataset, "Biểu đồ tiền phạt theo tháng", "Tháng", "Tiền phạt (VNĐ)", new Color(0, 123, 255));
    }

    // Constructor mở rộng: dùng cho PurchaseTimeData theo tháng
    public FeeMonthBarChart(List<PurchaseTimeData> dataList, String dataType) {
        DefaultCategoryDataset dataset = createPurchaseDataset(dataList, dataType);
        String yLabel = switch (dataType) {
            case "bookQuantity" -> "Số lượng sách";
            case "doneSheet" -> "Phiếu hoàn thành";
            case "cancelSheet" -> "Phiếu hủy";
            default -> "Tiền mua (VNĐ)";
        };

        Color color = switch (dataType) {
            case "bookQuantity" -> new Color(255, 193, 7);     // vàng
            case "doneSheet" -> new Color(40, 167, 69);        // xanh lá
            case "cancelSheet" -> new Color(220, 53, 69);      // đỏ
            default -> new Color(0, 123, 255);                 // xanh dương
        };

        setupChart(dataset, "Biểu đồ " + yLabel.toLowerCase() + " theo tháng", "Tháng", yLabel, color);
    }

    private void setupChart(DefaultCategoryDataset dataset, String title, String xLabel, String yLabel, Color barColor) {
        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, barColor);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

        barChart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
    }

    private DefaultCategoryDataset createPenaltyDataset(List<PenaltyTimeData> dataList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (dataList != null) {
            for (PenaltyTimeData data : dataList) {
                dataset.addValue(data.getPenaltyFee(), "Tiền phạt", String.valueOf(data.getTime()));
            }
        }
        return dataset;
    }

    private DefaultCategoryDataset createPurchaseDataset(List<PurchaseTimeData> dataList, String dataType) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (dataList != null) {
            for (PurchaseTimeData data : dataList) {
                String label = switch (dataType) {
                    case "bookQuantity" -> "Số sách";
                    case "doneSheet" -> "Phiếu hoàn thành";
                    case "cancelSheet" -> "Phiếu hủy";
                    default -> "Tiền mua";
                };

                Number value = switch (dataType) {
                    case "bookQuantity" -> data.getBookQuantity();
                    case "doneSheet" -> data.getDoneSheet();
                    case "cancelSheet" -> data.getCancelSheet();
                    default -> data.getPurchaseFee();
                };

                dataset.addValue(value, label, String.valueOf(data.getTime()));
            }
        }
        return dataset;
    }
}
