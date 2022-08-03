package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Document("Plane")
public class Plane {
    @Id
    @Getter
    @Setter
    private String planeID;

    @Getter
    @Setter
    private String Details;

    @Getter
    @Setter
    private int NumFirstClass;

    @Getter
    @Setter
    private int NumBusiness;

    @Getter
    @Setter
    private int NumPremiumEconomy;

    @Getter
    @Setter
    private int Economy;


    public boolean book(int seats, String ticket){
        switch (ticket) {
            case "BUS":
                this.NumBusiness -= seats;
                break;

            case "ECO":
                this.Economy -= seats;
                break;

            case "FIR":
                this.NumFirstClass -= seats;
                break;

            case "PME":
                this.NumPremiumEconomy -= seats;
                break;

            default:
                return false;
        }
        return true;
    }

}
