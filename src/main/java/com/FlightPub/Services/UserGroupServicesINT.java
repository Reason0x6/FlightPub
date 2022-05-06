package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;

import java.util.List;

public interface UserGroupServicesINT {

    List<UserAccount> listAllUsers();

    UserAccount getAdmin(String id);

    void saveUsers();

    void addUser(String id);

    void removeUser(String id);
}
