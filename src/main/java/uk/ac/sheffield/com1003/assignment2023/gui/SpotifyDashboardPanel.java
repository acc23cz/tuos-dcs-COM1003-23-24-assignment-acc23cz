package uk.ac.sheffield.com1003.assignment2023.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.*;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractSpotifyDashboardPanel;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpotifyDashboardPanel extends AbstractSpotifyDashboardPanel {
    private static final String SEPARATE = "\t";

    public SpotifyDashboardPanel(AbstractSongCatalog songCatalog) {
        super(songCatalog);
    }

    @Override
    public void executeQuery() {
        Query query = new Query(this.subQueryList);
        List<SongEntry> filteredSongEntriesList = query.executeQuery(this.songCatalog);

        String selectedArtistName = comboBoxArtistNames.getModel().getSelectedItem().toString();
        if (!selectedArtistName.isEmpty()) {
            filteredSongEntriesList = this.songCatalog.getSongEntriesList(
                    filteredSongEntriesList,
                    SongDetail.ARTIST, selectedArtistName);
        }
        String selectedAlbum = comboBoxAlbums.getModel().getSelectedItem().toString();
        if (!selectedAlbum.isEmpty()) {
            filteredSongEntriesList = this.songCatalog.getSongEntriesList(
                    filteredSongEntriesList,
                    SongDetail.ALBUM_NAME, selectedAlbum);
        }
        String selectedSong = comboBoxSongs.getModel().getSelectedItem().toString();
        if (!selectedSong.isEmpty()) {
            filteredSongEntriesList = this.songCatalog.getSongEntriesList(
                    filteredSongEntriesList,
                    SongDetail.NAME, selectedSong);
        }
        this.filteredSongEntriesList = filteredSongEntriesList;
        this.updateStatistics();
    }

    @Override
    public void clearFilters() {
        this.subQueryList.clear();
        this.setPropertyValueTextField(this.propertyQueriesTextArea, this.subQueryList);
        this.executeQuery();
    }

    @Override
    public void addFilter() {
        String valueString = this.value.getText();
        if (valueString.isEmpty()) {
            return;
        }
        String queryPropertyName = this.comboQueryProperties.getModel().getSelectedItem().toString();
        SongProperty songProperty = SongProperty.fromPropertyName(queryPropertyName);
        String operator = this.comboOperators.getModel().getSelectedItem().toString();
        double valueDouble = Double.parseDouble(valueString);
        this.subQueryList.removeIf(subQuery ->
                songProperty.equals(subQuery.getSongProperty())
                        && operator.equals(subQuery.getOperator())
        );
        this.subQueryList.add(new SubQuery(songProperty, operator, valueDouble));
        this.setPropertyValueTextField(this.propertyQueriesTextArea, this.subQueryList);
        this.executeQuery();
    }

    private void setPropertyValueTextField(JTextArea propertyQueriesTextArea, List<SubQuery> conditionList) {
        if (conditionList.isEmpty()) {
            propertyQueriesTextArea.setText("");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (SubQuery subQuery : conditionList) {
            stringBuilder.append(subQuery.getSongProperty().toString())
                    .append(subQuery.getOperator())
                    .append(subQuery.getValue())
                    .append(";");
        }
        propertyQueriesTextArea.setText(stringBuilder.toString());
    }

    @Override
    public void populateComboBoxes() {
        // get artists, albums, songs
        List<String> artists = songCatalog.getArtists();
        List<String> albums = songCatalog.getAlbums();
        List<String> songs = songCatalog.getSongs();
        songs = songs.stream()
                .distinct()
                .sorted()
                .map(item -> item.replaceAll("\"", ""))
                .collect(Collectors.toList());

        populateComboBox(comboBoxArtistNames, artists);
        populateComboBox(comboBoxAlbums, albums);
        populateComboBox(comboBoxSongs, songs);
    }

    private void populateComboBox(JComboBox<String> comboBox, List<String> items) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("");
        items.forEach(model::addElement);
        comboBox.setModel(model);
    }

    @Override
    public void addListeners() {
        // Ensure the component can gain focus to receive key events
        this.setFocusable(true);

        // add combo box listener
        comboBoxArtistNames.addItemListener(e -> executeQuery());
        comboBoxAlbums.addItemListener(e -> executeQuery());
        comboBoxSongs.addItemListener(e -> executeQuery());

        // add checkbox listener
        this.minCheckBox.addChangeListener(e -> updateStatistics());
        this.maxCheckBox.addChangeListener(e -> updateStatistics());
        this.averageCheckBox.addChangeListener(e -> updateStatistics());
    }

    @Override
    public void updateStatistics() {
        super.updateUI();

        // update the statistics and entries
        this.setSongCatalogStatisticsTextArea(songCatalogStatisticsTextArea, filteredSongEntriesList);
        this.setSongEntriesList(songEntriesTextArea, filteredSongEntriesList);
    }

    private void setSongCatalogStatisticsTextArea(JTextArea songCatalogStatisticsTextArea,
                                                  List<SongEntry> filteredSongEntriesList) {
        if (filteredSongEntriesList.isEmpty()) {
            songCatalogStatisticsTextArea.setText("no data");
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

        StringBuilder sb = new StringBuilder();
        sb.append(SEPARATE);
        for (SongProperty property : SongProperty.values()) {
            sb.append(property.getName())
                    .append(SEPARATE);
        }
        sb.append("\n");

        sb.append("Minimum:");
        sb.append(SEPARATE);
        for (SongProperty property : SongProperty.values()) {
            Double min = minValues.get(property);
            sb.append(String.format("%.3f", min)).append(SEPARATE);
        }
        sb.append("\n");

        sb.append("Maximum:");
        sb.append(SEPARATE);
        for (SongProperty property : SongProperty.values()) {
            Double max = maxValues.get(property);
            sb.append(String.format("%.3f", max)).append(SEPARATE);
        }
        sb.append("\n");

        sb.append("Average:");
        sb.append(SEPARATE);
        for (SongProperty property : SongProperty.values()) {
            Double average = averageValues.get(property);
            sb.append(String.format("%.2f", average)).append(SEPARATE);
        }
        sb.append("\n");

        songCatalogStatisticsTextArea.setText(sb.toString());
    }

    private void setSongEntriesList(JTextArea songEntriesTextArea, List<SongEntry> filteredSongEntriesList) {
        if (filteredSongEntriesList.isEmpty()) {
            songEntriesTextArea.setText("no data");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("ID");
        for (SongDetail detail : SongDetail.values()) {
            sb.append(SEPARATE);
            sb.append(detail.getName());
        }
        for (SongProperty property : SongProperty.values()) {
            sb.append(property.getName());
        }
        sb.append(SEPARATE);
        sb.append("\n");

        for (SongEntry song : filteredSongEntriesList) {
            int id = song.getId();
            sb.append(id);

            for (SongDetail detail : SongDetail.values()) {
                sb.append(SEPARATE);
                sb.append(song.getSongDetail(detail));
            }

            for (SongProperty property : SongProperty.values()) {
                sb.append(SEPARATE);
                sb.append(song.getSongProperty(property));
            }
            sb.append(SEPARATE);
            sb.append("\n");
        }

        songEntriesTextArea.setText(sb.toString());
    }

    @Override
    public boolean isMinCheckBoxSelected() {
        return this.minCheckBox.isSelected();
    }

    @Override
    public boolean isMaxCheckBoxSelected() {
        return this.maxCheckBox.isSelected();
    }

    @Override
    public boolean isAverageCheckBoxSelected() {
        return this.averageCheckBox.isSelected();
    }
}
