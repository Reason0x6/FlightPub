package com.FlightPub.RequestObjects;

import lombok.Getter;
import lombok.Setter;

public class BookingRequestContainer {
    @Getter
    @Setter
    private BookingRequest[] bookingRequest = new BookingRequest[4];

    public BookingRequestContainer() {}
}
