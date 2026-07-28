package org.taf.api.specs.graphql;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.taf.api.base.BaseApiTest;
import org.taf.api.client.graphql.GraphQlClient;
import org.taf.api.steps.GraphQlSteps;

public abstract class BaseGraphQlTest extends BaseApiTest {

    @Value("${api.graphql.base-url}")
    protected String graphqlBaseUrl;

    @Value("${api.graphql.auth-token:}")
    protected String authToken;

    protected GraphQlSteps graphQlSteps;

    @BeforeAll
    void setUpGraphQl() {
        graphQlSteps = new GraphQlSteps(new GraphQlClient(graphqlBaseUrl, authToken));
    }
}
