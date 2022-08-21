package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Java Object Representation of Database Object
 */
@Document("HolidayPackage")
public class HolidayPackage {
    @Getter
    @Setter
    private String airlineCode;

    @Getter
    @Setter
    private String airlineName;

    @Getter
    @Setter
    private String destinationCode;

    @Getter
    @Setter
    private String airport;

    @Getter
    @Setter
    private int discountPercentage;

    @Getter
    @Setter
    private String hotelName;

    @Getter
    @Setter
    private int hotelStarRating;

    @Getter
    @Setter
    private String hotelDescription;

    @Getter
    @Setter
    private Date packageStartDate;

    @Getter
    @Setter
    private Date packageEndDate;

    @Getter
    @Setter
    private String packageStartDateFormatted;

    @Getter
    @Setter
    private String packageEndDateFormatted;

    public HolidayPackage(String airlineCode, String airlineName, String destinationCode, String airport, int discountPercentage, String hotelName,
                          int hotelStarRating, String hotelDescription, Date packageStartDate, Date packageEndDate) {
        super();
        this.airlineCode = airlineCode;
        this.airlineName = airlineName;
        this.destinationCode = destinationCode;
        this.airport = airport;
        this.discountPercentage = discountPercentage;
        this.hotelName = hotelName;
        this.hotelStarRating = hotelStarRating;
        this.hotelDescription = hotelDescription;
        this.packageStartDate = packageStartDate;
        this.packageEndDate = packageEndDate;
    }

    public HolidayPackage() {
    }
}
