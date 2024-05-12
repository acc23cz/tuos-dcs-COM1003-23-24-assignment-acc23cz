package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractCustomChartPanel;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractSpotifyDashboardPanel;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;
import uk.ac.sheffield.com1003.assignment2023.gui.combinedchart.AbstractCombinedChart;
import uk.ac.sheffield.com1003.assignment2023.gui.combinedchart.CircularBarChart;
import uk.ac.sheffield.com1003.assignment2023.gui.combinedchart.RadarChart;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomChartPanel class.
 */
public class CustomChartPanel extends AbstractCustomChartPanel {

    /**
     * Constructor for the CustomChartPanel class.
     *
     * @param parentPanel the parent panel
     * @param customChart the custom chart
     */
    public CustomChartPanel(AbstractSpotifyDashboardPanel parentPanel, CustomChart customChart) {
        super(parentPanel, customChart);
    }

    /**
     * Paint the custom chart.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateChart();
        drawCustomChart(g);
    }

    /**
     * Update the chart data.
     */
    private void updateChart() {
        List<SongEntry> filteredSongEntriesList = this.getParentPanel().filteredSongEntriesList;
        if (filteredSongEntriesList != null) {
            this.getCustomChart().updateCustomChartContents(filteredSongEntriesList);
        }
    }

    /**
     * Draw the custom chart.
     *
     * @param g the graphics object
     */
    private void drawCustomChart(Graphics g) {
        // get the data for the chart
        Map<SongProperty, CustomChartAxisValues> chartAxesValues = this.getCustomChart().getCustomChartAxesValues();
        if (chartAxesValues.values().isEmpty()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) / 2;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));

        boolean isMinCheckBoxSelected = this.getParentPanel().isMinCheckBoxSelected();
        boolean isMaxCheckBoxSelected = this.getParentPanel().isMaxCheckBoxSelected();
        boolean isAverageCheckBoxSelected = this.getParentPanel().isAverageCheckBoxSelected();

        // Draw the circular bar chart
        Map<SongProperty, CustomChartAxisValues> circularBarChartData = new LinkedHashMap<>();
        List<SongProperty> circularBarChartProperties = Arrays.asList(
                SongProperty.POPULARITY,
                SongProperty.TEMPO,
                SongProperty.LOUDNESS
        );
        for (SongProperty property : circularBarChartProperties) {
            circularBarChartData.put(property, chartAxesValues.get(property));
        }
        CircularBarChart circularBarChart = new CircularBarChart(g2d,
                centerX, centerY, radius,
                circularBarChartData,
                isMinCheckBoxSelected, isMaxCheckBoxSelected, isAverageCheckBoxSelected);
        circularBarChart.drawChart();

        // Draw the radar chart
        Map<SongProperty, CustomChartAxisValues> radarChartData = new LinkedHashMap<>();
        List<SongProperty> radarChartProperties = Arrays.asList(
                SongProperty.DANCEABILITY,
                SongProperty.ENERGY,
                SongProperty.SPEECHINESS,
                SongProperty.ACOUSTICNESS,
                SongProperty.INSTRUMENTALNESS,
                SongProperty.LIVENESS,
                SongProperty.VALENCE);
        for (SongProperty property : radarChartProperties) {
            radarChartData.put(property, chartAxesValues.get(property));
        }
        RadarChart radarChart = new RadarChart(g2d,
                centerX, centerY, radius,
                radarChartData,
                isMinCheckBoxSelected, isMaxCheckBoxSelected, isAverageCheckBoxSelected);
        radarChart.drawChart();

        // Draw a center point on the chart.
        AbstractCombinedChart.drawCenterPoint(g2d, centerX, centerY, Color.RED);
    }
}
