package org.taf.api.specs.booking;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.taf.api.base.BaseApiTest;
import org.taf.api.client.rest.BookingClient;
import org.taf.api.model.BookingBody;
import org.taf.api.model.BookingDates;
import org.taf.api.steps.BookingSteps;

public abstract class BaseBookingTest extends BaseApiTest {

    @Value("${api.restful-booker.base-url}")
    protected String bookerBaseUrl;

    @Value("${api.restful-booker.username}")
    private String username;

    @Value("${api.restful-booker.password}")
    private String password;

    protected BookingSteps bookingSteps;

    @BeforeAll
    void setUpBooking() {
        String token = new BookingClient(bookerBaseUrl, null).acquireToken(username, password);
        bookingSteps = new BookingSteps(new BookingClient(bookerBaseUrl, token));
    }

    protected BookingBody defaultBody() {
        return BookingBody.builder()
                .firstName("James")
                .lastName("Brown")
                .totalPrice(111)
                .depositPaid(true)
                .bookingDates(new BookingDates("2024-01-01", "2024-02-01"))
                .additionalNeeds("Breakfast")
                .build();
    }
}
