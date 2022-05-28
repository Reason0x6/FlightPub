package com.FlightPub.RequestObjects;

import com.FlightPub.Services.FlightServices;
import com.FlightPub.model.Flight;
import com.FlightPub.model.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Component
@SessionScope
public class UserSession {
    @Getter
    UserAccount usr;


    @Getter
    @Setter
    private FlightServices flightServices;
    @Getter
    Map<String, Integer> sessionCart;

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    public UserSession(UserAccount usr){
        this.usr = usr;
        this.sessionCart = new HashMap<>();
    }

    public boolean isLoggedIn(){
        return usr != null ? true : false;
    }

    public String getFirstname(){
        return usr.getFirstname();
    }

    public String getEmail(){
        return usr.getEmail();
    }
    public String getPassword(){
        return usr.getPassword();
    }

    public void addToCart(int numSeats, String flightID){
        if(sessionCart.containsKey(flightID)){
            if (numSeats <= 0 ) {
                removeFromCart(flightID);
                return;
            }
        }
        sessionCart.put(flightID, numSeats);
        System.out.println(flightID + ", " + numSeats);
        return;
    }

    public void removeFromCart(String flightID){
        sessionCart.remove(flightID);
    }

    public int getSeatsFor(String id){
        return sessionCart.get(id);
    }

    public Map<String, Integer> getCart(){

        return sessionCart;
    }


    public Flight getFlight(String id){
        return flightServices.getById(id);
   }
    /*
    for (String key: map.keySet()) {
        System.out.println("key : " + key);
        System.out.println("value : " + map.get(key));
    }

    */


}
