package org.taf.api.specs.booking;

import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.taf.api.model.BookingBody;
import org.taf.api.model.BookingDates;
import org.taf.tags.Tags;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Story("Booking create")
class BookingCreateTest extends BaseBookingTest {

    @Test @Tag(Tags.SMOKE)
    void create_booking_returns_booking_id_and_data() {
        BookingBody body = defaultBody();

        Response r = bookingSteps.createBooking(body);
        int id = r.jsonPath().getInt("bookingid");

        assertThat(id).isPositive();
        r.then()
            .body("booking.firstname", equalTo("James"))
            .body("booking.totalprice", equalTo(111));

        bookingSteps.deleteBooking(id);
    }

    @ParameterizedTest(name = "create [{0} {1}, price={2}, needs={3}]")
    @MethodSource("bookingVariants")
    @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void create_booking_with_various_guest_data(String firstname, String lastname, int price, String needs) {
        BookingBody body = BookingBody.builder()
                .firstName(firstname)
                .lastName(lastname)
                .totalPrice(price)
                .depositPaid(false)
                .bookingDates(new BookingDates("2024-03-01", "2024-03-05"))
                .additionalNeeds(needs)
                .build();

        Response r = bookingSteps.createBooking(body);
        int id = r.jsonPath().getInt("bookingid");

        assertThat(id).isPositive();
        r.then().body("booking.firstname", equalTo(firstname));

        bookingSteps.deleteBooking(id);
    }

    static Stream<Arguments> bookingVariants() {
        return Stream.of(
            Arguments.of("Alice", "Smith", 200, "Late checkout"),
            Arguments.of("Bob", "Jones", 99, ""),
            Arguments.of("Carol", "White", 500, "Extra pillows")
        );
    }

    @Test @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void create_booking_with_empty_body_returns_error() {
        bookingSteps.createBookingEmptyExpect500();
    }
}
