package org.taf.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.taf.TafApplication;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(classes = TafApplication.class)
public abstract class BaseApiTest {

    @Value("${api.base-url}")
    private String baseUrl;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = baseUrl;
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
