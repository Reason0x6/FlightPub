package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Airline")
public class Airline {
    @Id

    @Getter
    @Setter
    private String airlineID;

    @Getter
    @Setter
    private String airlineName;

    Airline(){}
}
