package com.FlightPub.RequestObjects;

import com.FlightPub.model.UserAccount;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@SessionScope
public class UserSession {
    @Getter
    UserAccount usr;

    @Getter
    Map<String, Integer> sessionCart;

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

    public void addToCart(String flightID, int numSeats){
        sessionCart.put(flightID, numSeats);
    }

    public void removeFromCart(String flightID){
        sessionCart.remove(flightID);
    }

    public int getSeatsFor(String id){
        return sessionCart.get(id);
    }

    /*
    for (String key: map.keySet()) {
        System.out.println("key : " + key);
        System.out.println("value : " + map.get(key));
    }

    */


}
