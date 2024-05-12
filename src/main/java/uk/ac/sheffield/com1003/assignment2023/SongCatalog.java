package uk.ac.sheffield.com1003.assignment2023;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SKELETON IMPLEMENTATION
 */
public class SongCatalog extends AbstractSongCatalog {
    private static final int TRACK_NAME_INDEX = 0;
    private static final int TRACK_ARTIST_INDEX = 1;
    private static final int DURATION_MS_INDEX = 2;
    private static final int TRACK_ALBUM_NAME_INDEX = 3;
    private static final int TRACK_POPULARITY_INDEX = 4;
    private static final int DANCEABILITY_INDEX = 5;
    private static final int ENERGY_INDEX = 6;
    private static final int LOUDNESS_INDEX = 7;
    private static final int SPEECHINESS_INDEX = 8;
    private static final int ACOUSTICNESS_INDEX = 9;
    private static final int INSTRUMENTALNESS_INDEX = 10;
    private static final int LIVENESS_INDEX = 11;
    private static final int VALENCE_INDEX = 12;
    private static final int TEMPO_INDEX = 13;

    /**
     * constructor for the SongCatalog class
     *
     * @param songFile the file containing the song information
     * @throws IllegalArgumentException if the file is invalid
     * @throws IOException              if the file cannot be read
     */
    public SongCatalog(String songFile)
            throws IllegalArgumentException, IOException {
        super(songFile);
    }

    /**
     * parse the line and return a map of properties
     *
     * @param line the line to parse
     * @return a map of properties
     * @throws IllegalArgumentException if the line is invalid
     */
    @Override
    public SongPropertyMap parseSongEntryLine(String line) throws IllegalArgumentException {
        System.err.println(line);
        String[] fields = line.split("\t");

        SongPropertyMap songPropertyMap = new SongPropertyMap();
        // property
        songPropertyMap.putProperty(SongProperty.DURATION, Double.parseDouble(fields[DURATION_MS_INDEX]));
        songPropertyMap.putProperty(SongProperty.POPULARITY, Double.parseDouble(fields[TRACK_POPULARITY_INDEX]));
        songPropertyMap.putProperty(SongProperty.DANCEABILITY, Double.parseDouble(fields[DANCEABILITY_INDEX]));
        songPropertyMap.putProperty(SongProperty.ENERGY, Double.parseDouble(fields[ENERGY_INDEX]));
        songPropertyMap.putProperty(SongProperty.LOUDNESS, Double.parseDouble(fields[LOUDNESS_INDEX]));
        songPropertyMap.putProperty(SongProperty.SPEECHINESS, Double.parseDouble(fields[SPEECHINESS_INDEX]));
        songPropertyMap.putProperty(SongProperty.ACOUSTICNESS, Double.parseDouble(fields[ACOUSTICNESS_INDEX]));
        songPropertyMap.putProperty(SongProperty.INSTRUMENTALNESS, Double.parseDouble(fields[INSTRUMENTALNESS_INDEX]));
        songPropertyMap.putProperty(SongProperty.LIVENESS, Double.parseDouble(fields[LIVENESS_INDEX]));
        songPropertyMap.putProperty(SongProperty.VALENCE, Double.parseDouble(fields[VALENCE_INDEX]));
        songPropertyMap.putProperty(SongProperty.TEMPO, Double.parseDouble(fields[TEMPO_INDEX]));

        // detail
        songPropertyMap.putDetail(SongDetail.NAME, fields[TRACK_NAME_INDEX]);
        songPropertyMap.putDetail(SongDetail.ARTIST, fields[TRACK_ARTIST_INDEX]);
        songPropertyMap.putDetail(SongDetail.ALBUM_NAME, fields[TRACK_ALBUM_NAME_INDEX]);

        // return result
        return songPropertyMap;
    }

    /**
     * Returns a list of song entries that match the requested song property.
     *
     * @param songEntryList the list of song entries used as input for
     *                      this filtering by SongDetail.
     * @param songDetail    the SongDetail to retrieve
     * @param name          the name of the SongDetail to retrieve
     * @return a list of song entries that match the requested song property.
     */
    @Override
    public List<SongEntry> getSongEntriesList(List<SongEntry> songEntryList, SongDetail songDetail, String name) {
        return songEntryList.stream()
                .filter(song -> song.getSongDetail(songDetail).contains(name))
                .collect(Collectors.toList());
    }

