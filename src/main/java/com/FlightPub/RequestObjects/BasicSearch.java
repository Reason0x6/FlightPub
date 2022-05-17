package com.FlightPub.RequestObjects;


import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import com.FlightPub.model.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BasicSearch {
    @Getter
    @Setter
    private String destinationIn;

    @Getter
    @Setter
    private String originIn;

    @Getter
    @Setter
    private String start;

    @Getter
    @Setter
    private String end;

    @Getter
    @Setter
    private double minPrice = 0;

    @Getter
    @Setter
    private double maxPrice;

    @Getter
    @Setter
    private boolean directFlight;

    @Getter
    @Setter
    private int rating;

    @Getter
    @Setter
    private int seats;

    @Getter
    @Setter
    private boolean membershipFlights;

    @Getter
    @Setter
    private boolean nodest;

    @Getter
    @Setter
    private boolean exactdate;

    @Getter
    @Setter
    private boolean searchByArrival;

    private FlightServices flightServices;

    private LocationServices locService;

    BasicSearch(String destination, String origin){
        this.originIn = origin;
        this.destinationIn = destination;
    }

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationServices(LocationServices locRepo) {
        this.locService = locRepo;
    }

    public BasicSearch(){}

    public List<Flight> runBasicSearch(String start, String end) throws ParseException {
        Date dstart = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date dend = null;

        if(this.isExactdate()) {    // If search is for exact date, time frame is made for a 24 hour period
            dend = endOfDay(dstart);
        }else{   // Upper date bound is made the end of day
            dend = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            dend = endOfDay(dend);
        }

        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = locService.findByLocation(destinationIn);

        if(this.isSearchByArrival() == false)
        {
            // Performs Search where all parameters are known
            if (originObj != null && destinationObj != null) {
                return flightServices.getByOriginAndDestination(originObj.getLocationID(), destinationObj.getLocationID(), dstart, dend);
            }
            // Performs Search where destination is not known
            else if(originObj != null && destinationObj == null)
                return flightServices.getByOrigin(originObj.getLocationID(), dstart, dend);
        }else if(this.isSearchByArrival() == true)
        {
            // Performs Search where all parameters are known
            if (originObj != null && destinationObj != null) {
                return flightServices.getByOriginAndDestinationAndArrivalTimes(originObj.getLocationID(), destinationObj.getLocationID(), dstart, dend);
            }
            // Performs Search where destination is not known
            else if(originObj != null && destinationObj == null)
                return flightServices.getByOriginAndArrivalTimes(originObj.getLocationID(), dstart, dend);
        }
        return flightServices.getByOriginAndDestination(originIn, destinationIn, dstart, dend);
    }

    private Date endOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        return calendar.getTime();
    }

    public List<Flight> runAdvancedSearch(UserAccount user) throws ParseException {
        List<Flight> flights = this.runBasicSearch(this.start, this.end);
        List<Flight> filteredFlights = new ArrayList<>();

        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = locService.findByLocation(destinationIn);

        for(Flight flight : flights)
        {
            // Removes all indirect flights
            if(this.isDirectFlight()){
                if(originObj != null && !flight.getOriginID().equals(originObj.getLocationID()))
                    continue;
                if(destinationObj != null && !flight.getDestinationID().equals(destinationObj.getLocationID()))
                    continue;
            }
            // Filters by price
            if(minPrice != 0 || maxPrice != 100000){
                double price = flight.getTicketPrice();
                if(minPrice != 0 && minPrice > price)
                    continue;
                if(maxPrice != 100000 && maxPrice < price)
                    continue;
            }
            // Filter by rating
            if(rating != 0 && flight.getRating() < rating)
                continue;
            // Filter to the number of seats
            if(seats > (flight.getMaxSeats() - flight.getBookedSeats()))
                continue;
            // Filters flights that are not part of the membership program
            if(this.isMembershipFlights()){
                // TODO: fliter searches with the associated membership
            }
            filteredFlights.add(flight);    // adds the flight if all criteria is satisfied
        }

        return filteredFlights;
    }
}
