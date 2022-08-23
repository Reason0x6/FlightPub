package com.FlightPub.RequestObjects;
import com.FlightPub.model.Traveller;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class TravellerContainer {

    public TravellerContainer(){
        for (int count = 0; count < 9; count++) {
            travellers.add(new Traveller());
        }
    }

    @Getter
    @Setter
    private List<Traveller> travellers = new ArrayList<>();
}
