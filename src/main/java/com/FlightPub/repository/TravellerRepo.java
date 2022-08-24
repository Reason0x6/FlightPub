package com.FlightPub.repository;

import com.FlightPub.model.Traveller;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


/**
 * Implements database queries for travellers
 */public interface TravellerRepo extends MongoRepository<Traveller, String> {

    @Query(value = "{ 'travellerID' :?0 }", fields = "{ 'title' : 1, 'firstName' : 1, 'lastName' : 1 }")
    Traveller findByTraveller(String in);

}
