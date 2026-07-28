package org.taf.api.specs.graphql;

import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.taf.api.data.GraphQlQueries;
import org.taf.tags.Tags;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Story("GraphQL negative")
class GraphQlNegativeTest extends BaseGraphQlTest {

    @Test @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void nonexistent_id_returns_null_data() {
        Response r = graphQlSteps.queryWithRetry(
                GraphQlQueries.MOVIE_BY_ID, Map.of("id", "nonexistent-id-00000000"));

        r.then()
            .body("data.movie", nullValue());
    }

    @Test @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void malformed_query_returns_errors() {
        Response r = graphQlSteps.rawQuery("{ invalid query syntax !@# }");

        r.then()
            .statusCode(anyOf(equalTo(200), equalTo(400)))
            .body("errors", not(empty()))
            .body("errors[0].message", not(emptyOrNullString()));
        assertThat(r.jsonPath().getMap("data")).isNull();
    }

    @Test @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void nonexistent_field_returns_validation_error() {
        String query = """
                query {
                  movies(first: 1) {
                    id
                    fieldThatDoesNotExist
                  }
                }
                """;
        Response r = graphQlSteps.rawQuery(query);

        r.then()
            .statusCode(anyOf(equalTo(200), equalTo(400)))
            .body("errors", not(empty()))
            .body("errors[0].message", containsString("fieldThatDoesNotExist"));
        assertThat(r.jsonPath().getMap("data")).isNull();
    }
}
