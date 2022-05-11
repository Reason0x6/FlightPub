package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Membership")
public class Membership {
    @Id
    @Getter
    @Setter
    private String membershipNo;

    @Getter
    @Setter
    private String airlineID;

    public Membership() {}
}
