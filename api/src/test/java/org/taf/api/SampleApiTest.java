package org.taf.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SampleApiTest extends BaseApiTest {

    @Test
    void spring_context_loads_and_base_url_is_configured() {
        assertThat(RestAssured.baseURI).isNotBlank();
    System.out.println("work");
    }
}
