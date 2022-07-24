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

    // Adds a Flight to the object
    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    // Returns the flight at a given Index
    public Flight getFlightAtIndex(int index) {
        return flights.get(index);
    }

    // Tests if a provided location has been visited in this stopover chain
    public boolean locationVisited(String location) {
        for(Flight flight : flights) {
            if(flight.getOriginID().equals(location))
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

    // Checks that the specified number of seats are available on all flights
    public boolean seatsAvailable(int numberOfSeats) {
        for(Flight flight : flights) {
            if(flight.getMaxSeats() - flight.getBookedSeats() < numberOfSeats)
                return false;
        }
        return true;
    }
}
