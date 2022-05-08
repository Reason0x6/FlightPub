package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

public class Flight {
    @Getter
    @Setter
    private String flightID;

    @Getter
    @Setter
    private String originID;

    @Getter
    @Setter
    private String destinationID;

    @Getter
    @Setter
    private int departure;

    @Getter
    @Setter
    private int arrival;

    @Getter
    @Setter
    private String flightCode;

    @Getter
    @Setter
    private String airline;

    @Getter
    @Setter
    private double ticketPrice;

    @Getter
    @Setter
    private int bookedSeats;

    @Getter
    @Setter
    private LinkedList<String> bookings;
}
