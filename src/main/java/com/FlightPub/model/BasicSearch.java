package com.FlightPub.model;


import lombok.Getter;
import lombok.Setter;


public class BasicSearch {
    @Getter
    @Setter
    private String destination;
    @Getter
    @Setter
    private String departure;

    @Getter
    @Setter
    private String seatType;

    @Getter
    @Setter
    private String adults;

    @Getter
    @Setter
    private String kids;

    BasicSearch(String destination, String departure, String seatType, String adults, String kids){
        this.departure = departure;
        this.destination = destination;
        this.seatType = seatType;
        this.adults = adults;
        this.kids = kids;
    }


    BasicSearch(){}




}
