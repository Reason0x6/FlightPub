package com.FlightPub.model;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("WishListItem")
public class WishListItem {
    @Id
    @Setter
    @Getter
    private String WLID;

    @Setter
    @Getter
    private String userID;

    @Getter
    @Setter
    private String destinationID;

    public WishListItem() {}

    public WishListItem(String WLID, String userID, String destinationID) {

        this.WLID = WLID;
        this.userID = userID;
        this.destinationID = destinationID;
    }


}
