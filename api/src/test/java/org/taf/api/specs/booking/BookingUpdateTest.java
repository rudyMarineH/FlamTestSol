package org.taf.api.specs.booking;

import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.taf.tags.Tags;
import org.taf.api.model.BookingBody;

import static org.hamcrest.Matchers.equalTo;

@Story("Booking update")
class BookingUpdateTest extends BaseBookingTest {

    private int bookingId;

    @BeforeEach
    void createFixture() {
        bookingId = bookingSteps.createBooking(defaultBody()).jsonPath().getInt("bookingid");
    }

    @AfterEach
    void deleteFixture() {
        bookingSteps.deleteBooking(bookingId);
    }

    @Test @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void update_booking_replaces_fields() {
        BookingBody updated = defaultBody();
        updated.setFirstName("UpdatedName");
        updated.setTotalPrice(999);

        bookingSteps.updateBooking(bookingId, updated)
            .then()
                .body("firstname", equalTo("UpdatedName"))
                .body("totalprice", equalTo(999));
    }

    @Test @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void update_booking_twice_changes_different_fields() {
        BookingBody body = defaultBody();

        body.setFirstName("AliceUpdated");
        bookingSteps.updateBooking(bookingId, body)
            .then()
                .body("firstname", equalTo("AliceUpdated"))
                .body("totalprice", equalTo(111));

        body.setTotalPrice(350);
        bookingSteps.updateBooking(bookingId, body)
            .then()
                .body("firstname", equalTo("AliceUpdated"))
                .body("totalprice", equalTo(350));
    }

    @Test @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void update_booking_without_auth_returns_403() {
        bookingSteps.updateBookingNoAuthExpect403(bookingId, defaultBody());
    }
}
