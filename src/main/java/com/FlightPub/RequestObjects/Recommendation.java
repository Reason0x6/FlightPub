package com.FlightPub.RequestObjects;

import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Recommendation {
    @Setter
    private String recommendationLocation;
    @Setter
    private LocationServices locationServices;
    @Setter
    private FlightServices flightServices;

    public Recommendation(LocationServices locationServices, FlightServices flightServices) {
        this.locationServices = locationServices;
        this.flightServices = flightServices;
    }

    public List<Flight> getRecommendation() {
        // TODO If a user is logged in get their preferred location
        // Set current location
        Location currentLocation;
        if(recommendationLocation == null) {
            currentLocation = locationServices.mostPopular();
        } else {
            currentLocation = locationServices.getById(recommendationLocation);
        }

        // If no current location is found in database
        if (currentLocation == null) {
            System.err.println("No current location was found in database");
            return null;
        }

        // Create new search
        BasicSearch search = new BasicSearch();
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        search.setOriginIn(currentLocation.getLocationID());

        // The final list of recommended flights
        List<Flight> recommendedFlights = new LinkedList<>();

        // Get current date and date in 3 months
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String today = dateFormat.format(date);

        cal.add(Calendar.MONTH, 3);
        date = cal.getTime();
        String max = dateFormat.format(date);

        // Get currently popular locations
        List<Location> locations = locationServices.findAllSortedDescendingExcluding(currentLocation.getLocationID());

        // Get 1 flight from each popular location
        for (Location popularLocation : locations) {

            // Set search destination to next popular location
            search.setDestinationIn(popularLocation.getLocationID());

            // Find flights that are going from current location to popular location
            try {
                // TODO look into why this doesnt set the destination correctly
                // List<Flight> recommendSearch = search.runBasicSearch(today, max, false);
                List<Flight> recommendSearch = flightServices.getByOriginAndDestination(
                        currentLocation.getLocationID(),
                        popularLocation.getLocationID(),
                        new SimpleDateFormat("yyyy-MM-dd").parse(today),
                        new SimpleDateFormat("yyyy-MM-dd").parse(max));
                // If at least one flight was found add first flight to recommendation list
                if(!recommendSearch.isEmpty()) {
                    recommendedFlights.add(recommendSearch.get(0));
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Once 3 recommended flights have been found break for loop
            if (recommendedFlights.size() == 4) {
                break;
            }
        }
        
        System.out.println("Recommended Flights found:");
        for (Flight flight : recommendedFlights) {
            System.out.printf("FlightID: %s, Flight Origin: %s, Flight Destination: %s %n",flight.getFlightID(), flight.getOriginID(), flight.getDestinationID());
        }

        return recommendedFlights;
    }

    public String getRecommendationLocation() {
        if(recommendationLocation == null) {
            recommendationLocation = locationServices.mostPopular().getLocationID();
        }
        return recommendationLocation;
    }
}
