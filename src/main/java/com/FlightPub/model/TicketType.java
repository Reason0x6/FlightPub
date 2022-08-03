package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("TicketType")
public class TicketType {
    @Id
    @Setter
    @Getter
    private String ticketCode;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private boolean refundable;

    @Setter
    @Getter
    private boolean transferable;

    @Setter
    @Getter
    private boolean exchangable;

    @Setter
    @Getter
    private boolean FrequentFlyerPoints;

    public TicketType() {}

    public TicketType(String ticketCode, String name, boolean refundable, boolean transferable, boolean exchangable, boolean FrequentFlyerPoints){

       this.ticketCode = ticketCode;
       this.name = name;
       this.refundable = refundable;
       this.transferable = transferable;
       this.exchangable = exchangable;
       this.FrequentFlyerPoints = FrequentFlyerPoints;
    }
}
