package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Seat")
public class Seat {
    @Id
    @Setter
    @Getter
    private String seatID;

    @Setter
    @Getter
    private String passengerName;

    @Setter
    @Getter
    private String seatClass;

    public Seat() {}
}
