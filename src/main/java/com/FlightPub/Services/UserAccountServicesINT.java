package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;

import java.util.List;

public interface UserAccountServicesINT {
    List<UserAccount> listAll();

    UserAccount getById(String id);

    UserAccount saveOrUpdate(UserAccount product);

    void delete(String id);

}
