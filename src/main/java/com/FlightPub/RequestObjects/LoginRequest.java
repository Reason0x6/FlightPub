package com.FlightPub.RequestObjects;

import com.FlightPub.Services.SecurityService;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

/**
 * Login Request request object
 */
public class LoginRequest {

    private final SecurityService secService;
    @Getter
    @Setter
    private String email;
    @Getter
    private String password;
    @Getter
    @Setter
    private String redirect;

    LoginRequest() throws NoSuchAlgorithmException {
        secService = new SecurityService();
    }

    public void setPassword(String in) {
        this.password = secService.hash(in);
    }


}
