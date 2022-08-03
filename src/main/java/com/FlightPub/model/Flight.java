package com.FlightPub.model;

import com.FlightPub.Services.PlaneServices;
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
    private String DepartureCode;

    @Getter
    @Setter
    private String DestinationCode;

    @Getter
    @Setter
    private String PlaneCode;

    @Getter
    @Setter
    private Date DepartureTime;

    @Getter
    @Setter
    private Date ArrivalTime;

    @Getter
    @Setter
    private String FlightNumber;

    @Getter
    @Setter
    private String AirlineCode;


    //ADDITIONAL VARIABLES

    @Getter
    @Setter
    private Plane planeObj;

    @Getter
    @Setter
    private double ticketPrice;

    @Getter
    @Setter
    private Map<String, Integer> bookedSeats = new HashMap<>();

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
        this.DepartureCode = DepartureCode;
        this.DestinationCode = DestinationCode;
        this.FlightNumber = flightCode;
        this.AirlineCode = airline;
        this.ticketPrice = ticketPrice;


        try {
            setDepartureT(departure);
            setArrivalT(arrival);
        }catch(Exception e){}


    }

    public void setArrivalT(String in) throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddhhmmaa");
        Date date = originalFormat.parse(in);

        ArrivalTime = date;
    }

    public void setDepartureT(String in) throws ParseException {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddhhmmaa");
        Date date = originalFormat.parse(in);

        DepartureTime = date;
    }

    public int getTotalBookedSeats(){
        return 0;
    }

    public String getArrivalT(){
        return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(ArrivalTime);
    }
    public String getDepartureT(){
        return new SimpleDateFormat("dd/MM/yy hh:mm aa").format(DepartureTime);
    }

}
