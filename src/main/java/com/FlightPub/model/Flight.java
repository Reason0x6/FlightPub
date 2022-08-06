package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field("_id")
    private String flightID;

    @Getter
    @Setter
    @Field("DepartureCode")
    private String departureCode;

    @Getter
    @Setter
    @Field("DestinationCode")
    private String destinationCode;

    @Getter
    @Setter
    @Field("PlaneCode")
    private String planeCode;

    @Getter
    @Setter
    @Field("DepartureTime")
    private Date departureTime;

    @Getter
    @Setter
    @Field("DepartureTimeStopOver")
    private Date DepartureTimeStopOver;

    @Getter
    @Setter
    @Field("ArrivalTime")
    private Date arrivalTime;

    @Getter
    @Setter
    @Field("ArrivalTimeStopOver")
    private Date ArrivalTimeStopOver;

    @Getter
    @Setter
    @Field("FlightNumber")
    private String flightNumber;

    @Getter
    @Setter
    @Field("AirlineCode")
    private String airlineCode;

    @Getter
    @Setter
    @Field("Duration")
    private int duration;

    @Getter
    @Setter
    @Field("DurationSecondLeg")
    private int durationSecondLeg;

    @Getter
    @Setter
    @Field("StopoverCode")
    private String stopoverCode;



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
/*
    public Flight(String flightID,
                  String airline, String arrival, String DepartureCode, String DestinationCode,
                  String departure,  String flightCode){

        this.flightID = flightID;
        this.departureCode = DepartureCode;
        this.destinationCode = DestinationCode;
        this.flightNumber = flightCode;
        this.airlineCode = airline;



        try {
            setDepartureT(departure);
            setArrivalT(arrival);
        }catch(Exception e){}


    } */

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
