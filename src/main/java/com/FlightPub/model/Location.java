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
    private String location;

    @Getter
    @Setter
    private String country;

    @Getter
    @Setter
    private double latitude;

    @Getter
    @Setter
    private double longitude;

    @Getter
    @Setter
    private int popularity;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private boolean covid_restricted;

    @Getter
    @Setter
    private LinkedList<String> tags;

    public Location() {}

    public Location(String id, String country, String location, double lat, double lng, int pop, boolean covid_restricted) {
        this.locationID = id.toUpperCase();
        this.country = country;
        this.location = location;
        this.latitude = lat;
        this.longitude = lng;
        this.popularity = pop;
        this.covid_restricted = covid_restricted;
    }
}
