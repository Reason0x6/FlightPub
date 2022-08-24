package com.FlightPub.RequestObjects;

import lombok.Getter;
import lombok.Setter;

/**
 * Booking Request Container request object
 */
public class BookingRequestContainer {
    @Getter
    @Setter
    private BookingRequest[] bookingRequest = new BookingRequest[4];

    public BookingRequestContainer() {
    }

    // Returns number of booking requests held in the array
    public int size() {
        int output = 0;
        for (int count = 0; count < bookingRequest.length; count++) {
            if (bookingRequest[count] != null)
                output++;
        }
        return output;
    }
}
