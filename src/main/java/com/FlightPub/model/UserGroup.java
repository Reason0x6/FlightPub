package com.FlightPub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;

@Document("UserAccount")
public class UserGroup{
    @Id
    private String id;

    private LinkedList<String> userIDs;

    private String adminID;

    public UserGroup(String adminID) {
        super();
        userIDs = new LinkedList<>();
        userIDs.add(adminID);
        this.adminID = adminID;
    }

    public void addUser(String id){
        if (! userIDs.contains(id)){
            userIDs.add(id);
        }
    }


}
