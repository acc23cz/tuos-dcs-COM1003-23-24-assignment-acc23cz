package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractSongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;

import java.util.*;

public abstract class AbstractCustomChart {

    protected Map<SongProperty, CustomChartAxisValues> customChartAxesValues;

    protected final AbstractSongCatalog songCatalog;

    protected List<SongEntry> filteredSongEntries;

    public AbstractCustomChart(AbstractSongCatalog songCatalog,
                               List<SongEntry> filteredSongEntriesList) {
        this.songCatalog = Objects.requireNonNull(songCatalog, "songCatalog cannot be null");
        this.filteredSongEntries = Objects.requireNonNull(filteredSongEntriesList, "filteredSongEntriesList cannot be null");
        this.customChartAxesValues = new LinkedHashMap<>();
        updateCustomChartContents(filteredSongEntriesList);
    }

    public abstract void updateCustomChartContents(List<SongEntry> filteredSongEntriesList);

    public abstract Map<SongProperty, CustomChartAxisValues> getCustomChartAxesValues();

}
