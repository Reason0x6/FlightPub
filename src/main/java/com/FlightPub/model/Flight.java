package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

@Document("Flight")
public class Flight {

    @Id
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
    private String departure;

    @Getter
    @Setter
    private String arrival;

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
    private int maxSeats;

    @Getter
    @Setter
    private LinkedList<String> bookings;

    public Flight(){}

    public String getArrivalTime() throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddhhmmaa");
        Date date = originalFormat.parse(arrival);

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        return dateFormat.format(date);
    }

    public String getDepartureTime() throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddhhmmaa");
        Date date = originalFormat.parse(departure);

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        return dateFormat.format(date);
    }
}
