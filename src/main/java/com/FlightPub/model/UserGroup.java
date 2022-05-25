package com.FlightPub.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.UUID;

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

    public UserGroup(String adminID, String groupName) {
        this.adminID = adminID;
        this.groupName = groupName;

        id = NanoIdUtils.randomNanoId();

        userIDs = new LinkedList<>();
        userIDs.add(adminID);

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
