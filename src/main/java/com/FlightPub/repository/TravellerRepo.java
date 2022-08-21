package com.FlightPub.repository;

import com.FlightPub.model.Traveller;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TravellerRepo extends MongoRepository<Traveller, String> {

    @Query(value="{ 'travellerID' :?0 }")
    List<Traveller> findByTraveller(String in);

}
