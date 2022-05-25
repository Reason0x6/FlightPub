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
    private String airlineID;

    @Getter
    @Setter
    private int seatCount;

    @Getter
    @Setter
    private String planeType;

    @Getter
    @Setter
    private List<String> seats = new ArrayList<>();

    String[] col = {"A", "B", "C", "D"};

    public Plane() {
       for(int i = 0; i <= 28; i++){
           String s = (col[i % 4]) + (i+1);
           seats.add(s);
       }
    }
}
