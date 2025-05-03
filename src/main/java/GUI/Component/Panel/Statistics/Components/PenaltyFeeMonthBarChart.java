package GUI.Component.Panel.Statistics.Components;

import DTO.Statistics.PenaltyTimeData;
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

public class PenaltyFeeMonthBarChart extends JPanel {

    public PenaltyFeeMonthBarChart(List<PenaltyTimeData> dataList) {
        DefaultCategoryDataset dataset = createDataset(dataList);
        JFreeChart barChart = ChartFactory.createBarChart(
                "Biểu đồ tiền phạt theo tháng",  // Title
                "Tháng",                         // X-axis Label
                "Tiền phạt (VNĐ)",               // Y-axis Label
                dataset,                         // Dữ liệu
                PlotOrientation.VERTICAL,        // Hướng biểu đồ
                false,                            // Hiện legend
                true,                            // Hiện tooltips
                false                            // Không dùng URLs
        );


        // Tuỳ chỉnh hiển thị
        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 123, 255));
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

        barChart.setBackgroundPaint(Color.WHITE);

        // Nền trắng cho plot area
        plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY); // Màu lưới trục X
        plot.setRangeGridlinePaint(Color.GRAY);  // Màu lưới trục Y
    }

    private DefaultCategoryDataset createDataset(List<PenaltyTimeData> dataList) {
        if(dataList == null) return null;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (PenaltyTimeData data : dataList) {
            String month = Integer.toString(data.getTime());
            dataset.addValue(data.getPenaltyFee(), "Tiền phạt", month);
        }
        return dataset;
    }
}
