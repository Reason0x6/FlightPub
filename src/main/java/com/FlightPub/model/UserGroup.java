package com.FlightPub.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.LinkedList;

/**
 * Java Object Representation of Database Object
 */
@Document("Group")
public class UserGroup {
    @Id
    @Getter
    private String id;

    @Getter
    @Setter
    @Field("userIDs")
    private LinkedList<String> userIDs;

    @Getter
    @Setter
    @Field("adminID")
    private String adminID;

    @Getter
    @Setter
    @Field("groupName")
    private String groupName;

    @Getter
    @Setter
    @Field("invitedIds")
    private LinkedList<String> invitedIds;

    @Getter
    @Setter
    private LinkedList<String> declinedIds;

    @Getter
    @Setter
    private String flight;

    public UserGroup(String adminID, String groupName) {
        this.adminID = adminID;
        this.groupName = groupName;

        id = NanoIdUtils.randomNanoId();

        userIDs = new LinkedList<>();
        userIDs.add(adminID);

        invitedIds = new LinkedList<>();
        declinedIds = new LinkedList<>();
    }

    public void addUser(String id) {
        if (!userIDs.contains(id)) {
            userIDs.add(id);
        }
    }

    public void removeUser(String id) {
        userIDs.remove(id);
    }

    public void addInvite(String id) {
        if (!invitedIds.contains(id)) {
            invitedIds.add(id);
        }
    }

    public void addDecline(String id) {
        if (!declinedIds.contains(id)) {
            declinedIds.add(id);
        }
    }

    public void removeInvite(String id) {
        invitedIds.remove(id);
    }

    public void addFlight(String flightId) {
        flight = flightId;
    }
}
