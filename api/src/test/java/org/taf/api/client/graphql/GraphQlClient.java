package org.taf.api.client.graphql;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.taf.api.client.BaseApiClient;

import java.util.Map;

public class GraphQlClient extends BaseApiClient {

    private final String baseUrl;
    private final String authToken;

    public GraphQlClient(String baseUrl, String authToken) {
        this.baseUrl = baseUrl;
        this.authToken = authToken;
    }

    @Step("POST GraphQL query with variables")
    public Response executeQueryWithParams(String query, Map<String, Object> variables) {
        return spec()
                .body(Map.of("query", query, "variables", variables))
            .when()
                .post()
            .then()
                .extract().response();
    }

    @Step("POST GraphQL query")
    public Response executeQuery(String query) {
        return spec()
                .body(Map.of("query", query, "variables", Map.of()))
            .when()
                .post()
            .then()
                .extract().response();
    }

    private RequestSpecification spec() {
        RequestSpecification spec = baseSpec(baseUrl);
        if (authToken != null && !authToken.isBlank()) {
            spec = spec.header("Authorization", "Bearer " + authToken);
        }
        return spec;
    }
}
