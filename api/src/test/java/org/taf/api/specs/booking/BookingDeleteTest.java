package org.taf.api.specs.booking;

import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.taf.tags.Tags;

import static org.hamcrest.Matchers.nullValue;

@Story("Booking delete")
class BookingDeleteTest extends BaseBookingTest {

    @Test @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void delete_booking_removes_it_and_get_returns_404() {
        int id = bookingSteps.createBooking(defaultBody()).jsonPath().getInt("bookingid");
        bookingSteps.deleteAndVerifyRemoved(id);
    }

    @Test @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void delete_booking_without_auth_returns_403() {
        int id = bookingSteps.createBooking(defaultBody()).jsonPath().getInt("bookingid");
        bookingSteps.deleteBookingNoAuthExpect403(id);
        bookingSteps.deleteBooking(id);
    }
}
