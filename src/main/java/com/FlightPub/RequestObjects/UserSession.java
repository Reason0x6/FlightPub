package com.FlightPub.RequestObjects;

import com.FlightPub.model.UserAccount;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {
    @Getter
    UserAccount usr;

    public UserSession(UserAccount usr){

        this.usr = usr;

    }

    public boolean isLoggedIn(){
        return usr != null ? true : false;
    }

    public String getFirstname(){
        return usr.getFirstname();
    }

}
