package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;

@Document("Location")
public class Location {
    @Id
    @Getter
    @Setter
    private String locationID;

    @Getter
    @Setter
    private String country;

    @Getter
    @Setter
    private String locationName;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private LinkedList<String> tags;
}
