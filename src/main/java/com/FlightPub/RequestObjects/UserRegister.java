package com.FlightPub.RequestObjects;

import com.FlightPub.Services.SecurityService;
import com.FlightPub.model.UserAccount;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

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
    private String password;

    @Getter
    private String confirmpassword;

    private SecurityService secService;

    UserRegister() throws NoSuchAlgorithmException {
        secService = new SecurityService();
    }

    public void setPassword(String in){
        this.password = secService.hash(in);
    }


    public void setConfirmpassword(String in){
        this.confirmpassword = secService.hash(in);
    }

    public boolean allFilled(){
        if(email != null && firstname != null&& address != null){
            return true;
        }
        return false;
    }

    public boolean isValid(){
        if(confirmpassword.equals(password) && allFilled()){
            return true;
        }
        return false;
    }


}
