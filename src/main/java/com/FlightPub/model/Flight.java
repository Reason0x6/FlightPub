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
    private Date departure;

    @Getter
    @Setter
    private Date arrival;

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

    @Getter
    @Setter
    private double rating;

    public Flight(){}

    public Flight(String flightID, String originID, String destinationID,
                  String departure, String arrival, String flightCode,
                  String airline, double ticketPrice){

        this.flightID = flightID;
        this.originID = originID;
        this.destinationID = destinationID;
        this.flightCode = flightCode;
        this.airline = airline;
        this.ticketPrice = ticketPrice;
        try {
            setDepartureTime(departure);
            setArrivalTime(arrival);
        }catch(Exception e){}

    }

    public void setArrivalTime(String in) throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddhhmmaa");
        Date date = originalFormat.parse(in);

        arrival = date;
    }

    public void setDepartureTime(String in) throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddhhmmaa");
        Date date = originalFormat.parse(in);

        departure = date;
    }


    public String getArrivalTime(){
        return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(arrival);
    }
    public String getDepartureTime(){
        return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(arrival);
    }
}
