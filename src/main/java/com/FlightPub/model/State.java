package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("State")
public class State {
    @Id
    @Getter
    @Setter
    private int stateID;

    @Getter
    @Setter
    private String countryName;
}
