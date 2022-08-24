package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Java Object Representation of Database Object
 */
@Document("Booking")
public class Booking {
    @Id
    @Getter
    @Field("_id")
    private String id;

    @Getter
    @Field("accountEmail")
    private String accountEmail;

    @Setter
    @Getter
    @Field("flightID")
    private String flightID;

    @Getter
    @Setter
    @Field("travellerID")
    private String travellerID;

    @Getter
    @Setter
    @Field("seat")
    private String seat;

    @Getter
    @Setter
    @Field("confirmationID")
    private String confirmationID;

    @Getter
    @Setter
    private Flight flight;

    public Booking() {}

    public Booking(String accountEmail, String flightID, String travellerID, String bookedSeat, String confirmationID) {

        this.accountEmail = accountEmail;
        this.flightID = flightID;
        this.travellerID = travellerID;
        this.seat = bookedSeat;
        this.confirmationID = confirmationID;
    }


    public void setBookingID(ObjectId id) {
        this.id = id.toString();
    }

    public void setBookingID(String id) {
        this.id = id;
    }


}
