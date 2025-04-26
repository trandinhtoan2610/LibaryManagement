package GUI.Component.Panel.Statistics.Components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomBookChart extends JPanel {
    private Map<String, Integer> monthlyData;
    private String chartTitle;

    public CustomBookChart(Map<String, Integer> data, String title) {
        this.monthlyData = data;
        this.chartTitle = title;
        setPreferredSize(new Dimension(200, 200)); // Smaller size for compact view
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Adjusted padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw chart title
        drawTitle(g2d);

        // Calculate dimensions
        int margin = 30; // Reduced margin for smaller size
        int chartWidth = getWidth() - 2 * margin;
        int chartHeight = getHeight() - 2 * margin - 20; // Reserve space for labels

        // Draw axes
        drawAxes(g2d, margin, chartWidth, chartHeight);

        // Draw bars
        drawBars(g2d, margin, chartWidth, chartHeight);
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Smaller font for title
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(chartTitle);
        int x = (getWidth() - titleWidth) / 2;
        g2d.setColor(new Color(50, 50, 50));
        g2d.drawString(chartTitle, x, 15);
    }

    private void drawAxes(Graphics2D g2d, int margin, int chartWidth, int chartHeight) {
        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(new Color(80, 80, 80));
        g2d.drawLine(margin, margin + chartHeight, margin + chartWidth, margin + chartHeight); // x-axis
        g2d.drawLine(margin, margin, margin, margin + chartHeight); // y-axis

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10)); // Smaller font for labels
        g2d.drawString("Tháng", getWidth() / 2, getHeight() - 5);
    }

    private void drawBars(Graphics2D g2d, int margin, int chartWidth, int chartHeight) {
        if (monthlyData.isEmpty()) return;

        int maxValue = monthlyData.values().stream().max(Integer::compare).orElse(1);
        int barCount = monthlyData.size();
        int barWidth = Math.min(20, chartWidth / (barCount * 2)); // Adjust bar width for small size
        int spacing = (chartWidth - (barCount * barWidth)) / (barCount + 1);

        Color barColor = new Color(70, 130, 180);
        Color valueColor = new Color(30, 30, 30);

        int i = 0;
        for (Map.Entry<String, Integer> entry : monthlyData.entrySet()) {
            String month = entry.getKey();
            int value = entry.getValue();

            int x = margin + spacing + i * (barWidth + spacing);
            int barHeight = (int) ((double) value / maxValue * chartHeight);
            int y = margin + chartHeight - barHeight;

            g2d.setColor(barColor);
            g2d.fillRect(x, y, barWidth, barHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, barWidth, barHeight);

            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9)); // Smaller font for month labels
            FontMetrics fm = g2d.getFontMetrics();
            int monthWidth = fm.stringWidth(month);
            g2d.drawString(month, x + (barWidth - monthWidth) / 2, margin + chartHeight + 12);

            if (value > 0) {
                String valueStr = String.valueOf(value);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                fm = g2d.getFontMetrics();
                int valueWidth = fm.stringWidth(valueStr);

                int valueY = Math.max(margin + 10, y - 10);
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRoundRect(x + (barWidth - valueWidth) / 2 - 2, valueY - fm.getAscent(),
                        valueWidth + 4, fm.getHeight(), 3, 3);

                g2d.setColor(valueColor);
                g2d.drawString(valueStr, x + (barWidth - valueWidth) / 2, valueY);
            }

            i++;
        }

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 8)); // Smaller font for y-axis labels
        int tickCount = Math.min(5, maxValue);
        for (int j = 0; j <= tickCount; j++) {
            int y = margin + chartHeight - (j * chartHeight / tickCount);

            g2d.setColor(new Color(230, 230, 230));
            g2d.drawLine(margin, y, margin + chartWidth, y);

            g2d.setColor(new Color(100, 100, 100));
            int tickValue = (int) ((double) j / tickCount * maxValue);
            String tickLabel = String.valueOf(tickValue);
            g2d.drawString(tickLabel, margin - 25, y + 4);
        }
    }
    public void updateData(Map<String, Integer> newData, String newTitle) {
        this.monthlyData = new LinkedHashMap<>(newData);
        this.chartTitle = newTitle;
        repaint();
    }
}
