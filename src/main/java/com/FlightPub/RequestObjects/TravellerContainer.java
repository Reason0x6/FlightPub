package com.FlightPub.RequestObjects;
import com.FlightPub.model.Traveller;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Object used to store travellers
 */
public class TravellerContainer {

    @Setter
    private int counter = -1;

    @Getter
    @Setter
    private Traveller[] travellers = new Traveller[100];

    public TravellerContainer(){}

    public int getCounter() {
        counter ++;
        return counter;
    }
}
