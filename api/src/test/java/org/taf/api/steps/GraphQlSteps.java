package org.taf.api.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.taf.api.client.graphql.GraphQlClient;
import org.taf.api.data.GraphQlQueries;
import org.taf.api.model.MovieSummary;
import org.taf.util.Retry;

import java.util.Map;

import static org.hamcrest.Matchers.nullValue;

public class GraphQlSteps {

    private final GraphQlClient client;

    public GraphQlSteps(GraphQlClient client) {
        this.client = client;
    }

    @Step("Execute GraphQL query with retry")
    public Response queryWithRetry(String query, Map<String, Object> variables) {
        return Retry.until(() -> client.executeQueryWithParams(query, variables), resp -> resp.statusCode() == 200);
    }

    @Step("Execute GraphQL query")
    public Response executeQuery(String query) {
        return client.executeQuery(query);
    }

    @Step("Execute GraphQL query with variables")
    public Response executeQueryWithParams(String query, Map<String, Object> variables) {
        return client.executeQueryWithParams(query, variables);
    }

    @Step("Fetch first movie ID from list")
    public String fetchFirstMovieId() {
        Response list = queryWithRetry(GraphQlQueries.MOVIES_PAGE, Map.of("first", 1, "skip", 0));
        list.then().body("errors", nullValue());
        MovieSummary first = list.jsonPath().getObject("data.movies[0]", MovieSummary.class);
        Assumptions.assumeTrue(first != null, "No movies in schema — skipping");
        return first.id();
    }

}
