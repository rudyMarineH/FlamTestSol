package org.taf.api.specs.booking;

import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.taf.tags.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.taf.api.model.BookingBody;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Booking read")
class BookingReadTest extends BaseBookingTest {

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
    void get_booking_by_id_returns_correct_data() {
        BookingBody booking = bookingSteps.getBookingParsed(bookingId);

        assertThat(booking.getFirstName()).isEqualTo("James");
        assertThat(booking.getLastName()).isEqualTo("Brown");
        assertThat(booking.getTotalPrice()).isEqualTo(111);
        assertThat(booking.isDepositPaid()).isTrue();
        assertThat(booking.getBookingDates().checkin()).isEqualTo("2024-01-01");
        assertThat(booking.getBookingDates().checkout()).isEqualTo("2024-02-01");
        assertThat(booking.getAdditionalNeeds()).isEqualTo("Breakfast");
    }

    @ParameterizedTest(name = "filter by firstname={0} lastname={1}")
    @CsvSource({"James,Brown", "Alice,Smith"})
    @Tag(Tags.POSITIVE) @Tag(Tags.SMOKE)
    void get_booking_list_filtered_by_name(String firstname, String lastname) {
        bookingSteps.filterByName(firstname, lastname);
    }

    @Test @Tag(Tags.NEGATIVE) @Tag(Tags.SMOKE)
    void get_nonexistent_booking_returns_404() {
        bookingSteps.verifyNotFound(999999999);
    }
}
