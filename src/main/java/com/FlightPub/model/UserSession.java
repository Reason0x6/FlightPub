package com.FlightPub.model;

import lombok.Getter;

public class UserSession {
    @Getter
    UserAccount usr;

    public UserSession(UserAccount usr){

        this.usr = usr;

    }

    public boolean isLoggedIn(){
        return usr != null ? true : false;
    }

    public String getUsername(){
        return usr.getUsername();
    }

}
