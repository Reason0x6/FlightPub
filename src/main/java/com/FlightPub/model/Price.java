package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Java Object Representation of Database Object
 */
@Document("Price")
public class Price {
    @Id
    @Setter
    @Getter
    @Field("_id")
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
    private String classCode = "";

    @Setter
    @Getter
    @Field("TicketCode")
    private String ticketCode = "";

    @Setter
    @Getter
    @Field("StartDate")
    private Long startDate;

    @Setter
    @Getter
    @Field("EndDate")
    private Long endDate;

    @Setter
    @Getter
    @Field("Price")
    private Double price;

    @Setter
    @Getter
    @Field("PriceLeg1")
    private Double priceLeg1 = 0.0;

    @Setter
    @Getter
    @Field("PriceLeg2")
    private Double priceLeg2 = 0.0;


    public Price() {
    }

    public Price(String classCode, String ticketCode) {
        this.classCode = classCode;
        this.ticketCode = ticketCode;
    }

    // Conversions between various time formats
    public static String longToString(Long in) {
        try {
            Date date = new Date(in.longValue());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return format.format(date);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Long stringToLong(String in) {
        try {
            SimpleDateFormat format;
            if (in.length() == 10)
                format = new SimpleDateFormat("yyyy-MM-dd");
            else
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = format.parse(in);
            return date.getTime();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // Getters and Setters for the Thymeleaf template
    public String getStartString() {
        return longToString(this.startDate);
    }

    public String getEndString() {
        return longToString(this.endDate);
    }

    public void setStartDate(String startDate) {
        this.startDate = stringToLong(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = stringToLong(endDate);
    }

}
