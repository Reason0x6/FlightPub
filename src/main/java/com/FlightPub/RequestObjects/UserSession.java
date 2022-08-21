package com.FlightPub.RequestObjects;

import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.model.Booking;
import com.FlightPub.model.Flight;
import com.FlightPub.model.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SessionScope
public class UserSession {
    @Getter
    UserAccount usr;
    @Getter
    Map<String, Integer> sessionCart;
    @Getter
    @Setter
    private FlightServices flightServices;
    @Getter
    @Setter
    private UserAccountServices usrService;
    @Getter
    @Setter
    private String lastSearchedDestination;

    @Getter
    @Setter
    private List<Flight> lastViewedFlight;

    @Getter
    @Setter
    private List<List<String[]>> firClassSeatList;

    @Getter
    @Setter
    private List<List<String[]>> busClassSeatList;

    @Getter
    @Setter
    private List<List<String[]>> pmeClassSeatList;

    @Getter
    @Setter
    private List<List<String[]>> ecoClassSeatList;

    @Getter
    @Setter
    private List<BookingRequest> cart;

    @Getter
    @Setter
    private List<BookingRequest> checkedOutCart;

    @Getter
    @Setter
    private Booking booking;

    public UserSession(UserAccount usr) {
        this.usr = usr;
        this.sessionCart = new HashMap<>();
    }

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserServices(UserAccountServices usrService) {
        this.usrService = usrService;
    }

    public boolean isLoggedIn() {
        return usr != null;
    }

    public String getFirstname() {
        return usr.getFirstname();
    }

    public String getEmail() {
        return usr.getEmail();
    }

    public String getPassword() {
        return usr.getPassword();
    }

    public void addToCart(BookingRequest bookingRequest) {
        if (cart == null) {
            cart = new ArrayList<>();
        }
        cart.add(bookingRequest);
    }


    public void removeFromCart(String id) {
        for (BookingRequest br : cart) {
            if (br.getId().equals(id)) {
                cart.get(cart.indexOf(br)).setAllSeatsList(null);
                cart.remove(br);
                break;
            }
        }
    }

    public int getSeatsFor(String id) {
        return sessionCart.get(id);
    }

    public boolean addToWishList(String id) {

        Flight tempF = flightServices.getById(id);
        return usrService.addToWishList(tempF.getDestinationCode(), usr.getEmail());

    }

    public void removeFromWishList(String id) {
        usrService.removeWIL(id, usr.getEmail());
    }

    public List<Map.Entry<String, String>> getWishList() {

        return usrService.getWishList(usr);
    }

    public Flight getFlight(String id) {
        return flightServices.getById(id);
    }

    /*
    for (String key: map.keySet()) {
        System.out.println("key : " + key);
        System.out.println("value : " + map.get(key));
    }

    */

    // Setters for single flight data
    public void setBusClassSeatListDirect(List<String[]> in) {
        busClassSeatList = new ArrayList<>();
        busClassSeatList.add(in);
    }

    public void setEcoClassSeatListDirect(List<String[]> in) {
        ecoClassSeatList = new ArrayList<>();
        ecoClassSeatList.add(in);
    }

    public void setFirClassSeatListDirect(List<String[]> in) {
        firClassSeatList = new ArrayList<>();
        firClassSeatList.add(in);
    }

    public void setPmeClassSeatListDirect(List<String[]> in) {
        pmeClassSeatList = new ArrayList<>();
        pmeClassSeatList.add(in);
    }

    public void setLastViewedFlightDirect(Flight in) {
        lastViewedFlight = new ArrayList<>();
        lastViewedFlight.add(in);
    }
}
