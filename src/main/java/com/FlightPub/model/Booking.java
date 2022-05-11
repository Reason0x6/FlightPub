package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Booking {
    @Id
    @Setter
    @Getter
    private String bookingID;

    @Setter
    @Getter
    private String userID;

    @Setter
    @Getter
    private String flightID;

    @Setter
    @Getter
    private List<String> bookedSeats;

    @Setter
    @Getter
    private String bookingStatus;

    public Booking() {}
}
