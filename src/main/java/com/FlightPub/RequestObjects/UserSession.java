package com.FlightPub.RequestObjects;

import com.FlightPub.model.UserAccount;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@SessionScope
public class UserSession {
    @Getter
    UserAccount usr;

    @Getter
    LinkedList<String> sessionCart;
    // LinkedList<Map<String, Integer>> sessionCart;

    public UserSession(UserAccount usr){
        this.usr = usr;
        this.sessionCart = new LinkedList<>();
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
        sessionCart.add(numSeats, flightID);
        // sessionCart.add(Map<flightID, numSeats>);
    }

    public void removeFromCart(String flightID){
        sessionCart.remove();
    }
}
