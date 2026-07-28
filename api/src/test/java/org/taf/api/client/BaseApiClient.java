package org.taf.api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    protected RequestSpecification baseSpec(String baseUrl) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept("application/json")
                .filter(new AllureRestAssured())
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
    }

    protected RequestSpecification rawSpec(String baseUrl) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept("application/json")
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
    }
}
