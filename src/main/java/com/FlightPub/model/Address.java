package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Address")
public class Address {
    @Id
    @Setter
    @Getter
    private String UserID;

    @Getter
    @Setter
    private String streetNumber;

    @Getter
    @Setter
    private String street;

    @Getter
    @Setter
    private String suburb;

    @Getter
    @Setter
    private int stateID;

    @Getter
    @Setter
    private int postcode;
}
