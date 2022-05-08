package com.FlightPub.model;

import com.FlightPub.Services.SecurityService;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class LoginRequest {


    @Getter
    @Setter
    private String email;


    @Getter
    private String password;

    private SecurityService secService;

    LoginRequest() throws NoSuchAlgorithmException {
        secService = new SecurityService();
    }

    public void setPassword(String in){
        this.password = secService.hash(in);
    }

}
