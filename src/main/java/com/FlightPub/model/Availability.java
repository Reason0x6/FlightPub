package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("Availability")
public class Availability {
    @Id
    @Getter
    @Setter
    private String ID;

    @Getter
    @Setter
    private String AirlineCode;

    @Getter
    @Setter
    private String ClassCode;

    @Getter
    @Setter
    private Date DepartureTime;

    @Getter
    @Setter
    private String FlightNumber;

    @Getter
    @Setter
    private int NumberAvailableSeatsLeg1;

    @Getter
    @Setter
    private int NumberAvailableSeatsLeg2;

    @Getter
    @Setter
    private String TicketCode;


    public Availability() {}


}
