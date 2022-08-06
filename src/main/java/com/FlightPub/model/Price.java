package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document("Price")
public class Price {
    @Id
    @Setter
    @Getter
    private String ID;

    @Setter
    @Getter
    @Field("AirlineCode")
    private String airlineCode;

    @Setter
    @Getter
    @Field("FlightNumber")
    private String flightNumber;

    @Setter
    @Getter
    @Field("ClassCode")
    private String classCode;

    @Setter
    @Getter
    @Field("TicketCode")
    private String ticketCode;

    @Setter
    @Getter
    @Field("StartDate")
    private Date startDate;

    @Setter
    @Getter
    @Field("EndDate")
    private Date endDate;

    @Setter
    @Getter
    @Field("Price")
    private Double price;

    @Setter
    @Getter
    @Field("PriceLeg1")
    private Double priceLeg1;

    @Setter
    @Getter
    @Field("PriceLeg2")
    private Double priceLeg2;


    public Price() {}

}
