package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("Traveller")
public class Traveller {

    @Getter
    @Setter
    @Field("title")
    private String title;

    @Getter
    @Setter
    @Field("firstName")
    private String firstName;

    @Getter
    @Setter
    @Field("lastName")
    private String lastName;

    @Getter
    @Setter
    @Field("dob")
    private Date dob;

    @Getter
    @Setter
    @Field("saveTraveller")
    private boolean saveTraveller;

    @Getter
    @Setter
    @Field("seat")
    private String seat;

    public Traveller() {
        this.title = "";
        this.firstName = "";
        this.lastName = "";
        this.dob = new Date();
        this.saveTraveller = false;
    }

    public Traveller(String title, String firstname, String lastname, Date dob, boolean saveTraveller, String seat) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.saveTraveller = saveTraveller;
        this.seat = seat;
    }

}
