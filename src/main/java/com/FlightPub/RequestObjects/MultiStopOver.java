package com.FlightPub.RequestObjects;

import com.FlightPub.model.Flight;
import lombok.Getter;
import lombok.Setter;

public class MultiStopOver {
    @Getter
    @Setter
    private Flight firstFlight;

    @Getter
    @Setter
    private Flight secondFlight;

    @Getter
    @Setter
    private Flight thirdFlight;


    // Default constructor
    public MultiStopOver() {}

    // Multi argument constructor
    public MultiStopOver(Flight firstFlight, Flight secondFlight, Flight thirdFlight) {
        this.firstFlight = firstFlight;
        this.secondFlight = secondFlight;
        this.thirdFlight = thirdFlight;
    }
}
