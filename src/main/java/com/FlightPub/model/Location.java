package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.LinkedList;

@Document("Location")
public class Location {
    @Id
    @Getter
    @Setter
    private String locationID;

    @Getter
    @Setter
    @Field("location")
    private String location;

    @Getter
    @Setter
    @Field("country")
    private String country;

    @Getter
    @Setter
    @Field("latitude")
    private double latitude;

    @Getter
    @Setter
    @Field("longitude")
    private double longitude;

    @Getter
    @Setter
    @Field("popularity")
    private int popularity;

    @Getter
    @Setter
    @Field("description")
    private String description;

    @Getter
    @Setter
    @Field("adjacentLocations")
    private ArrayList<String> adjacentLocations;

    @Getter
    @Setter
    @Field("covid_restricted")
    private boolean covid_restricted;

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
