package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("Availability")
public class Availability {
    @Id
    @Getter
    @Setter
    private String ID;

    @Getter
    @Setter
    @Field("AirlineCode")
    private String airlineCode;

    @Getter
    @Setter
    @Field("ClassCode")
    private String classCode;

    @Getter
    @Setter
    @Field("DepartureTime")
    private Date departureTime;

    @Getter
    @Setter
    @Field("FlightNumber")
    private String flightNumber;

    @Getter
    @Setter
    @Field("NumberAvailableSeatsLeg1")
    private int numberAvailableSeatsLeg1;

    @Getter
    @Setter
    @Field("NumberAvailableSeatsLeg2")
    private int numberAvailableSeatsLeg2;

    @Getter
    @Setter
    @Field("TicketCode")
    private String ticketCode;


    public Availability() {}


}
