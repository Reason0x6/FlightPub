package com.FlightPub.RequestObjects;

import lombok.Getter;
import lombok.Setter;

/**
 * New Group request object
 */
public class NewGroup {
    @Getter
    @Setter
    private String groupName;

    @Getter
    @Setter
    private String flightId;
}
