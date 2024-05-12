package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

import javax.swing.*;
import java.util.Objects;

/**
 * This is an Abstract class used for displaying a Custom Chart Panel.
 * <p>
 * Should be implemented as uk.ac.sheffield.assignment2023.gui.CustomChartPanel.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 * <p>
 * Copyright (c) University of Sheffield 2023
 */
public abstract class AbstractCustomChartPanel extends JPanel {

    private final AbstractSpotifyDashboardPanel parentPanel;
    private final AbstractCustomChart customChart;

    public AbstractCustomChartPanel(AbstractSpotifyDashboardPanel parentPanel, AbstractCustomChart customChart) {
        super();
        this.parentPanel = Objects.requireNonNull(parentPanel, "Parent panel cannot be null");
        this.customChart = Objects.requireNonNull(customChart, "Custom chart cannot be null");
    }

    public AbstractSpotifyDashboardPanel getParentPanel() {
        return parentPanel;
    }

    public AbstractCustomChart getCustomChart() {
        return customChart;
    }
}
