package com.FlightPub.model;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document("WishList")
public class WishListItem {
    @Id
    @Setter
    @Getter
    private String WLID;

    @Setter
    @Getter
    @Field("userID")
    private String userID;

    @Getter
    @Setter
    @Field("destinationID")
    private String destinationID;

    public WishListItem() {}

    public WishListItem(String WLID, String userID, String destinationID) {

        this.WLID = WLID;
        this.userID = userID;
        this.destinationID = destinationID;
    }


}