    /**
     * Returns the minimum value of the requested song property.
     *
     * @param songProperty    the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested
     *                        minimum value
     * @return the minimum value of the requested song property
     * @throws NoSuchElementException if the requested song property does not have a value
     */
    @Override
    public double getMinimumValue(SongProperty songProperty, List<SongEntry> songEntriesList)
            throws NoSuchElementException {
        return songEntriesList.stream()
                .mapToDouble(song -> song.getSongProperty(songProperty))
                .min()
                .orElseThrow(() -> new NoSuchElementException("No value present for property: " + songProperty));
    }

    /**
     * Returns the maximum value of the requested song property.
     *
     * @param songProperty    the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested
     *                        maximum value
     * @return the maximum value of the requested song property
     * @throws NoSuchElementException if the requested song property does not have a value
     */
    @Override
    public double getMaximumValue(SongProperty songProperty, List<SongEntry> songEntriesList)
            throws NoSuchElementException {
        return songEntriesList.stream()
                .mapToDouble(song -> song.getSongProperty(songProperty))
                .max()
                .orElseThrow(() -> new NoSuchElementException("No value present for property: " + songProperty));

    }

    /**
     * Returns the average value of the requested song property.
     *
     * @param songProperty    the property to evaluate
     * @param songEntriesList list of song entries used to obtain the requested
     *                        average value
     * @return the average value of the requested song property
     * @throws NoSuchElementException if the requested song property does not have a value
     */
    @Override
    public double getAverageValue(SongProperty songProperty, List<SongEntry> songEntriesList)
            throws NoSuchElementException {
        return songEntriesList.stream()
                .mapToDouble(song -> song.getSongProperty(songProperty))
                .average()
                .orElseThrow(() -> new NoSuchElementException("No value present for property: " + songProperty));

    }

    /**
     * Returns the first five song entries in the catalog.
     *
     * @return the first five song entries in the catalog.
     */
    @Override
    public List<SongEntry> getFirstFiveSongEntries() {
        return getSongEntriesList().stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all artists in the catalog.
     *
     * @return a list of all artists in the catalog.
     */
    @Override
    public List<String> getArtists() {
        return getSongEntriesList().stream()
                .map(SongEntry::getSongArtist)
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .map(item -> item.replaceAll("\"", ""))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all albums in the catalog.
     *
     * @return a list of all albums in the catalog.
     */
    @Override
    public List<String> getAlbums() {
        return getSongEntriesList().stream()
                .map(SongEntry::getSongAlbumName)
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .map(item -> item.replaceAll("\"", ""))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the number of unique songs in the catalog.
     *
     * @return the number of unique songs in the catalog.
     */
    @Override
    public int getNumberOfUniqueSongs() {
        Set<String> uniqueSongs = new HashSet<>();
        for (SongEntry song : getSongEntriesList()) {
            uniqueSongs.add(song.getSongName());
        }
        return uniqueSongs.size();
    }

    /**
     * Calculates the number of unique artists in the catalog.
     *
     * @return the number of unique artists in the catalog.
     */
    @Override
    public int getNumberOfUniqueArtists() {
        Set<String> uniqueArtists = new HashSet<>();
        for (SongEntry song : getSongEntriesList()) {
            uniqueArtists.add(song.getSongArtist());
        }
        return uniqueArtists.size();
    }

    /**
     * Calculates the average duration of all songs in the catalog.
     *
     * @return the average duration of all songs in the catalog.
     */
    @Override
    public double getAverageDurationOfSongs() {
        List<SongEntry> songs = getSongEntriesList();
        if (songs.isEmpty()) {
            return 0.0;
        }
        double totalDuration = 0;
        for (SongEntry song : songs) {
            totalDuration += song.getSongProperty(SongProperty.DURATION);
        }
        return totalDuration / songs.size();
    }

    /**
     * Calculates the average tempo of all songs in the catalog.
     *
     * @return the average tempo of all songs in the catalog.
     */
    @Override
    public double getAverageDurationOfTempo() {
        List<SongEntry> songs = getSongEntriesList();
        if (songs.isEmpty()) {
            return 0.0;
        }
        double totalTemp = 0;
        for (SongEntry song : songs) {
            totalTemp += song.getSongProperty(SongProperty.TEMPO);
        }
        return totalTemp / songs.size();
    }
}
