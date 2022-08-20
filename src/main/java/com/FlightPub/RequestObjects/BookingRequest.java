package com.FlightPub.RequestObjects;

import com.FlightPub.model.Flight;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingRequest {

    @Getter
    @Setter
    private Flight flight;

    @Getter
    @Setter
    private int userId;

    @Getter
    @Setter
    List<String[]> busSeats = new ArrayList<>();

    @Getter
    @Setter
    List<String[]> ecoSeats = new ArrayList<>();

    @Getter
    @Setter
    List<String[]> firSeats = new ArrayList<>();

    @Getter
    @Setter
    List<String[]> pmeSeats = new ArrayList<>();

    @Getter
    @Setter
    private String firStandard = "0";

    @Getter
    @Setter
    private String busStandard = "0";

    @Getter
    @Setter
    private String pmeStandard = "0";

    @Getter
    @Setter
    private String ecoStandard = "0";

    @Getter
    @Setter
    private String firPlatinum = "0";

    @Getter
    @Setter
    private String busPlatinum = "0";

    @Getter
    @Setter
    private String pmePlatinum = "0";

    @Getter
    @Setter
    private String ecoPlatinum = "0";

    @Getter
    @Setter
    private String firLongDistance = "0";

    @Getter
    @Setter
    private String busLongDistance = "0";

    @Getter
    @Setter
    private String pmeLongDistance = "0";

    @Getter
    @Setter
    private String ecoLongDistance = "0";

    @Getter
    @Setter
    private String firStandby = "0";

    @Getter
    @Setter
    private String busStandby = "0";

    @Getter
    @Setter
    private String pmeStandby = "0";

    @Getter
    @Setter
    private String ecoStandby = "0";

    @Getter
    @Setter
    private String firPremDisc = "0";

    @Getter
    @Setter
    private String busPremDisc = "0";

    @Getter
    @Setter
    private String pmePremDisc = "0";

    @Getter
    @Setter
    private String ecoPremDisc = "0";

    @Getter
    @Setter
    private String firDiscounted = "0";

    @Getter
    @Setter
    private String busDiscounted = "0";

    @Getter
    @Setter
    private String pmeDiscounted = "0";

    @Getter
    @Setter
    private String ecoDiscounted = "0";

    @Getter
    @Setter
    private String firPremium = "0";

    @Getter
    @Setter
    private String busPremium = "0";

    @Getter
    @Setter
    private String pmePremium = "0";

    @Getter
    @Setter
    private String ecoPremium = "0";

    @Getter
    @Setter
    private double price = 0;

    @Getter
    private String id;

    BookingRequest() {
        id = String.valueOf(getTotalSeats() + System.currentTimeMillis());
    }

    public Date getDepartureDate() {
        return Flight.stringToDate(flight.getDepartureString());
    }

    public double getPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(price));
    }

    public int getTotalSeats(){
        int seats = 0;
        if (!firStandard.equals("")){
            seats += Integer.parseInt(firStandard);
        }
        if (!busStandard.equals("")){
            seats += Integer.parseInt(busStandard);
        }
        if (!pmeStandard.equals("")){
            seats += Integer.parseInt(pmeStandard);
        }
        if (!ecoStandard.equals("")){
            seats += Integer.parseInt(ecoStandard);
        }
        if (!firPlatinum.equals("")){
            seats += Integer.parseInt(firPlatinum);
        }
        if (!busPlatinum.equals("")){
            seats += Integer.parseInt(busPlatinum);
        }
        if (!pmePlatinum.equals("")){
            seats += Integer.parseInt(pmePlatinum);
        }
        if (!ecoPlatinum.equals("")){
            seats += Integer.parseInt(ecoPlatinum);
        }
        if (!firLongDistance.equals("")){
            seats += Integer.parseInt(firLongDistance);
        }
        if (!busLongDistance.equals("")){
            seats += Integer.parseInt(busLongDistance);
        }
        if (!pmeLongDistance.equals("")){
            seats += Integer.parseInt(pmeLongDistance);
        }
        if (!ecoLongDistance.equals("")){
            seats += Integer.parseInt(ecoLongDistance);
        }
        if (!firStandby.equals("")){
            seats += Integer.parseInt(firStandby);
        }
        if (!busStandby.equals("")){
            seats += Integer.parseInt(busStandby);
        }
        if (!pmeStandby.equals("")){
            seats += Integer.parseInt(pmeStandby);
        }
        if (!ecoStandby.equals("")){
            seats += Integer.parseInt(ecoStandby);
        }
        if (!firPremDisc.equals("")){
            seats += Integer.parseInt(firPremDisc);
        }
        if (!busPremDisc.equals("")){
            seats += Integer.parseInt(busPremDisc);
        }
        if (!pmePremDisc.equals("")){
            seats += Integer.parseInt(pmePremDisc);
        }
        if (!ecoPremDisc.equals("")){
            seats += Integer.parseInt(ecoPremDisc);
        }
        if (!firDiscounted.equals("")){
            seats += Integer.parseInt(firDiscounted);
        }
        if (!busDiscounted.equals("")){
            seats += Integer.parseInt(busDiscounted);
        }
        if (!pmeDiscounted.equals("")){
            seats += Integer.parseInt(pmeDiscounted);
        }
        if (!ecoDiscounted.equals("")){
            seats += Integer.parseInt(ecoDiscounted);
        }
        if (!firPremium.equals("")){
            seats += Integer.parseInt(firPremium);
        }
        if (!busPremium.equals("")){
            seats += Integer.parseInt(busPremium);
        }
        if (!pmePremium.equals("")){
            seats += Integer.parseInt(pmePremium);
        }
        if (!ecoPremium.equals("")){
            seats += Integer.parseInt(ecoPremium);
        }
        return seats;
    }
}
