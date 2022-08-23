package com.FlightPub.RequestObjects;

import com.FlightPub.model.AdminAccount;
import lombok.Getter;

public class AdminSession {

    @Getter
    AdminAccount admin;

    public AdminSession(AdminAccount admin) {
        this.admin = admin;
    }

    public boolean isLoggedIn() {
        return admin != null;
    }

    public String getEmail() {
        return admin.getEmail();
    }

    public String getFirstName() {
        return admin.getFirstName();
    }

    public String getLastName() {
        return admin.getLastName();
    }

    public String getCompany() {
        return admin.getCompany();
    }

    public String getPassword() {
        return admin.getPassword();
    }
}
