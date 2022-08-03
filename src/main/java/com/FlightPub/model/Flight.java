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
import java.util.List;

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
    private Date departure;

    @Getter
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

    @Getter
    @Setter
    private Plane plane = new Plane();

    @Getter
    @Setter
    private boolean promoted;

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
        this.setDeparture(departure);
        this.setArrival(arrival);
        this.promoted = false;

        plane = new Plane();

    }

    // Formats the String to be parsed into a Date Object
    public void setArrival(String in){
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
            Date date = originalFormat.parse(in);
            this.arrival = date;
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    public void setDeparture(String in){
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
            Date date = originalFormat.parse(in);
            this.departure = date;
        } catch (ParseException e) {
            System.out.println(e);
        }
    }


    // Returns the Date in a readable String format
    public String getArrivalTime(){
        if(arrival == null)
            return null;
        else
            return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(arrival);
    }

    public String getDepartureTime(){
        if(arrival == null)
            return null;
        else
            return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(departure); }

    // Returns the Date in a String format that conforms to the expected format of DateTime-local (HTML input)
    public String getArrivalDateTime(){
        if(arrival == null)
            return null;
        else
            return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").format(arrival);
    }

    public String getDepartureDateTime(){
        if(arrival == null)
            return null;
        else
            return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").format(departure); }

    public List<String> getAllSeats(){
        return plane.getSeats();
    }

}
