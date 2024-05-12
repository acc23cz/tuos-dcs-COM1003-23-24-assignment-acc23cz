package uk.ac.sheffield.com1003.assignment2023.gui.combinedchart;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Custom class representing a circular bar chart.
 */
public final class CircularBarChart extends AbstractCombinedChart {

    /**
     * Constructor for the AbstractCombinedChart class.
     *
     * @param g2d                       The Graphics2D object used for drawing.
     * @param centerX                   The x-coordinate of the center of the chart.
     * @param centerY                   The y-coordinate of the center of the chart.
     * @param radius                    The radius of the chart.
     * @param circularBarChartData      The data for the chart.
     * @param isMinCheckBoxSelected     the min checkbox is selected or not
     * @param isMaxCheckBoxSelected     the max checkbox is selected or not
     * @param isAverageCheckBoxSelected the average checkbox is selected or not
     */
    public CircularBarChart(Graphics2D g2d,
                            int centerX, int centerY, int radius,
                            Map<SongProperty, CustomChartAxisValues> circularBarChartData,
                            boolean isMinCheckBoxSelected,
                            boolean isMaxCheckBoxSelected,
                            boolean isAverageCheckBoxSelected) {
        super(g2d, centerX, centerY, radius,
                circularBarChartData,
                isMinCheckBoxSelected,
                isMaxCheckBoxSelected,
                isAverageCheckBoxSelected);
    }

    @Override
    public void drawChart() {
        double angleStep = Math.PI / numProperties;
        double startAngle = Math.PI / 2;

        this.drawRadialPieChart(g2d,
                centerX, centerY, radius,
                startAngle, angleStep * numProperties,
                Color.BLACK, 0.5f);

        AtomicInteger i = new AtomicInteger();
        data.forEach((property, values) -> {
            // Draw the bar chart line.
            this.drawRadialPieChartLine(g2d,
                    centerX, centerY, radius,
                    i, angleStep, Color.BLACK, 0.5f);

            // Draw the bar chart label.
            this.drawRadialPieChartLabel(g2d,
                    centerX, centerY, radius,
                    property.toString().toLowerCase(),
                    startAngle + angleStep * i.get(), angleStep, Color.BLACK);

            // Draw the radial pie bar chart.
            Double valueRange = this.valueRangeList.get(i.get());
            Double baselineValue = this.baselineValueList.get(i.get());
            if (this.isMaxCheckBoxSelected) {
                Double maxValue = this.maxValueList.get(i.get());
                double maxPercentage = (maxValue - baselineValue) / valueRange;
                int maxRadius = (int) (((double) radius) * maxPercentage);
                this.drawRadialPieChart(g2d,
                        centerX, centerY, maxRadius,
                        startAngle + angleStep * i.get(), angleStep,
                        Color.GREEN, 2.0f);
            }

            if (this.isMinCheckBoxSelected) {
                Double minValue = this.minValueList.get(i.get());
                double minPercentage = (minValue - baselineValue) / valueRange;
                int minRadius = (int) (((double) radius) * minPercentage);
                this.drawRadialPieChart(g2d,
                        centerX, centerY, minRadius,
                        startAngle + angleStep * i.get(), angleStep,
                        Color.GREEN, 2.0f);
            }

            if (this.isAverageCheckBoxSelected) {
                Double averageValue = this.averageValueList.get(i.get());
                double averagePercentage = (averageValue - baselineValue) / valueRange;
                int averageRadius = (int) (((double) radius) * averagePercentage);
                this.drawRadialPieChart(g2d,
                        centerX, centerY, averageRadius,
                        startAngle + angleStep * i.get(), angleStep,
                        Color.BLUE, 2.0f);
            }
            i.incrementAndGet();
        });
    }

    /**
     * Draw a radial pie chart line.
     *
     * @param g2d       graphics object
     * @param centerX   center x coordinate
     * @param centerY   center y coordinate
     * @param radius    radius of the pie chart
     * @param i         index of the pie chart
     * @param angleStep angle step of the pie chart
     * @param color     color of the pie chart
     * @return angle of the pie chart
     */
    private double drawRadialPieChartLine(Graphics2D g2d,
                                          int centerX, int centerY, int radius,
                                          AtomicInteger i, double angleStep,
                                          Color color, float strokeWidth) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth));
        int index = i.get();
        if (index == 0) {
            g2d.drawLine(centerX, centerY, centerX, centerY - radius);
        }
        double angle = index * angleStep;
        int x1 = (int) (centerX - radius * Math.sin(angle));
        int y1 = (int) (centerY + radius * Math.cos(angle));
        g2d.drawLine(centerX, centerY, x1, y1);
        return angle;
    }

    /**
     * Draw a radial pie chart label.
     *
     * @param g2d       graphics object
     * @param centerX   center x coordinate
     * @param centerY   center y coordinate
     * @param radius    radius of the pie chart
     * @param labelName label name of the pie chart
     * @param angle     angle of the pie chart
     * @param angleStep angle step of the pie chart
     * @param color     color of the pie chart
     */
    private void drawRadialPieChartLabel(Graphics2D g2d,
                                         int centerX, int centerY, int radius,
                                         String labelName,
                                         double angle, double angleStep, Color color) {
        double angleMiddle = (angle + angleStep / 2) % (2 * Math.PI);
        int labelX, labelY;
        int labelMargin = 10;
        if (angleMiddle <= Math.PI) {
            labelX = (int) (centerX + (radius) * Math.cos(angleMiddle));
            labelY = (int) (centerY - (radius) * Math.sin(angleMiddle));
        } else {
            angleMiddle = angleMiddle - Math.PI;
            labelX = (int) (centerX - (radius - radius / 2 + labelMargin) * Math.sin(angleMiddle));
            labelY = (int) (centerY + (radius + radius / 2) * Math.cos(angleMiddle));
        }
        int labelWidth = g2d.getFontMetrics().stringWidth(labelName) + 2 * labelMargin;
        int labelHeight = g2d.getFontMetrics().getHeight() + labelMargin;
        g2d.setColor(Color.CYAN);
        g2d.fillRect(labelX, labelY, labelWidth, labelHeight);

        g2d.setColor(color);
        g2d.drawString(labelName, labelX + labelMargin, labelY + labelHeight - labelMargin);
    }

    /**
     * Draw a radial pie chart.
     *
     * @param g2d        graphics object
     * @param centerX    center x coordinate
     * @param centerY    center y coordinate
     * @param radius     radius of the pie chart
     * @param startAngle start angle of the pie chart
     * @param angleStep  angle step of the pie chart
     * @param color      color of the pie chart
     */
    private void drawRadialPieChart(Graphics2D g2d,
                                    int centerX, int centerY, int radius,
                                    double startAngle, double angleStep, Color color, float width) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(width));
        double arcX = centerX - radius;
        double arcY = centerY - radius;
        double diameter = radius * 2;
        Arc2D.Double arc = new Arc2D.Double(arcX, arcY,
                diameter, diameter,
                Math.toDegrees(startAngle), Math.toDegrees(angleStep),
                Arc2D.PIE);
        g2d.draw(arc);
    }
}
