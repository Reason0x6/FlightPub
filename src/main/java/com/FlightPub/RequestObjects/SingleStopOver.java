package com.FlightPub.RequestObjects;

import com.FlightPub.model.Flight;
import lombok.Getter;
import lombok.Setter;

public class SingleStopOver {
    @Getter
    @Setter
    private Flight firstFlight;

    @Getter
    @Setter
    private Flight secondFlight;

    // Default constructor
    public SingleStopOver() {}

    // Multi argument constructor
    public SingleStopOver(Flight firstFlight, Flight secondFlight) {
        this.firstFlight = firstFlight;
        this.secondFlight = secondFlight;
    }
}
