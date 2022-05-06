package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;
import com.FlightPub.model.UserGroup;

import java.util.LinkedList;
import java.util.List;

public interface UserGroupServicesINT {

    LinkedList<UserAccount> listAllUsers();

    UserAccount getAdmin(String id);

    void saveUsers(UserGroup usrGroupObj);

    void addUser(String id);

    void removeUser(String id);

    void loadUserGroup(String id);
}
