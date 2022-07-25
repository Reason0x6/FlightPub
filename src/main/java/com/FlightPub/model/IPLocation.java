package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("IPLocation")
public class IPLocation {
    @Id
    @Getter
    @Setter
    private String IPAddress;

    @Getter
    @Setter
    private String countryCode;


    @Getter
    @Setter
    private String country;

    @Getter
    @Setter
    private String UtcOffset;

    @Getter
    @Setter
    private String State;

    @Getter
    @Setter
    private String City;

    @Getter
    @Setter
    private String Zip;

    @Getter
    @Setter
    private String latitude;

    @Getter
    @Setter
    private String longitude;



    public IPLocation() {}

    public IPLocation(String IPAddress, String countryCode, String country, String UtcOffset, String State, String City, String Zip, String lat, String lng) {
        this.IPAddress = IPAddress.toUpperCase();
        this.countryCode = countryCode;
        this.country = country;
        this.State = State;
        this.UtcOffset = UtcOffset;
        this.City = City;
        this.Zip = Zip;
        this.latitude = lat;
        this.longitude = lng;
    }
}
