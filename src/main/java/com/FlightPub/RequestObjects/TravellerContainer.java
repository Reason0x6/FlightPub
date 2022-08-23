package com.FlightPub.RequestObjects;
import com.FlightPub.model.Traveller;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class TravellerContainer {

    public TravellerContainer(){

    }

    @Getter
    @Setter
    private Traveller[] travellers = new Traveller[100];
}
