package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Java Object Representation of Database Object
 */
@Document("TicketClass")
public class TicketClass {
    @Id
    @Setter
    @Getter
    private String ticketClass;

    @Setter
    @Getter
    @Field("Details")
    private String details;


    public TicketClass() {
    }

    public TicketClass(String id, String details) {
        this.ticketClass = id;
        this.details = details;
    }
}
