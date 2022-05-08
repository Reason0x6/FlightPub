package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Plane")
public class Plane {
    @Id
    @Getter
    @Setter
    private String planeID;

    @Getter
    @Setter
    private String airlineID;

    @Getter
    @Setter
    private int seatCount;

    @Getter
    @Setter
    private String planeType;
}
