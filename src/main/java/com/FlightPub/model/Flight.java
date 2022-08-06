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
        try{
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
            Date date = originalFormat.parse(in);
            this.arrivalTime = date;
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    public void setDeparture(String in){
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
            Date date = originalFormat.parse(in);
            this.departureTime = date;
        } catch (ParseException e) {
            System.out.println(e);
        }
    }


    // Returns the Date in a readable String format
    public String getArrivalTime(){
        if(arrivalTime == null)
            return null;
        else
            return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(arrivalTime);
    }

    public String getDepartureTime(){
        if(arrivalTime == null)
            return null;
        else
            return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(departureTime); }

    // Returns the Date in a String format that conforms to the expected format of DateTime-local (HTML input)
    public String getArrivalDateTime(){
        if(arrivalTime == null)
            return null;
        else
            return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").format(arrivalTime);
    }

    public String getDepartureDateTime(){
        if(arrivalTime == null)
            return null;
        else
            return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").format(departureTime);
    }



}
