package org.taf.api.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.taf.api.client.graphql.GraphQlClient;
import org.taf.api.data.GraphQlQueries;
import org.taf.api.model.MovieSummary;
import org.taf.util.Retry;

import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class GraphQlSteps {

    private final GraphQlClient client;

    public GraphQlSteps(GraphQlClient client) {
        this.client = client;
    }

    @Step("Execute GraphQL query with retry")
    public Response queryWithRetry(String query, Map<String, Object> variables) {
        Response r = retry(() -> client.executeQuery(query, variables));
        assertThat(r.statusCode()).isEqualTo(200);
        return r;
    }

    public Response queryWithRetry(String query) {
        return queryWithRetry(query, Map.of());
    }

    @Step("Execute raw GraphQL query (no status assertion)")
    public Response rawQuery(String query) {
        return client.executeRawQuery(query);
    }

    @Step("Execute raw GraphQL query with variables (no status assertion)")
    public Response rawQuery(String query, Map<String, Object> variables) {
        return client.executeQuery(query, variables);
    }

    @Step("Fetch first movie ID from list")
    public String fetchFirstMovieId() {
        Response list = queryWithRetry(GraphQlQueries.MOVIES_PAGE, Map.of("first", 1, "skip", 0));
        list.then().body("errors", nullValue());
        MovieSummary first = list.jsonPath().getObject("data.movies[0]", MovieSummary.class);
        Assumptions.assumeTrue(first != null, "No movies in schema — skipping");
        return first.id();
    }

    private Response retry(Supplier<Response> request) {
        return Retry.until(request, r -> r.statusCode() < 500);
    }
}
