package com.FlightPub.repository;


import com.FlightPub.model.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface FlightRepo extends MongoRepository<Flight, String> {

    @Query(value="{ 'destinationID' : ?0 }", fields="{ 'destination' : 1 }")
    List<Flight> findByDestination(String dest);

    @Query(value="{ 'originID' : ?0 }", fields="{ 'originID' : 1 }")
    List<Flight> findByOrigin(String origin);

    @Query(value="{ 'originID' : ?0, 'destinationID': ?1, 'departure':{$gte: ?2, $lte: ?3} }")
    List<Flight> findByOriginAndDesitination(String origin, String dest, Date start, Date end);

    @Query(value="{ 'originID' : ?0, 'departure':{$gte: ?1, $lte: ?2} }")
    List<Flight> findByOrigin(String origin, Date start, Date end);

    @Query(value="{ 'originID' : ?0, 'destinationID': ?1, 'arrival':{$gte: ?2, $lte: ?3} }")
    List<Flight> findByOriginAndDesitinationAndArrivalTimes(String origin, String dest, Date start, Date end);

    @Query(value="{ 'originID' : ?0, 'arrival':{$gte: ?1, $lte: ?2} }")
    List<Flight> findByOriginAndArrivalTimes(String origin, Date start, Date end);
}