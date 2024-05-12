package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractSongCatalog;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongEntry;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SubQuery;
import uk.ac.sheffield.com1003.assignment2023.gui.CustomChart;
import uk.ac.sheffield.com1003.assignment2023.gui.CustomChartPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractSpotifyDashboardPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected final AbstractSongCatalog songCatalog;
    public List<SongEntry> filteredSongEntriesList;
    protected List<SubQuery> subQueryList = new ArrayList<>();

    // Component initialization split into separate methods for better modularity
    protected JComboBox<String> comboBoxArtistNames = createComboBox();
    protected JComboBox<String> comboBoxAlbums = createComboBox();
    protected JComboBox<String> comboBoxSongs = createComboBox();
    protected JComboBox<String> comboQueryProperties = createPropertiesComboBox();
    protected JComboBox<String> comboOperators = createOperatorsComboBox();
    protected JTextField value = new JTextField(5);
    protected JTextArea propertyQueriesTextArea = createPropertyQueriesTextArea();
    protected JTextArea songCatalogStatisticsTextArea = createSongCatalogStatisticsTextArea();
    protected JTextArea songEntriesTextArea = createSongEntriesTextArea();
    protected JCheckBox minCheckBox = new JCheckBox("Minimum", true);
    protected JCheckBox maxCheckBox = new JCheckBox("Maximum", true);
    protected JCheckBox averageCheckBox = new JCheckBox("Average", true);

    // Abstract methods are defined to be implemented by subclasses
    public abstract void addFilter();
    public abstract void clearFilters();
    public abstract void executeQuery();
    public abstract void populateComboBoxes();
    public abstract void addListeners();
    public abstract void updateStatistics();
    public abstract boolean isMinCheckBoxSelected();
    public abstract boolean isMaxCheckBoxSelected();
    public abstract boolean isAverageCheckBoxSelected();

    public AbstractSpotifyDashboardPanel(AbstractSongCatalog songCatalog) {
        this.songCatalog = songCatalog;
        this.filteredSongEntriesList = songCatalog.getSongEntriesList();
        initializeUI();
        populateComboBoxes();
        updateStatistics();
        addListeners();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        add(createQueryPanel(), BorderLayout.NORTH);
        add(createCustomChartPanel(), BorderLayout.CENTER);
        add(createSongCatalogPanel(), BorderLayout.EAST);
    }

    private JPanel createQueryPanel() {
        JPanel queryPanel = new JPanel(new GridLayout(4, 1));
        queryPanel.add(createTypeSelectorPanel());
        queryPanel.add(createFilterBuilderPanel());
        queryPanel.add(new JLabel("List of filters by property (or subqueries):", SwingConstants.LEFT));
        queryPanel.add(new JScrollPane(propertyQueriesTextArea));
        return queryPanel;
    }

    private JTextArea createPropertyQueriesTextArea() {
        return new JTextArea(1, 50);
    }

    private JPanel createTypeSelectorPanel() {
        JPanel typeSelectorPanel = new JPanel(new FlowLayout());
        typeSelectorPanel.add(new JLabel("Filter by :", SwingConstants.LEFT));
        typeSelectorPanel.add(new JLabel("Artist:", SwingConstants.LEFT));
        typeSelectorPanel.add(comboBoxArtistNames);
        typeSelectorPanel.add(new JLabel("Album:", SwingConstants.LEFT));
        typeSelectorPanel.add(comboBoxAlbums);
        typeSelectorPanel.add(new JLabel("Song:", SwingConstants.LEFT));
        typeSelectorPanel.add(comboBoxSongs);
        return typeSelectorPanel;
    }

    private JPanel createFilterBuilderPanel() {
        JPanel filterBuilderPanel = new JPanel(new FlowLayout());
        filterBuilderPanel.add(new JLabel("Filter by property:", SwingConstants.LEFT));
        filterBuilderPanel.add(comboQueryProperties);
        filterBuilderPanel.add(new JLabel("Operator:", SwingConstants.LEFT));
        filterBuilderPanel.add(comboOperators);
        filterBuilderPanel.add(new JLabel("Value:", SwingConstants.LEFT));
        filterBuilderPanel.add(value);

        JButton addByPropertyFilter = new JButton("Add by Property Filter");
        addByPropertyFilter.addActionListener(e -> addFilter());
        filterBuilderPanel.add(addByPropertyFilter);

        JButton clearAllByPropertyFilters = new JButton("Clear All by Property Filters");
        clearAllByPropertyFilters.addActionListener(e -> clearFilters());
        filterBuilderPanel.add(clearAllByPropertyFilters);
        return filterBuilderPanel;
    }

    private JPanel createCustomChartPanel() {
        JPanel customChartContainer = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("CUSTOM CHART");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        customChartContainer.add(titleLabel, BorderLayout.PAGE_START);

        CustomChart customChart = new CustomChart(songCatalog, filteredSongEntriesList);
        CustomChartPanel chartPanel = new CustomChartPanel(this, customChart);
        customChartContainer.add(chartPanel, BorderLayout.CENTER);
        customChartContainer.add(createControlCustomChartContainer(), BorderLayout.SOUTH);
        return customChartContainer;
    }

    private JPanel createControlCustomChartContainer() {
        JPanel controlCustomChartContainer = new JPanel();
        controlCustomChartContainer.add(minCheckBox);
        controlCustomChartContainer.add(maxCheckBox);
        controlCustomChartContainer.add(averageCheckBox);
        return controlCustomChartContainer;
    }

    private JPanel createSongCatalogPanel() {
        JPanel songCatalogPanel = new JPanel(new BorderLayout());
        songCatalogPanel.add(createStatisticsPanel(), BorderLayout.NORTH);
        songCatalogPanel.add(createSongEntriesPanel(), BorderLayout.CENTER);
        return songCatalogPanel;
    }

    private JPanel createStatisticsPanel() {
        JPanel statisticsPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("SONG CATALOG STATISTICS");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statisticsPanel.add(titleLabel, BorderLayout.PAGE_START);

        statisticsPanel.add(new JScrollPane(songCatalogStatisticsTextArea), BorderLayout.CENTER);
        return statisticsPanel;
    }

    private JTextArea createSongCatalogStatisticsTextArea() {
        return new JTextArea(10, 70);
    }

    private JPanel createSongEntriesPanel() {
        JPanel songEntriesPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("SONG ENTRIES");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        songEntriesPanel.add(titleLabel, BorderLayout.PAGE_START);

        songEntriesPanel.add(new JScrollPane(songEntriesTextArea), BorderLayout.CENTER);
        return songEntriesPanel;
    }

    private JTextArea createSongEntriesTextArea() {
        return new JTextArea(28, 70);
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(250, 30));
        return comboBox;
    }

    private JComboBox<String> createPropertiesComboBox() {
        return new JComboBox<>(new DefaultComboBoxModel<>(Arrays.stream(SongProperty.values())
                .map(SongProperty::getName).toArray(String[]::new)));
    }

    private JComboBox<String> createOperatorsComboBox() {
        String[] operators = { ">", ">=", "<", "<=", "==", "!=" };
        return new JComboBox<>(operators);
    }
}
