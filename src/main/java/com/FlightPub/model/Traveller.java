package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

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
    @Field("seat")
    private String seat;

    public Traveller() {
        this.title = "";
        this.firstName = "";
        this.lastName = "";
        this.dob = "";
        this.saveTraveller = false;
    }

    public Traveller(String title, String firstName, String lastName, String dob, boolean saveTraveller) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.saveTraveller = saveTraveller;
    }

    public void setTravellerID(ObjectId id){
        this.id = id.toString();
    }

    public void setTravellerID(String id){
        this.id = id;
    }

}
