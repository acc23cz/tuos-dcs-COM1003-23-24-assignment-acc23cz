package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

import javax.swing.*;

/**
 * This class instantiates the GUI for the Spotify Dashboard.
 * It extends JFrame, meaning it is a top-level window with a title and a border.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 * Copyright (c) University of Sheffield 2023
 */
public class SpotifyDashboard extends JFrame {
    // Unique identifier for this Serializable class
    private static final long serialVersionUID = 1L;

    public SpotifyDashboard(AbstractSpotifyDashboardPanel panel) {
        if (panel == null) {
            throw new IllegalArgumentException("Panel cannot be null");
        }
        setTitle("Spotify Dashboard");
        add(panel);
        setDefaultSize();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Sets the default size of the frame. This size is used if the frame cannot be maximized.
     */
    private void setDefaultSize() {
        setSize(800, 600);  // Default size if not maximized
    }
}