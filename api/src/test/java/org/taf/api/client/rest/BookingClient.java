package org.taf.api.client.rest;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.taf.api.client.BaseApiClient;
import org.taf.api.model.BookingBody;

import java.util.Map;

public class BookingClient extends BaseApiClient {

    private final String baseUrl;
    private final String token;

    public BookingClient(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl;
        this.token = authenticate(baseUrl, username, password);
    }

    private String authenticate(String baseUrl, String username, String password) {
        return rawSpec(baseUrl)
                .body(Map.of("username", username, "password", password))
            .when()
                .post("/auth")
            .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
    }

    private RequestSpecification spec() {
        return baseSpec(baseUrl);
    }

    private RequestSpecification specWithAuth() {
        return spec().cookie("token", token);
    }

    @Step("POST /booking — {body.firstName} {body.lastName}")
    public Response createBooking(BookingBody body) {
        return spec()
                .body(body)
            .when()
                .post("/booking")
            .then()
                .extract().response();
    }

    @Step("POST /booking — empty body")
    public Response createBookingEmpty() {
        return spec()
                .body("{}")
            .when()
                .post("/booking")
            .then()
                .extract().response();
    }

    @Step("GET /booking/{id}")
    public Response getBooking(int id) {
        return spec()
            .when()
                .get("/booking/" + id)
            .then()
                .extract().response();
    }

    @Step("GET /booking?firstname={firstname}&lastname={lastname}")
    public Response getBookings(String firstname, String lastname) {
        return spec()
                .queryParam("firstname", firstname)
                .queryParam("lastname", lastname)
            .when()
                .get("/booking")
            .then()
                .extract().response();
    }

    @Step("PUT /booking/{id}")
    public Response updateBooking(int id, BookingBody body) {
        return specWithAuth()
                .body(body)
            .when()
                .put("/booking/" + id)
            .then()
                .extract().response();
    }

    @Step("PUT /booking/{id} — no auth")
    public Response updateBookingNoAuth(int id, BookingBody body) {
        return spec()
                .body(body)
            .when()
                .put("/booking/" + id)
            .then()
                .extract().response();
    }

    @Step("DELETE /booking/{id}")
    public Response deleteBooking(int id) {
        return specWithAuth()
            .when()
                .delete("/booking/" + id)
            .then()
                .extract().response();
    }

    @Step("DELETE /booking/{id} — no auth")
    public Response deleteBookingNoAuth(int id) {
        return spec()
            .when()
                .delete("/booking/" + id)
            .then()
                .extract().response();
    }

}
