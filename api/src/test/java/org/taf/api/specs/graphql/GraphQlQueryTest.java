package org.taf.api.specs.graphql;

import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.taf.api.data.GraphQlQueries;
import org.taf.tags.Tags;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Story("GraphQL queries")
class GraphQlQueryTest extends BaseGraphQlTest {

    @Test @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void list_movies_returns_non_empty_collection() {
        Response r = graphQlSteps.queryWithRetry(GraphQlQueries.MOVIES_PAGE, Map.of("first", 5, "skip", 0));

        r.then().body("errors", nullValue());
        List<?> movies = r.jsonPath().getList("data.movies");
        assertThat(movies).isNotNull().isNotEmpty();
        assertThat(r.jsonPath().getString("data.movies[0].id")).isNotBlank();
    }

    @Test @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void movie_by_id_returns_all_requested_fields() {
        String id = graphQlSteps.fetchFirstMovieId();

        Response r = graphQlSteps.queryWithRetry(GraphQlQueries.MOVIE_BY_ID, Map.of("id", id));

        r.then()
            .body("errors", nullValue())
            .body("data.movie.id", equalTo(id))
            .body("data.movie.title", not(emptyOrNullString()))
            .body("data.movie.slug", not(emptyOrNullString()));
    }

    @Test @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void fragment_query_returns_nested_published_by_name() {
        String id = graphQlSteps.fetchFirstMovieId();

        Response r = graphQlSteps.queryWithRetry(GraphQlQueries.MOVIE_WITH_FRAGMENT, Map.of("id", id));

        r.then().body("errors", nullValue());
        assertThat(r.jsonPath().getString("data.movie.id")).isEqualTo(id);
        assertThat(r.jsonPath().getString("data.movie.slug")).isNotBlank();
        assertThat(r.jsonPath().getString("data.movie.publishedBy.name")).isNotBlank();
    }

}
