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

public class FeeYearBarChart extends JPanel {

    public FeeYearBarChart(List<PenaltyTimeData> dataList) {
        DefaultCategoryDataset dataset = createPenaltyDataset(dataList);
        setupChart(dataset, "Biểu đồ tiền phạt theo năm", "Năm", "Tiền phạt (VNĐ)", new Color(0, 123, 255));
    }

    // Constructor mới dành cho dữ liệu mua sách
    public FeeYearBarChart(List<PurchaseTimeData> dataList, boolean isPurchaseChart) {
        DefaultCategoryDataset dataset = createPurchaseDataset(dataList);
        setupChart(dataset, "Biểu đồ tiền mua theo năm", "Năm", "Tiền mua (VNĐ)", new Color(40, 167, 69));
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

        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
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

    private DefaultCategoryDataset createPurchaseDataset(List<PurchaseTimeData> dataList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (dataList != null) {
            for (PurchaseTimeData data : dataList) {
                dataset.addValue(data.getPurchaseFee(), "Tiền mua", String.valueOf(data.getTime()));
            }
        }
        return dataset;
    }
}
