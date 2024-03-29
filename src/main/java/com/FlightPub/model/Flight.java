package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;

/**
 * Java Object Representation of Database Object
 */
@Document("Flights")
public class Flight {

    @Id
    @Getter
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
    @Field("DepartureTime")
    private Long departureTime;

    @Getter
    @Field("DepartureTimeStopOver")
    private Long departureTimeStopOver;

    @Getter
    @Field("ArrivalTime")
    private Long arrivalTime;

    @Getter
    @Field("ArrivalTimeStopOver")
    private Long arrivalTimeStopOver;

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
    @Field("StopOverCode")
    private String stopoverCode;

    @Getter
    private String DepartureName;

    @Getter
    private String DestinationName;

    @Getter
    private String StopoverName;


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

    @Getter
    @Field("CheapestPrice")
    private String cheapestPrice;

    public Flight() {
    }

    // Conversions between various time formats
    public static String longToString(Long in) {
        try {
            Date date = new Date(in.longValue());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return format.format(date);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Long stringToLong(String in) {
        try {
            SimpleDateFormat format;
            if (in.length() == 10)
                format = new SimpleDateFormat("yyyy-MM-dd");
            else
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(in);
            return date.getTime();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static String dateToString(Date in) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return format.format(in);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Date stringToDate(String in) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(in);
            return date;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Date longToDate(Long in) {
        try {
            return new Date(in);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Long dateToLong(Date in) {
        try {
            return in.getTime();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void loadNames(String dep, String dest, String stop) {

        this.DepartureName = dep;

        this.DestinationName = dest;

        this.StopoverName = stop;

    }

    public void setFlightID(ObjectId id) {
        flightID = id.toString();
    }

    public void setFlightID(String id) {
        flightID = id;
    }

    // Setters for the time related class variables
    public void setDepartureTime(String time) {
        departureTime = stringToLong(time);
    }

    public void setArrivalTime(String time) {
        arrivalTime = stringToLong(time);
    }

    public void setArrivalTimeStopOver(String time) {
        arrivalTimeStopOver = stringToLong(time);
    }

    public void setDepartureTimeStopOver(String time) {
        departureTimeStopOver = stringToLong(time);
    }

    // Getters for thymeleaf (doesnt work with nested function calls on the template)
    public String getDepartureString() {
        return longToString(departureTime);
    }

    public String getArrivalString() {
        return longToString(arrivalTime);
    }

    public Date getDepartureDate() {
        return longToDate(departureTime);
    }

    public Date getArrivalDate() {
        return longToDate(arrivalTime);
    }

    public String getDepartureStopOverString() {
        return longToString(departureTimeStopOver);
    }

    public String getArrivalStopOverString() {
        return longToString(arrivalTimeStopOver);
    }

    public String getCheapestPrice() {
        return cheapestPrice != null ? cheapestPrice : "TBA";
    }

    public void setCheapestPrice(String cheapestFlights) {
        this.cheapestPrice = cheapestFlights;
    }


}
