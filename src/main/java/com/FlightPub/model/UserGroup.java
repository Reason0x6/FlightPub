package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;

@Document("UserAccount")
public class UserGroup{
    @Id
    private String id;

    @Getter
    @Setter
    private LinkedList<String> userIDs;

    @Getter
    @Setter
    private String adminID;

    public UserGroup(String adminID) {
        super();
        userIDs = new LinkedList<>();
        userIDs.add(adminID);
        this.adminID = adminID;
    }

    //public String getAdminID(){return adminID;}

    public void addUser(String id){
        if (! userIDs.contains(id)){
            userIDs.add(id);
        }
    }

    public void removeUser(String id){
        //if (userIDs.contains(id)){userIDs.remove(id);}
        userIDs.remove(id);
    }


}
