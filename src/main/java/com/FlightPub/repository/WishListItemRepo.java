package com.FlightPub.repository;
import com.FlightPub.model.WishListItem;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WishListItemRepo  extends MongoRepository<WishListItem, String> {

    @Query(value="{ 'userID' : ?0 }")
    List<WishListItem> findAllByUserIDs(String userIDs);

    @Aggregation(pipeline = {"{ '$group': { '_id' : '$destinationID', count : { '$sum' : 1 } } }", "{ '$sort' : { 'count' : -1 } }"})
    List<WishListItem> findAllByPopularitySortDesc();

    @Query(value="{ 'userID' : ?0,  'destinationID' :  ?1}")
    List<WishListItem> findIfExist(String userID, String Location);

}
