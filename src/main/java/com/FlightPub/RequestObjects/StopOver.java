package com.FlightPub.RequestObjects;
import com.FlightPub.model.Flight;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class StopOver {
    @Getter
    @Setter
    List<Flight> flights = new ArrayList<>();

    // Default constructor
    public StopOver() {}

    // Single Flight constructor
    public StopOver(Flight flight) {
        flights.add(flight);
    }

    // copy constructor that adds another flight
    public StopOver(List<Flight> flights, Flight newFlight) {
        for(Flight flight : flights)
            this.flights.add(flight);
        this.flights.add(newFlight);
    }

    // Adds a Flight to the object
    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    // Returns the flight at a given Index
    public Flight getFlightAtIndex(int index) {
        return flights.get(index);
    }

    // Tests if a provided location has been visited in this stopover chain
    public boolean locationVisited(String destLocation, String stopoverLocation) {
        for(Flight flight : flights) {
            if(flight.getDepartureCode().equals(destLocation) || flight.getDepartureCode().equals(stopoverLocation))
                return true;
            if(flight.getStopoverCode()!=null && (flight.getStopoverCode().equals(destLocation)||flight.getStopoverCode().equals(destLocation)))
                return true;
        }
        return false;
    }

    // Returns the total combined price of th flight
    public double getTotalCost() {
        double total = 0;
        for(Flight flight : flights)
            total += flight.getTicketPrice();
        return total;
    }

    // Returns the minimum rating of the flight sequence
    public double getMinRating() {
        double min = flights.get(0).getRating();
        for(Flight flight : flights) {
            if(flight.getRating() < min)
                min = flight.getRating();
        }
        return min;
    }

    // Returns the number of flights in the stopover
    public int getNumberOfStops() {
        int count = flights.size();
        for(Flight flight : flights) {
            if(flight.getStopoverCode() != null)
                count++;
        }
        System.out.print(count);
        return count;
    }

    // Returns the size of the list
    public int size() { return flights.size(); }
    
}
