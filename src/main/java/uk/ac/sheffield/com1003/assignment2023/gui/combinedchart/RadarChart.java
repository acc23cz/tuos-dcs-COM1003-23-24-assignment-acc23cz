package uk.ac.sheffield.com1003.assignment2023.gui.combinedchart;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Custom chart class for drawing radar charts.
 */
public final class RadarChart extends AbstractCombinedChart {

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
    public RadarChart(Graphics2D g2d,
                      int centerX, int centerY, int radius,
                      Map<SongProperty, CustomChartAxisValues> data,
                      boolean isMinCheckBoxSelected,
                      boolean isMaxCheckBoxSelected,
                      boolean isAverageCheckBoxSelected) {
        super(g2d,
                centerX, centerY, radius,
                data,
                isMinCheckBoxSelected,
                isMaxCheckBoxSelected,
                isAverageCheckBoxSelected);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawChart() {
        double angleStep = Math.PI / (numProperties - 1);
        double startAngle = -Math.PI / 2;

        drawAxes(g2d,
                centerX, centerY, radius,
                startAngle, angleStep, Color.BLACK);

        if (isMaxCheckBoxSelected && !maxValueList.isEmpty()) {
            drawPolygonForValues(g2d,
                    centerX, centerY, radius,
                    startAngle, angleStep,
                    Color.GREEN,
                    maxValueList);
        }
        if (isMinCheckBoxSelected && !minValueList.isEmpty()) {
            drawPolygonForValues(g2d,
                    centerX, centerY, radius,
                    startAngle, angleStep,
                    Color.RED,
                    minValueList);
        }
        if (isAverageCheckBoxSelected && !averageValueList.isEmpty()) {
            drawPolygonForValues(g2d,
                    centerX, centerY, radius,
                    startAngle, angleStep,
                    Color.BLUE,
                    averageValueList);
        }
    }

    /**
     * Draws a polygon for the given values.
     *
     * @param g2d        The Graphics2D object used for drawing.
     * @param centerX    The x-coordinate of the center of the chart.
     * @param centerY    The y-coordinate of the center of the chart.
     * @param radius     The radius of the chart.
     * @param startAngle The starting angle for the polygon.
     * @param angleStep  The angle step for the polygon.
     * @param color      The color of the polygon.
     */
    private void drawAxes(Graphics2D g2d,
                          int centerX, int centerY, int radius,
                          double startAngle, double angleStep, Color color) {
        Polygon polygon = new Polygon();

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(0.5f));
        AtomicInteger i = new AtomicInteger();
        this.data.forEach((property, customChartAxisValues) -> {
            double angle = startAngle + i.get() * angleStep;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            g2d.drawLine(centerX, centerY, x, y);

            int labelX = (int) (centerX + (radius + 20) * Math.cos(angle));
            int labelY = (int) (centerY + (radius + 20) * Math.sin(angle));

            String labelName = property.toString().toLowerCase();

            int labelMargin = 10;
            int labelWidth = g2d.getFontMetrics().stringWidth(labelName) + 2 * labelMargin;
            int labelHeight = g2d.getFontMetrics().getHeight() + labelMargin;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(labelX - labelMargin,
                    labelY - g2d.getFontMetrics().getHeight() / 2 - labelMargin,
                    labelWidth, labelHeight);

            g2d.setColor(color);
            if (angle > Math.PI / 2 && angle < Math.PI * 3 / 2) {
                g2d.drawString(labelName, labelX - g2d.getFontMetrics().stringWidth(labelName), labelY);
            } else {
                g2d.drawString(labelName, labelX, labelY);
            }
            polygon.addPoint(x, y);
            i.getAndIncrement();
        });
        g2d.drawPolygon(polygon);
    }

    /**
     * Draws a polygon for the given values.
     *
     * @param g           The Graphics object used for drawing.
     * @param centerX     The x-coordinate of the center of the chart.
     * @param centerY     The y-coordinate of the center of the chart.
     * @param totalRadius The total radius of the chart.
     * @param startAngle  The starting angle for the polygon.
     * @param angleStep   The angle step for the polygon.
     * @param color       The color of the polygon.
     * @param values      The list of values to be plotted.
     */
    private void drawPolygonForValues(Graphics2D g,
                                      int centerX, int centerY, int totalRadius,
                                      double startAngle, double angleStep,
                                      Color color,
                                      List<Double> values) {
        Polygon polygon = new Polygon();
        g.setColor(color);
        for (int i = 0; i < numProperties; i++) {
            double angle = startAngle + angleStep * i;
            Double value = values.get(i);
            Double baselineValue = baselineValueList.get(i);
            Double valueRange = valueRangeList.get(i);
            double percentage = (value - baselineValue) / valueRange;
            int radius = (int) (totalRadius * percentage);
            int newX = (int) (centerX + radius * Math.cos(angle));
            int newY = (int) (centerY + radius * Math.sin(angle));
            g.setStroke(new BasicStroke(2));
            g.drawOval(newX - 2, newY - 2, 4, 4);
            polygon.addPoint(newX, newY);
        }
        g.drawPolygon(polygon);
    }
}
