package com.FlightPub.RequestObjects;

import com.FlightPub.model.Availability;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Price;
import lombok.Getter;
import lombok.Setter;

public class AvailabilityContainer {
    @Getter
    @Setter
    private Flight flight;

    @Getter
    @Setter
    private Availability availability;

    @Getter
    @Setter
    private String className;

    @Getter
    @Setter
    private String ticketName;

    public AvailabilityContainer() {}

    public AvailabilityContainer(Availability availability, String className, String ticketName) {
        this.availability = availability;
        this.className = className;
        this.ticketName = ticketName;
    }
}
