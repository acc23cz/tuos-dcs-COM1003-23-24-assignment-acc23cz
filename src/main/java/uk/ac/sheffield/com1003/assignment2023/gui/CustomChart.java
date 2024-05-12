package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractSongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractCustomChart;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.CustomChartAxisValues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomChart class represents a custom chart for displaying song data.
 * It extends the AbstractCustomChart class and implements the updateCustomChartContents method.
 */
public class CustomChart extends AbstractCustomChart {

    /**
     * Constructor for the CustomChart class.
     *
     * @param songCatalog             the song catalog
     * @param filteredSongEntriesList the list of filtered song entries
     */
    public CustomChart(AbstractSongCatalog songCatalog, List<SongEntry> filteredSongEntriesList) {
        super(songCatalog, filteredSongEntriesList);
    }

    /**
     * Updates the custom chart axes values with the minimum, maximum,
     * and average values of the specified property for the filtered song entries list.
     *
     * @param filteredSongEntriesList the list of filtered song entries
     */
    @Override
    public void updateCustomChartContents(List<SongEntry> filteredSongEntriesList) {
        if (filteredSongEntriesList.isEmpty()) {
            customChartAxesValues.clear();
            return;
        }

        Map<SongProperty, Double> minValues = new HashMap<>();
        Map<SongProperty, Double> maxValues = new HashMap<>();
        Map<SongProperty, Double> averageValues = new HashMap<>();

        for (SongProperty property : SongProperty.values()) {
            double maximumValue = this.songCatalog.getMaximumValue(property, filteredSongEntriesList);
            maxValues.put(property, maximumValue);
            double minimumValue = this.songCatalog.getMinimumValue(property, filteredSongEntriesList);
            minValues.put(property, minimumValue);
            double averageValue = this.songCatalog.getAverageValue(property, filteredSongEntriesList);
            averageValues.put(property, averageValue);
        }

        customChartAxesValues.clear();
        for (SongProperty property : SongProperty.values()) {
            customChartAxesValues.put(
                    property,
                    new CustomChartAxisValues(
                            minValues.get(property),
                            maxValues.get(property),
                            averageValues.get(property)
                    )
            );
        }
    }

    /**
     * Returns the custom chart axes values.
     *
     * @return the custom chart axes values.
     */
    @Override
    public Map<SongProperty, CustomChartAxisValues> getCustomChartAxesValues() {
        return customChartAxesValues;
    }
}
