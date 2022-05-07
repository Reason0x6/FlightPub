package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;

@Document("Country")
public class Country {
    @Id
    @Getter
    @Setter
    private String countryName;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private LinkedList<String> tags;
}
