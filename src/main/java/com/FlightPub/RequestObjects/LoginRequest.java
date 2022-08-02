package com.FlightPub.RequestObjects;

import com.FlightPub.Services.SecurityService;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

public class LoginRequest {


    @Getter
    @Setter
    private String email;

    @Getter
    private String password;

    @Getter
    @Setter
    private String redirect;
    private final SecurityService secService;

    LoginRequest() throws NoSuchAlgorithmException {
        secService = new SecurityService();
    }

    public void setPassword(String in){
        this.password = secService.hash(in);
    }



}
