package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Java Object Representation of Database Object
 */
@Document("TicketType")
public class TicketType {
    @Id
    @Setter
    @Getter
    private String ticketCode;

    @Setter
    @Getter
    @Field("Name")
    private String name;

    @Setter
    @Getter
    @Field("Refundable")
    private boolean refundable;

    @Setter
    @Getter
    @Field("Transferable")
    private boolean transferable;

    @Setter
    @Getter
    @Field("Exchangable")
    private boolean exchangable;

    @Setter
    @Getter
    @Field("FrequentFlyerPoints")
    private boolean FrequentFlyerPoints;

    public TicketType() {
    }

    public TicketType(String ticketCode, String name, boolean refundable, boolean transferable, boolean exchangable, boolean FrequentFlyerPoints) {

        this.ticketCode = ticketCode;
        this.name = name;
        this.refundable = refundable;
        this.transferable = transferable;
        this.exchangable = exchangable;
        this.FrequentFlyerPoints = FrequentFlyerPoints;
    }
}
