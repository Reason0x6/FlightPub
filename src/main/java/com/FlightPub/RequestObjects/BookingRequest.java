package com.FlightPub.RequestObjects;

import com.FlightPub.model.Flight;
import com.FlightPub.model.Traveller;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Booking Request request object
 */
public class BookingRequest {

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
    List<String> allSeatsList = new ArrayList<>();
    @Getter
    @Setter
    private Flight flight;

    @Getter
    @Setter
    private int userId;

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
    private final String id;


    @Getter
    private List<Traveller> traveller = new ArrayList<>();

    public void addTraveller(Traveller t){
        traveller.add(t);
    }

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

    public int getTotalSeats() {
        int seats = 0;
        allSeatsList = new ArrayList<>();
        if (!firStandard.equals("")) {
            seats += Integer.parseInt(firStandard);
            String seatSelected = "First Class - Standard";
            if (!firStandard.equals("0")) {
                for (int i = 0; i < Integer.parseInt(firStandard); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!busStandard.equals("")) {
            seats += Integer.parseInt(busStandard);
            String seatSelected = "Business - Standard";
            if (!busStandard.equals("0")) {
                for (int i = 0; i < Integer.parseInt(busStandard); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!pmeStandard.equals("")) {
            seats += Integer.parseInt(pmeStandard);
            String seatSelected = "Premium Economy - Standard";
            if (!pmeStandard.equals("0")) {
                for (int i = 0; i < Integer.parseInt(pmeStandard); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!ecoStandard.equals("")) {
            seats += Integer.parseInt(ecoStandard);
            String seatSelected = "Economy - Standard";
            if (!ecoStandard.equals("0")) {
                for (int i = 0; i < Integer.parseInt(ecoStandard); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!firPlatinum.equals("")) {
            seats += Integer.parseInt(firPlatinum);
            String seatSelected = "First Class - Platinum";
            if (!firPlatinum.equals("0")) {
                for (int i = 0; i < Integer.parseInt(firPlatinum); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!busPlatinum.equals("")) {
            seats += Integer.parseInt(busPlatinum);
            String seatSelected = "Business - Platinum";
            if (!busPlatinum.equals("0")) {
                for (int i = 0; i < Integer.parseInt(busPlatinum); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!pmePlatinum.equals("")) {
            seats += Integer.parseInt(pmePlatinum);
            String seatSelected = "Premium Economy - Platinum";
            if (!pmePlatinum.equals("0")) {
                for (int i = 0; i < Integer.parseInt(pmePlatinum); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!ecoPlatinum.equals("")) {
            seats += Integer.parseInt(ecoPlatinum);
            String seatSelected = "Economy - Platinum";
            if (!ecoPlatinum.equals("0")) {
                for (int i = 0; i < Integer.parseInt(ecoPlatinum); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!firLongDistance.equals("")) {
            seats += Integer.parseInt(firLongDistance);
            String seatSelected = "First Class - Long Distance";
            if (!firLongDistance.equals("0")) {
                for (int i = 0; i < Integer.parseInt(firLongDistance); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!busLongDistance.equals("")) {
            seats += Integer.parseInt(busLongDistance);
            String seatSelected = "Business - Long Distance";
            if (!busLongDistance.equals("0")) {
                for (int i = 0; i < Integer.parseInt(busLongDistance); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!pmeLongDistance.equals("")) {
            seats += Integer.parseInt(pmeLongDistance);
            String seatSelected = "Premium Economy - Long Distance";
            if (!pmeLongDistance.equals("0")) {
                for (int i = 0; i < Integer.parseInt(pmeLongDistance); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!ecoLongDistance.equals("")) {
            seats += Integer.parseInt(ecoLongDistance);
            String seatSelected = "Economy - Long Distance";
            if (!ecoLongDistance.equals("0")) {
                for (int i = 0; i < Integer.parseInt(ecoLongDistance); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!firStandby.equals("")) {
            seats += Integer.parseInt(firStandby);
            String seatSelected = "First Class - Standby";
            if (!firStandby.equals("0")) {
                for (int i = 0; i < Integer.parseInt(firStandby); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!busStandby.equals("")) {
            seats += Integer.parseInt(busStandby);
            String seatSelected = "Business - Standby";
            if (!busStandby.equals("0")) {
                for (int i = 0; i < Integer.parseInt(busStandby); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!pmeStandby.equals("")) {
            seats += Integer.parseInt(pmeStandby);
            String seatSelected = "Premium Economy - Standby";
            if (!pmeStandby.equals("0")) {
                for (int i = 0; i < Integer.parseInt(pmeStandby); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!ecoStandby.equals("")) {
            seats += Integer.parseInt(ecoStandby);
            String seatSelected = "Economy - Standby";
            if (!ecoStandby.equals("0")) {
                for (int i = 0; i < Integer.parseInt(ecoStandby); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!firPremDisc.equals("")) {
            seats += Integer.parseInt(firPremDisc);
            String seatSelected = "First Class - Premium Discount";
            if (!firPremDisc.equals("0")) {
                for (int i = 0; i < Integer.parseInt(firPremDisc); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!busPremDisc.equals("")) {
            seats += Integer.parseInt(busPremDisc);
            String seatSelected = "Business - Premium Discount";
            if (!busPremDisc.equals("0")) {
                for (int i = 0; i < Integer.parseInt(busPremDisc); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!pmePremDisc.equals("")) {
            seats += Integer.parseInt(pmePremDisc);
            String seatSelected = "Premium Economy - Premium Discount";
            if (!pmePremDisc.equals("0")) {
                for (int i = 0; i < Integer.parseInt(pmePremDisc); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!ecoPremDisc.equals("")) {
            seats += Integer.parseInt(ecoPremDisc);
            String seatSelected = "Economy - Premium Discount";
            if (!ecoPremDisc.equals("0")) {
                for (int i = 0; i < Integer.parseInt(ecoPremDisc); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!firDiscounted.equals("")) {
            seats += Integer.parseInt(firDiscounted);
            String seatSelected = "First Class - Discounted";
            if (!firDiscounted.equals("0")) {
                for (int i = 0; i < Integer.parseInt(firDiscounted); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!busDiscounted.equals("")) {
            seats += Integer.parseInt(busDiscounted);
            String seatSelected = "Business - Discounted";
            if (!busDiscounted.equals("0")) {
                for (int i = 0; i < Integer.parseInt(busDiscounted); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!pmeDiscounted.equals("")) {
            seats += Integer.parseInt(pmeDiscounted);
            String seatSelected = "Premium Economy - Discounted";
            if (!pmeDiscounted.equals("0")) {
                for (int i = 0; i < Integer.parseInt(pmeDiscounted); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!ecoDiscounted.equals("")) {
            seats += Integer.parseInt(ecoDiscounted);
            String seatSelected = "Economy - Discounted";
            if (!ecoDiscounted.equals("0")) {
                for (int i = 0; i < Integer.parseInt(ecoDiscounted); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!firPremium.equals("")) {
            seats += Integer.parseInt(firPremium);
            String seatSelected = "First Class - Premium";
            if (!firPremium.equals("0")) {
                for (int i = 0; i < Integer.parseInt(firPremium); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!busPremium.equals("")) {
            seats += Integer.parseInt(busPremium);
            String seatSelected = "Business - Premium";
            if (!busPremium.equals("0")) {
                for (int i = 0; i < Integer.parseInt(busPremium); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!pmePremium.equals("")) {
            seats += Integer.parseInt(pmePremium);
            String seatSelected = "Premium Economy - Premium";
            if (!pmePremium.equals("0")) {
                for (int i = 0; i < Integer.parseInt(pmePremium); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        if (!ecoPremium.equals("")) {
            seats += Integer.parseInt(ecoPremium);
            String seatSelected = "Economy - Premium";
            if (!ecoPremium.equals("0")) {
                for (int i = 0; i < Integer.parseInt(ecoPremium); i++) {
                    allSeatsList.add(seatSelected);
                }
            }
        }
        return seats;
    }
}
