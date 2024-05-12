package uk.ac.sheffield.com1003.assignment2023;

import uk.ac.sheffield.com1003.assignment2023.codeprovided.AbstractQueryParser;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.Query;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SongProperty;
import uk.ac.sheffield.com1003.assignment2023.codeprovided.SubQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a parser that converts a list of query strings into a list of Query objects.
 */
public class QueryParser extends AbstractQueryParser {

    /**
     * Converts a list of query strings into a list of Query objects.
     *
     * @param queryTokens provides the list of tokens to be used to build a list of queries
     * @return a list of queries
     */
    @Override
    public List<Query> buildQueries(List<String> queryTokens) {
        List<Query> queries = new ArrayList<>();
        List<SubQuery> subQueries = new ArrayList<>();

        for (int i = 0; i < queryTokens.size(); i++) {
            if ("WHERE".equalsIgnoreCase(queryTokens.get(i))
                    || "AND".equalsIgnoreCase(queryTokens.get(i))) {
                String songPropertyName = queryTokens.get(i + 1).toUpperCase();
                String operator = queryTokens.get(i + 2);
                double value = Double.parseDouble(queryTokens.get(i + 3));
                subQueries.add(new SubQuery(
                        SongProperty.fromName(songPropertyName),
                        operator, value));
                i += 3;
            }
        }
        queries.add(new Query(subQueries));
        return queries;
    }
}
