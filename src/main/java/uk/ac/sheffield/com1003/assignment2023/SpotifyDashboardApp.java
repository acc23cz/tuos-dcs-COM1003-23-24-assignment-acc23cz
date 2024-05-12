package uk.ac.sheffield.com1003.assignment2023;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.*;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.AbstractSpotifyDashboardPanel;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.gui.SpotifyDashboard;
import uk.ac.sheffield.com1003.assignment2023.gui.SpotifyDashboardPanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class used to run the assignment's GUI.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 * <p>
 * Copyright (c) University of Sheffield 2023
 */
public class SpotifyDashboardApp {
    private final AbstractSongCatalog songCatalog;
    private final List<Query> listOfQueries;

    public SpotifyDashboardApp(String songFile, String queryFile) {
        AbstractSongCatalog songCatalog = null;
        List<Query> listOfQueries = null;
        try {
            songCatalog = new SongCatalog(songFile);

            List<String> queryTokens = new ArrayList<>(AbstractQueryParser.readQueryTokensFromFile(queryFile));
            try {
                listOfQueries = new ArrayList<>(new QueryParser().buildQueries(queryTokens));
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            }
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e);
            System.exit(-1);
        }
        this.songCatalog = songCatalog;
        this.listOfQueries = listOfQueries;

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{
                    "./src/main/resources/spotify_songs.tsv",
                    "./src/main/resources/queries.txt"
            };
        }
        SpotifyDashboardApp spotifyDashboardApp = new SpotifyDashboardApp(args[0], args[1]);
        spotifyDashboardApp.startCLI();
        spotifyDashboardApp.startGUI();
    }

    public void startCLI() {
        // Basic song catalogue information
        printQuestionAnswers();

        // Queries
        printNumberQueries();
        executeQueries();
    }

    private void executeQueries() {
        System.out.println("Executing queries...");

        for (Query query : listOfQueries) {
            System.out.println(query.toString() + ":");
            List<SongEntry> queryResults = query.executeQuery(songCatalog);
            printSongEntries(queryResults);
            System.out.println();
        }
    }

    private void printNumberQueries() {
        if (listOfQueries != null) {
            System.out.println("Total number of queries: " + listOfQueries.size());
        } else {
            System.out.println("No queries loaded.");
        }
    }

    private void printQuestionAnswers() {
        List<SongEntry> songs = songCatalog.getSongEntriesList();
        int totalSongs = songs.size();

        double averageDurationOfSongs = this.songCatalog.getAverageDurationOfSongs();
        double averageDurationOfTempo = this.songCatalog.getAverageDurationOfTempo();
        double maximumValue = this.songCatalog.getMaximumValue(SongProperty.LOUDNESS,
                this.songCatalog.getSongEntriesList());
        double minimumValue = this.songCatalog.getMinimumValue(SongProperty.TEMPO,
                this.songCatalog.getSongEntriesList());

        if (totalSongs > 0) {
            printNumberUniqueSongs();
            printNumberUniqueArtists();
            System.out.println("The average duration of a song in the dataset is: " + String.format("%.2f", averageDurationOfSongs));
            System.out.println("The average tempo of a song in the dataset is: " + String.format("%.2f", averageDurationOfTempo));
            System.out.println("The maximum loudness in the dataset is: " + String.format("%.3f", maximumValue));
            System.out.println("The minimum tempo in the dataset is: " + String.format("%.3f", minimumValue));
        } else {
            System.out.println("No songs available for statistics.");
        }
    }

    private void printNumberUniqueArtists() {
        int numberOfUniqueArtists = songCatalog.getNumberOfUniqueArtists();
        System.out.println("The total number of unique artists in the dataset is: " + numberOfUniqueArtists);
    }

    private void printNumberUniqueSongs() {
        int numberOfUniqueSongs = songCatalog.getNumberOfUniqueSongs();
        System.out.println("The total number of unique songs in the dataset is: " + numberOfUniqueSongs);
    }

    private void printFirstFiveSongEntries() {
        List<SongEntry> firstFiveSongEntries = this.songCatalog.getFirstFiveSongEntries();
        System.out.println(firstFiveSongEntries);
    }

    private void printSongEntries(List<SongEntry> songEntriesList) {
        // to avoid long prints to console, list is limited to 5
//        int count = 1;
//        for (SongEntry song : songEntriesList) {
//            System.out.println(song);
//            if (++count > 5) {
//                break;
//            }
//        }
        printFirstFiveSongEntries();
    }

    public void startGUI() {
        // Start GUI
        AbstractSpotifyDashboardPanel spotifyDashboardPanel = new SpotifyDashboardPanel(songCatalog);
        SpotifyDashboard songDashboard = new SpotifyDashboard(spotifyDashboardPanel);
        songDashboard.setVisible(true);
    }
}
