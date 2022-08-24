package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("Airlines")
public class Airlines {

    @Id
    @Getter
    @Setter
    private String airlineID;

    @Getter
    @Setter
    @Field("AirlineName")
    private String airlineName;

    @Getter
    @Setter
    @Field("CountryCode3")
    private String countryCode;

    @Getter
    @Setter
    @Field("Sponsored")
    private boolean sponsored = false;

    public Airlines() {
    }
}
