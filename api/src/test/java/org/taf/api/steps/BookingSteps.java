package org.taf.api.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.taf.api.client.rest.BookingClient;
import org.taf.api.model.BookingBody;
import org.taf.util.Retry;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class BookingSteps {

    private final BookingClient client;

    public BookingSteps(BookingClient client) {
        this.client = client;
    }

    @Step("Create booking for {body.firstName} {body.lastName}")
    public Response createBooking(BookingBody body) {
        Response r = retry(() -> client.createBooking(body));
        assertThat(r.statusCode()).isEqualTo(200);
        return r;
    }

    @Step("Get booking {id}")
    public Response getBooking(int id) {
        return client.getBooking(id);
    }

    @Step("Get booking {id} — parsed to object")
    public BookingBody getBookingParsed(int id) {
        return client.getBooking(id).then().statusCode(200).extract().as(BookingBody.class);
    }

    @Step("Update booking {id}")
    public Response updateBooking(int id, BookingBody body) {
        Response r = client.updateBooking(id, body);
        assertThat(r.statusCode()).isEqualTo(200);
        return r;
    }

    @Step("Delete booking {id} and verify 404")
    public void deleteAndVerifyRemoved(int id) {
        client.deleteBooking(id).then().statusCode(201);
        client.getBooking(id).then().statusCode(404);
    }

    @Step("Delete booking {id}")
    public void deleteBooking(int id) {
        client.deleteBooking(id);
    }

    @Step("Filter bookings by firstname={firstname} lastname={lastname}")
    public Response filterByName(String firstname, String lastname) {
        Response r = client.getBookings(firstname, lastname);
        r.then()
            .statusCode(200)
            .body("$", instanceOf(java.util.List.class));
        return r;
    }

    @Step("Verify booking {id} returns 404")
    public void verifyNotFound(int id) {
        client.getBooking(id).then().statusCode(404);
    }

    @Step("Create booking with empty body — expect 500")
    public void createBookingEmptyExpect500() {
        client.createBookingEmpty().then().statusCode(500);
    }

    @Step("Update booking {id} without auth — expect 403")
    public void updateBookingNoAuthExpect403(int id, BookingBody body) {
        client.updateBookingNoAuth(id, body).then().statusCode(403);
    }

    @Step("Delete booking {id} without auth — expect 403")
    public void deleteBookingNoAuthExpect403(int id) {
        client.deleteBookingNoAuth(id).then().statusCode(403);
    }

    private Response retry(Supplier<Response> request) {
        return Retry.until(request, r -> r.statusCode() < 500);
    }
}
