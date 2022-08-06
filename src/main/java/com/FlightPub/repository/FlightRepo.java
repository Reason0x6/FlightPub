package com.FlightPub.repository;


import com.FlightPub.model.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface FlightRepo extends MongoRepository<Flight, String> {

    @Query(value="{ 'DestinationCode' : { '$regex' : ?0 , $options: 'i'} }", fields="{ 'destination' : 1 }")
    List<Flight> findByDestination(String dest);

    @Query(value="{ 'DepartureCode' : { '$regex' : ?0 , $options: 'i'} }", fields="{ 'getDepartureCode' : 1 }")
    List<Flight> findByOrigin(String origin);

    @Query(value="{ 'DepartureCode' : { '$regex' : ?0 , $options: 'i'}, 'DestinationCode' : { '$regex' : ?1 , $options: 'i'}, 'DepartureTime' :{$gte: ?2, $lte: ?3} }")
    List<Flight> findByOriginAndDestination(String origin, String dest, Date start, Date end);

    @Query(value="{ 'DepartureCode' : { '$regex' : ?0 , $options: 'i'}, 'DepartureTime':{$gte: ?1, $lte: ?2} }")
    List<Flight> findByOrigin(String origin, Date start, Date end);

    @Query(value="{ 'DepartureCode' : { '$regex' : ?0 , $options: 'i'}, 'DestinationCode': { '$regex' : ?1 , $options: 'i'}, 'ArrivalTime':{$gte: ?2, $lte: ?3} }")
    List<Flight> findByOriginAndDestinationAndArrivalTimes(String origin, String dest, Date start, Date end);

    @Query(value="{ 'DepartureCode' : { '$regex' : ?0 , $options: 'i'}, 'ArrivalTime':{$gte: ?1, $lte: ?2} }")
    List<Flight> findByOriginAndArrivalTimes(String origin, Date start, Date end);

    @Query(value="{ '_id' : ?0 }")
    List<Flight> getByID(String id);
}