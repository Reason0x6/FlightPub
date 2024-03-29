package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("Traveller")
public class Traveller {

    @Id
    @Getter
    @Field("_id")
    private String id;

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
    private String dob;

    @Getter
    @Setter
    @Field("saveTraveller")
    private boolean saveTraveller;

    @Getter
    @Setter
    @Field("accountEmail")
    private String accountEmail;

    @Getter
    @Setter
    @Field("seat")
    private String seat;

    public Traveller() {
        this.title = "";
        this.firstName = "";
        this.lastName = "";
        this.dob = "";
        this.saveTraveller = false;
        this.accountEmail = "";
        this.seat = "";
    }

    public Traveller(String title, String firstName, String lastName, String dob, boolean saveTraveller, String accountEmail) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.saveTraveller = saveTraveller;
        this.accountEmail = accountEmail;
    }

    public void setTravellerID(ObjectId id) {
        this.id = id.toString();
    }

    public void setTravellerID(String id) {
        this.id = id;
    }

}
