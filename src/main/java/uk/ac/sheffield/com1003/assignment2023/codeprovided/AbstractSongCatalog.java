package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provided basic reading functionalities of the dataset with song
 * entries.
 * <p>
 * This class is designed to be extended.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 * <p>
 * Copyright (c) University of Sheffield 2023
 */
public abstract class AbstractSongCatalog {

    protected final List<SongEntry> songEntriesList;

    /**
     * Constructor - reads datasets with song catalogue (song entries
     * and initialises the songEntries list.
     */
    public AbstractSongCatalog(String songFile)
            throws IllegalArgumentException, IOException {
        this.songEntriesList = new ArrayList<>();
        loadSongCatalogData(songFile);
    }

    /**
     * Reads the TSV file passed by main. It then reads the contents of the files
     * and creates the relevant SongEntry objects and returns them into a list.
     * Catches exception errors should they occur and it delegates handling of other
     * exceptions
     *
     * @param songFile This will be the dataset providing the song dataset.
     * @return List of SongEntry objects
     */
    private List<SongEntry> readDataFromFile(String songFile) throws IOException {
        if (songFile.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name: " + songFile);
        }
        songFile = songFile.substring(songFile.lastIndexOf("/") + 1);
        List<SongEntry> songEntriesList = new ArrayList<>();
        int count = 0;
        try (InputStream inputStream = AbstractSongCatalog.class.getClassLoader().getResourceAsStream(songFile);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Trim leading and trailing whitespaces
                line = line.trim();
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    // header skip
                    if (count == 0) {
                        count++;
                        continue;
                    }
                    SongEntry songEntry = new SongEntry(count, parseSongEntryLine(line));
                    songEntriesList.add(songEntry);
                    count++;
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Error processing line " + count + ": " + line + "\nCause: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading resource file: " + songFile, e);
        }
        return songEntriesList;
    }

    /**
     * Read the contents of filename and stores it.
     *
     * @param songFile file with the songs.
     */
    private void loadSongCatalogData(String songFile)
            throws IllegalArgumentException, IOException {
        List<SongEntry> entriesFromFile = readDataFromFile(songFile);
        songEntriesList.addAll(entriesFromFile);
    }

    /**
     * Returns the list of song entries.
     *
     * @return List<SongEntry>, a list song entries.
     */
    public List<SongEntry> getSongEntriesList() {
        return songEntriesList;
    }

    /**
     * Returns the list of song entries after filtering by SongDetail.
     *
     * @param filteredSongEntriesList the list of song entries used as input for
     *                                this filtering by SongDetail.
     * @param songDetail              the SongDetail to retrieve
     * @param name                    the name of the SongDetail to retrieve
     * @return List<SongEntry>, a </SongEntry> list with the relevant Song entries
     */
    public abstract List<SongEntry> getSongEntriesList(List<SongEntry> filteredSongEntriesList,
                                                       SongDetail songDetail, String name);

    /**
     * Parse the properties from a given line from a song catalog file.
     * You can expect that each value appears in the same order as the columns in
     * the file,
     * and that this order will not change.
     *
     * @param line the line to parse
     * @return a SongPropertyMap constructed from the parsed row, containing values
     * for every property
     * @throws IllegalArgumentException if the line is malformed (i.e. does not
     *                                  include every property
     *                                  for a single song, or contains undefined
     *                                  properties)
     */
    public abstract SongPropertyMap parseSongEntryLine(String line) throws IllegalArgumentException;

    /**
     * Get the minimum value of the given property for song entries in this song
     * catalog
     *
     * @param songProperty    the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested
     *                        minimum value
     * @return the minimum value of the property
     */
    public abstract double getMinimumValue(SongProperty songProperty, List<SongEntry> songEntriesList);

    /**
     * Get the maximum value of the given property for song entries in this song
     * catalog
     *
     * @param songProperty    the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested
     *                        maximum value
     * @return the maximum value of the property
     */
    public abstract double getMaximumValue(SongProperty songProperty, List<SongEntry> songEntriesList);

    /**
     * Get the average value of the given property for song entries in this song
     * catalog
     *
     * @param songProperty    the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested
     *                        average value
     * @return the average value of the property
     */
    public abstract double getAverageValue(SongProperty songProperty, List<SongEntry> songEntriesList);

    public abstract List<SongEntry> getFirstFiveSongEntries();

    public abstract List<String> getArtists();

    public abstract List<String> getAlbums();

    public List<String> getSongs() {
        List<String> songNames = new ArrayList<>();
        for (SongEntry song : songEntriesList) {
            songNames.add(song.getSongName());
        }
        return songNames;
    }

    public abstract int getNumberOfUniqueSongs();

    public abstract int getNumberOfUniqueArtists();

    public abstract double getAverageDurationOfSongs();

    public abstract double getAverageDurationOfTempo();
}
