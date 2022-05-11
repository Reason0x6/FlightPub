package com.FlightPub.RequestObjects;


import com.FlightPub.Services.FlightServices;
import com.FlightPub.model.Flight;
import com.FlightPub.repository.FlightRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class BasicSearch {
    @Getter
    @Setter
    private String destination;
    @Getter
    @Setter
    private String departure;

    @Getter
    @Setter
    private String start;

    @Getter
    @Setter
    private String end;
    private FlightServices flightServices;

    BasicSearch(String destination, String departure){
        this.departure = departure;
        this.destination = destination;

    }

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    public BasicSearch(){}

    public List<Flight> runBasicSearch(String start, String end) throws ParseException {
        Date dstart = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date dend = new SimpleDateFormat("yyyy-MM-dd").parse(end);
        System.out.println(dstart);
        System.out.println(dend);
        return flightServices.getByOriginAndDestination(departure, destination, dstart, dend);
    }

}
