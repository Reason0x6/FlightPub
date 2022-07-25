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
    private String UtcOffset;

    @Getter
    @Setter
    private String State;

    @Getter
    @Setter
    private String City;

    @Getter
    @Setter
    private int Zip;

    @Getter
    @Setter
    private double latitude;

    @Getter
    @Setter
    private double longitude;



    public IPLocation() {}

    public IPLocation(String IPAddress, String countryCode, String UtcOffset, String State, String City, int Zip, double lat, double lng) {
        this.IPAddress = IPAddress.toUpperCase();
        this.countryCode = countryCode;
        this.State = State;
        this.UtcOffset = UtcOffset;
        this.City = City;
        this.Zip = Zip;
        this.latitude = lat;
        this.longitude = lng;
    }
}
