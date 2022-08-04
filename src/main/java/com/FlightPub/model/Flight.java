package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Document("Flights")
public class Flight {

    @Id
    @Getter
    @Setter
    private String flightID;

    @Getter
    @Setter
    private String departureCode;

    @Getter
    @Setter
    private String destinationCode;

    @Getter
    @Setter
    private String planeCode;

    @Getter
    @Setter
    private Date departureTime;

    @Getter
    @Setter
    private Date arrivalTime;

    @Getter
    @Setter
    private String flightNumber;

    @Getter
    @Setter
    private String airlineCode;


    //ADDITIONAL VARIABLES

    @Getter
    @Setter
    private double ticketPrice;


    @Getter
    @Setter
    private int maxSeats;

    @Getter
    @Setter
    private LinkedList<String> bookings;

    @Getter
    @Setter
    private double rating;

    public Flight(){

    }

    public Flight(String flightID,
                  String airline, String arrival, String DepartureCode, String DestinationCode,
                  String departure,  String flightCode, double ticketPrice){

        this.flightID = flightID;
        this.departureCode = DepartureCode;
        this.destinationCode = DestinationCode;
        this.flightNumber = flightCode;
        this.airlineCode = airline;
        this.ticketPrice = ticketPrice;


        try {
            setDepartureT(departure);
            setArrivalT(arrival);
        }catch(Exception e){}


    }

    public void setArrivalT(String in) throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddhhmmaa");
        Date date = originalFormat.parse(in);

        arrivalTime = date;
    }

    public void setDepartureT(String in) throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddhhmmaa");
        Date date = originalFormat.parse(in);

        departureTime = date;
    }



    public String getArrivalT(){
        return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(arrivalTime);
    }
    public String getDepartureT(){
        return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(departureTime);
    }

}
