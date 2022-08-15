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
import java.util.*;


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

    private static FlightServices flightServices;

    private LocationServices locService;

    BasicSearch(String destination, String origin) {
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
    public BasicSearch() {
    }

    // Returns a flight of flights according to the basic search
    public List<Flight> runBasicSearch(String start, String end, boolean stopover) {
        Long dstart = null;
        Long dend = null;
        // Catch date parse exceptions
        try {
            // Date Processing
            dstart = Flight.stringToLong(start);

            if (this.isExactdate()) {    // If search is for exact date, time frame is made for a 24 hour period
                dend = addBuffer(dstart, 0, 23, 59);
            } else {   // Upper date bound is made the end of day
                dend = Flight.stringToLong(end);
                dend = addBuffer(dend, 0, 23, 59);
            }

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        // Location Processing
        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = null;


        if (destinationIn != null && !destinationIn.equals("") && !destinationIn.equals("Show All"))  // Eliminates the empty Search for location
            destinationObj = locService.findByLocation(destinationIn);
        if (destinationIn == null || destinationIn.equals(""))   // Removes the 'null' from being displayed on the application
            destinationIn = "Show All";

        // Perform the correct Search
        if (originObj != null) {
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
    public List<StopOver> basicStopOverSearch(int numberOfStops) {
        List<Flight> firstFlights = runBasicSearch(start, end, true);  //Returns the list of flights perform BFS
        List<Flight> allFlights = flightServices.listAll();     // returns all flights in the database
        List<StopOver> flights = new ArrayList<>();   // Stores the singe stop over flights

        for (Flight flight : firstFlights) {
            flights.add(new StopOver(flight));
        }

        for (int count = 0; count < numberOfStops; count++) {
            List<StopOver> output = new ArrayList<>();  // Stores the stopovers of each stopover iteration
            for (StopOver flight : flights) {
                for (Flight newFlight : allFlights) {
                    if (flight.getFlightAtIndex(count).getDestinationCode().equals(newFlight.getDepartureCode())) {
                        if (flight.locationVisited(newFlight.getDestinationCode()) == false) {
                            if (isSuitableTiming(flight.getFlightAtIndex(count), newFlight)) {
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
        if (finalDestination != null) {
            List<StopOver> output = new ArrayList<>();
            for (StopOver flight : flights) {
                if (flight.getFlightAtIndex(numberOfStops - 1).getDestinationCode().equals(finalDestination))
                    output.add(flight);
            }
            flights = output;
        }
        return flights;
    }

    //#TODO convert promoted flights to Promoted Airlines
    // Determines which of the flights are promoted
      public List<Flight> getPromotedFlights(List<Flight> allFlights) {
        List<Flight> promoted = new ArrayList<>();
        for (Flight flight : allFlights) {
            if (false)
                promoted.add(flight);
        }
        return promoted;
    }

    // Determines where a flight would be suitable for a stopover in terms of its timing
    private boolean isSuitableTiming(Flight first, Flight second) {
        if (addBuffer(first.getArrivalTime(), 0, 1, 0).compareTo(second.getDepartureTime()) == -1) {
            if (addBuffer(first.getArrivalTime(), 1, 0, 0).compareTo(second.getDepartureTime()) >= 0) {
                return true;
            }
        }
        return false;
    }

    private Long addBuffer(Long date, int days, int hours, int minutes) {
        return date += days*24*60*60*1000 + hours*60*60*1000 + minutes*60*1000;
    }

    // Extension of the basic search that incorporates specific search parameters and filters
    public List<Flight> runAdvancedSearch(UserAccount user) {
        List<Flight> flights = this.runBasicSearch(this.start, this.end, false);
        List<Flight> filteredFlights = new ArrayList<>();

        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = locService.findByLocation(destinationIn);

        for (Flight flight : flights) {
            // Removes all indirect flights
            if (this.isDirectFlight()) {
                if (originObj != null && !flight.getDepartureCode().equals(originObj.getLocationID()))
                    continue;
                if (destinationObj != null && !flight.getDestinationCode().equals(destinationObj.getLocationID()))
                    continue;
            }
            // Filters by price
            if (minPrice != 0 || maxPrice != 100000) {
                double price = flight.getTicketPrice();
                if (minPrice != 0 && minPrice > price)
                    continue;
                if (maxPrice != 100000 && maxPrice < price)
                    continue;
            }
            // Filter by rating
            if (rating != 0 && flight.getRating() < rating)
                continue;
            // Filter to the number of seats
            if (seats > flightServices.getAvailableSeats(flight.getFlightNumber(), flight.getDepartureTime()))
                continue;
            // Filters flights that are not part of the membership program
            if (this.isMembershipFlights()) {
                // TODO: filter searches with the associated membership
            }
            filteredFlights.add(flight);    // adds the flight if all criteria is satisfied
        }

        return filteredFlights;
    }

    // Extension of the basicStopOverSearch that incorporates specific search parameters and filters
    public List<StopOver> advancedStopOverSearch(UserAccount user, int numberOfStops) {
        List<StopOver> flights = this.basicStopOverSearch(numberOfStops);
        List<StopOver> filteredFlights = new ArrayList<>();

        Location originObj = locService.findByLocation(originIn);
        Location destinationObj = locService.findByLocation(destinationIn);

        for (StopOver flight : flights) {
            // Filters by price
            if (minPrice != 0 || maxPrice != 100000) {
                double price = flight.getTotalCost();
                if ((minPrice != 0 && minPrice > price) || (maxPrice != 100000 && maxPrice < price))
                    continue;
            }
            // Filter by rating
            if (rating != 0 && flight.getMinRating() < rating)
                continue;

            boolean availableSeats = true;
            // Filter to the number of seats
            for(Flight x: flight.getFlights()){
                if(flightServices.getAvailableSeats(x.getFlightNumber(), x.getDepartureTime()) <= 0){
                    availableSeats = false;
                }
            }
            if (availableSeats)
                continue;
            // Filters flights that are not part of the membership program
            if (this.isMembershipFlights()) {
                // TODO: filter searches with the associated membership
            }
            filteredFlights.add(flight);    // adds the flight if all criteria is satisfied
        }

        return filteredFlights;
    }

    public List<String[]> setCheapestPriceForSearchResults(List<Flight> flight) {
        List<String[]> priceList = new ArrayList<>();
        for (Flight f : flight) {
            String[] price;
            price = flightServices.findCheapestFlight(f.getFlightID(), f.getFlightNumber(), f.getDepartureTime());
            priceList.add(price);
        }
        return priceList;
    }
}

