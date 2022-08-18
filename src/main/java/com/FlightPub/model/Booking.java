package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Java Object Representation of Database Object
 */
@Document("Booking")
public class Booking {
    @Id
    @Setter
    @Getter
    private String bookingID;

    @Setter
    @Getter
    @Field("userID")
    private String userID;

    @Setter
    @Getter
    @Field("flightID")
    private String flightID;



    public Booking() {}

    public Booking(String bookingID, String userID, String flightID, List<String> bookedSeats, String bookingStatus){

        this.bookingID = bookingID;
        this.userID = userID;
        this.flightID = flightID;
    }
}
