package uk.ac.sheffield.com1003.assignment2023.codeprovided;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an Abstract class in charge of parsing the text file containing the queries
 * and building them using the class Query.
 * <p>
 * Should be implemented as uk.ac.sheffield.assignment2023.QueryParser
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 * <p>
 * Copyright (c) University of Sheffield 2023
 */

public abstract class AbstractQueryParser {

    /**
     * This method gets a text file containing a list of queries and returns a list
     * of tokens that will be used to build the queries that need to be executed.
     *
     * @param queryFileLocation provides the file name containing the queries.
     * @return a list of Strings representing query tokens
     */
    public static List<String> readQueryTokensFromFile(String queryFileLocation) {
        List<String> queryTokens = new ArrayList<>();
        if (queryFileLocation.isEmpty()) {
            System.err.println("No query file location provided. No queries will be executed.");
            return queryTokens;
        }

        queryFileLocation = queryFileLocation.substring(queryFileLocation.lastIndexOf("/") + 1);

        // Improved delimiter to handle one or more spaces
        String splitRegex = "\\s+";

        try (InputStream inputStream = AbstractSongCatalog.class.getClassLoader().getResourceAsStream(queryFileLocation);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Trim leading and trailing spaces
                line = line.trim();
                if (line.isEmpty()) {
                    // Skip empty lines
                    continue;
                }
                String[] query = line.toLowerCase().split(splitRegex);
                for (String token : query) {
                    if (!token.isEmpty()) {
                        queryTokens.add(token);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(queryFileLocation + " could not be found. No queries will be executed.");
        } catch (IOException e) {
            System.err.println("File could not be handled. No queries will be executed.");
        }
        return queryTokens;
    }


    /**
     * This abstract method builds a list of Query objects representing the queries provided
     * in the list of tokens provided as an argument.
     *
     * @param queryTokens provides the list of tokens to be used to build a list of queries
     * @return a list of queries
     */
    public abstract List<Query> buildQueries(List<String> queryTokens);
}
