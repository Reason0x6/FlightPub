package com.FlightPub.RequestObjects;

import com.FlightPub.model.Availability;
import com.FlightPub.model.Flight;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class EditedFlightContainer {
    @Getter
    @Setter
    private Flight flight;

    @Getter
    @Setter
    private List<Availability> availabilities;

    public EditedFlightContainer() {}

    public EditedFlightContainer(Flight flight, List<Availability> availabilities) {
        this.flight = flight;
        this.availabilities = availabilities;
    }

}
