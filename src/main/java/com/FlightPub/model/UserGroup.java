package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;

@Document("Group")
public class UserGroup{
    @Id
    @Getter
    private String id;

    @Getter
    @Setter
    private LinkedList<String> userIDs;

    @Getter
    @Setter
    private String adminID;

    @Getter
    @Setter
    private String groupName;

    public UserGroup() {}

    public UserGroup(String adminID, String groupName) {
        super();
        userIDs = new LinkedList<>();
        userIDs.add(adminID);
        this.adminID = adminID;
        this.groupName = groupName;
    }

    public void addUser(String id){
        if (! userIDs.contains(id)){
            userIDs.add(id);
        }
    }

    public void removeUser(String id){
        userIDs.remove(id);
    }
}
