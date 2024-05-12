package uk.ac.sheffield.com1003.assignment2023.gui.combinedchart;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract class representing a combined chart.
 */
public abstract class AbstractCombinedChart {
    protected Graphics2D g2d;
    protected int centerX, centerY, radius;
    protected Map<SongProperty, CustomChartAxisValues> data;
    protected int numProperties = 0;
    protected List<Double> maxValueList = new ArrayList<>();
    protected List<Double> minValueList = new ArrayList<>();
    protected List<Double> averageValueList = new ArrayList<>();
    protected List<Double> valueRangeList = new ArrayList<>();
    protected List<Double> baselineValueList = new ArrayList<>();
    protected double maxValueRange;
    protected boolean isMinCheckBoxSelected;
    protected boolean isMaxCheckBoxSelected;
    protected boolean isAverageCheckBoxSelected;

    /**
     * Constructor for the AbstractCombinedChart class.
     *
     * @param g2d                       The Graphics2D object used for drawing.
     * @param centerX                   The x-coordinate of the center of the chart.
     * @param centerY                   The y-coordinate of the center of the chart.
     * @param radius                    The radius of the chart.
     * @param data                      The data for the chart.
     * @param isMinCheckBoxSelected     the min checkbox is selected or not
     * @param isMaxCheckBoxSelected     the max checkbox is selected or not
     * @param isAverageCheckBoxSelected the average checkbox is selected or not
     */
    protected AbstractCombinedChart(Graphics2D g2d,
                                    int centerX, int centerY, int radius,
                                    Map<SongProperty, CustomChartAxisValues> data,
                                    boolean isMinCheckBoxSelected,
                                    boolean isMaxCheckBoxSelected,
                                    boolean isAverageCheckBoxSelected) {
        this.g2d = g2d;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.data = data;
        this.isMinCheckBoxSelected = isMinCheckBoxSelected;
        this.isMaxCheckBoxSelected = isMaxCheckBoxSelected;
        this.isAverageCheckBoxSelected = isAverageCheckBoxSelected;
        this.setChartData();
    }

    /**
     * Set the chart data.
     */
    protected void setChartData() {
        numProperties = data.size();

        double totalMaxValue = 0;
        double totalMinValue = 0;

        for (Map.Entry<SongProperty, CustomChartAxisValues> entry : data.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            CustomChartAxisValues axisValues = entry.getValue();
            double max = axisValues.getMax();
            double min = axisValues.getMin();
            double average = axisValues.getAverage();

            totalMaxValue = Math.max(totalMaxValue, max);
            totalMinValue = Math.min(totalMinValue, min);

            maxValueList.add(max);
            minValueList.add(min);
            averageValueList.add(average);
            valueRangeList.add(max - min);
            baselineValueList.add(min);
        }
        maxValueRange = totalMaxValue;
    }

    /**
     * Draw the chart.
     */
    public abstract void drawChart();

    /**
     * Draw a center point on the chart.
     *
     * @param g2d     graphics object
     * @param centerX center x coordinate
     * @param centerY center y coordinate
     */
    public static void drawCenterPoint(Graphics2D g2d, int centerX, int centerY, Color color) {
        g2d.setColor(color);
        int diameter = 8;
        int x = centerX - diameter / 2;
        int y = centerY - diameter / 2;
        g2d.fillOval(x, y, diameter, diameter);
    }
}
