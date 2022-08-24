package com.FlightPub.RequestObjects;

import com.FlightPub.Services.SecurityService;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

/**
 * Simple Java Object used to store admin registration information
 */
public class AdminRegister {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String company;

    @Getter
    private String password;

    @Getter
    private String confirmPassword;

    @Getter
    @Setter
    private String registrationcode;

    private final SecurityService secService;

    AdminRegister() throws NoSuchAlgorithmException {
        secService = new SecurityService();
    }

    public void setPassword(String in) {
        this.password = secService.hash(in);
    }


    public void setConfirmpassword(String in) {
        this.confirmPassword = secService.hash(in);
    }

    public boolean allFilled() {
        return this.email != null && this.firstName != null && this.lastName != null && this.company != null;
    }

    public boolean isValid() {
        return this.confirmPassword.equals(this.password) && allFilled();
    }
}
