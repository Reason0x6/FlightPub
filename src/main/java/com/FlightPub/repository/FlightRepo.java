package com.FlightPub.repository;


import com.FlightPub.model.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FlightRepo extends MongoRepository<Flight, String> {

    @Query(value="{ 'destinationID' : ?0 }", fields="{ 'destination' : 1 }")
    List<Flight> findByDesitination(String dest);

    @Query(value="{ 'originID' : ?0 }", fields="{ 'originID' : 1 }")
    List<Flight> findByOrigin(String origin);

    @Query(value="{ 'originID' : ?0, 'destinationID': ?1 }")
    List<Flight> findByOriginAndDesitination(String origin, String dest);
}
