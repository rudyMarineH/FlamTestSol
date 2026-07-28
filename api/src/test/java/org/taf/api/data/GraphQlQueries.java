package org.taf.api.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public final class GraphQlQueries {
    private GraphQlQueries() {}

    public static final String MOVIES_PAGE         = load("/graphql/query/GetMovies.graphql");
    public static final String MOVIE_BY_ID         = load("/graphql/query/GetMovie.graphql");
    public static final String MOVIE_WITH_FRAGMENT = load("/graphql/query/GetMovieDetail.graphql");

    private static String load(String path) {
        try (InputStream is = GraphQlQueries.class.getResourceAsStream(path)) {
            return new String(Objects.requireNonNull(is, "GraphQL file not found: " + path).readAllBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
