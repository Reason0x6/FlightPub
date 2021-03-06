package com.FlightPub.repository;
import com.FlightPub.model.WishListItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WishListItemRepo  extends MongoRepository<WishListItem, String> {

    @Query(value="{ 'UserIDs' : ?0 }")
    List<WishListItem> findAllByUserIDs(String userIDs);
}
