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
    private String AirlineCode;

    @Getter
    @Setter
    @Field("ClassCode")
    private String ClassCode;

    @Getter
    @Setter
    @Field("DepartureTime")
    private Date DepartureTime;

    @Getter
    @Setter
    @Field("FlightNumber")
    private String FlightNumber;

    @Getter
    @Setter
    @Field("NumberAvailableSeatsLeg1")
    private int NumberAvailableSeatsLeg1;

    @Getter
    @Setter
    @Field("NumberAvailableSeatsLeg2")
    private int NumberAvailableSeatsLeg2;

    @Getter
    @Setter
    @Field("TicketCode")
    private String TicketCode;


    public Availability() {}


}
