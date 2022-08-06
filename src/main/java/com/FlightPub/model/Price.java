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
    private String AirlineCode;

    @Setter
    @Getter
    @Field("FlightNumber")
    private String FlightNumber;

    @Setter
    @Getter
    @Field("ClassCode")
    private String ClassCode;

    @Setter
    @Getter
    @Field("TicketCode")
    private String TicketCode;

    @Setter
    @Getter
    @Field("StartDate")
    private Date StartDate;

    @Setter
    @Getter
    @Field("EndDate")
    private Date EndDate;

    @Setter
    @Getter
    @Field("Price")
    private Double Price;

    @Setter
    @Getter
    @Field("PriceLeg1")
    private Double PriceLeg1;

    @Setter
    @Getter
    @Field("PriceLeg2")
    private Double PriceLeg2;


    public Price() {}

}
