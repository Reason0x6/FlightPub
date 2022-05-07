package com.FlightPub.Controllers;

import lombok.Getter;
import lombok.Setter;

public class BasicSearch {
    @Getter
    @Setter
    private String destination;
    @Getter
    @Setter
    private String depature;

    @Getter
    @Setter
    private String seatType;

    @Getter
    @Setter
    private String adults;

    @Getter
    @Setter
    private String kids;

    BasicSearch(String destination, String depature, String seatType, String adults, String kids){
        this.depature = depature;
        this.destination = destination;
        this.seatType = seatType;
        this.adults = adults;
        this.kids = kids;
    }




}
