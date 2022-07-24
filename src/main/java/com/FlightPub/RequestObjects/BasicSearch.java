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

    // Links associated Flight service
    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    // Links associated location service
    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationServices(LocationServices locRepo) {
        this.locService = locRepo;
    }

    // Default constructor
    public BasicSearch(){}

    // Returns a flight of flights accoridng to the basic search
    public List<Flight> runBasicSearch(String start, String end, boolean stopover) throws ParseException {
        // Date Processing
        Date dstart = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date dend = null;

        if(this.isExactdate()) {    // If search is for exact date, time frame is made for a 24 hour period
            dend = endOfDay(dstart);
        } else {   // Upper date bound is made the end of day
            dend = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            dend = endOfDay(dend);
        }

        // Location Proccessing
        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = null;

        if(destinationIn != null && !destinationIn.equals(""))  // Eliminates the empty Search for location
            destinationObj = locService.findByLocation(destinationIn);
        if(destinationIn == null || destinationIn.equals(""))   // Removes the 'null' from being displayed on the application
            destinationIn = "Show All";

        // Perform the correct Search
        if(originObj != null) {
            if (this.isSearchByArrival() == false) {
                // Performs Search where all parameters are known
                if (destinationObj != null && stopover == false) {
                    return flightServices.getByOriginAndDestination(originObj.getLocationID(), destinationObj.getLocationID(), dstart, dend);
                }
                // Performs Search where destination is not known
                else if (destinationObj == null || stopover == true)
                    return flightServices.getByOrigin(originObj.getLocationID(), dstart, dend);

            } else if (this.isSearchByArrival() == true) {
                // Performs Search where all parameters are known
                if (destinationObj != null && stopover == false) {
                    return flightServices.getByOriginAndDestinationAndArrivalTimes(originObj.getLocationID(), destinationObj.getLocationID(), dstart, dend);
                }
                // Performs Search where destination is not known
                else if (destinationObj == null || stopover == true)
                    return flightServices.getByOriginAndArrivalTimes(originObj.getLocationID(), dstart, dend);
            }
        }
        return flightServices.getByOriginAndDestination(originIn, destinationIn, dstart, dend);
    }

    // Returns a list of flights with a specified number of stopovers
    public List<StopOver> basicStopOverSearch(int numberOfStops) throws ParseException{
        List<Flight> firstFlights = runBasicSearch(start, end, true);  //Returns the list of flights perform BFS
        List<Flight> allFlights = flightServices.listAll();     // returns all flights in the database
        List<StopOver> flights = new ArrayList<>();   // Stores the singe stop over flights

        for(Flight flight : firstFlights) {
            flights.add(new StopOver(flight));
        }

        for(int count = 0; count < numberOfStops; count++) {
            List<StopOver> output = new ArrayList<>();  // Stores the stopovers of each stopover iteration
            for(StopOver flight : flights) {
                for(Flight newFlight : allFlights) {
                    if(flight.getFlightAtIndex(count).getDestinationID().equals(newFlight.getOriginID())) {
                        if(flight.locationVisited(newFlight.getDestinationID()) == false) {
                            if(isSuitableTiming(flight.getFlightAtIndex(count), newFlight)) {
                                flight.addFlight(newFlight);
                                output.add(flight);
                            }
                        }
                    }
                }
                flights = output;
            }
        }

        Location finalDestination = locService.findByLocation(destinationIn);
        if(finalDestination != null) {
            List<StopOver> output = new ArrayList<>();
            for(StopOver flight : flights) {
                if(flight.getFlightAtIndex(numberOfStops-1).getDestinationID().equals(finalDestination))
                    output.add(flight);
            }
            flights = output;
        }
        return flights;
    }

    // Determines where a flight would be suitable for a stopover in terms of its timing
    private boolean isSuitableTiming(Flight first, Flight second) {
        if(first.getArrival().compareTo(second.getDeparture()) == -1) {
            if(addBuffer(first.getArrival(), 1).compareTo(second.getDeparture()) >= 0) {
                return true;
            }
        }
        return false;
    }

    private Date addBuffer(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    // Converts the provided date to include the time for the end of the day
    private Date endOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        return calendar.getTime();
    }

    // Extension of the basic search that incorperates specific search parameters and filters
    public List<Flight> runAdvancedSearch(UserAccount user) throws ParseException {
        List<Flight> flights = this.runBasicSearch(this.start, this.end, false);
        List<Flight> filteredFlights = new ArrayList<>();

        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = locService.findByLocation(destinationIn);

        for(Flight flight : flights) {
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

    // Extension of the basicStopOverSearch that incorporates specific search parameters and filters
    public List<StopOver> advancedStopOverSearch(UserAccount user, int numberOfStops) throws ParseException {
        List<StopOver> flights = this.basicStopOverSearch(numberOfStops);
        List<StopOver> filteredFlights = new ArrayList<>();

        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = locService.findByLocation(destinationIn);

        for(StopOver flight : flights)
        {
            // Filters by price
            if(minPrice != 0 || maxPrice != 100000){
                double price = flight.getTotalCost();
                if((minPrice != 0 && minPrice > price) || (maxPrice != 100000 && maxPrice < price))
                    continue;
            }
            // Filter by rating
            if(rating != 0 && flight.getMinRating() < rating)
                continue;
            // Filter to the number of seats
            if(flight.seatsAvailable(seats) == false)
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
