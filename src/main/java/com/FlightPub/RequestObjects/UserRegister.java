package com.FlightPub.RequestObjects;

import com.FlightPub.Services.SecurityService;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

/**
 * Register User request object
 */
public class UserRegister {
    @Getter
    @Setter
    private String firstname;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String prefAirport;

    @Getter
    private String password;

    @Getter
    private String confirmpassword;

    private final SecurityService secService;

    UserRegister() throws NoSuchAlgorithmException {
        secService = new SecurityService();
    }

    public void setPassword(String in) {
        this.password = secService.hash(in);
    }


    public void setConfirmpassword(String in) {
        this.confirmpassword = secService.hash(in);
    }

    public boolean allFilled() {
        return email != null && firstname != null && address != null && prefAirport != null;
    }

    public boolean isValid() {
        return confirmpassword.equals(password) && allFilled();
    }


}
