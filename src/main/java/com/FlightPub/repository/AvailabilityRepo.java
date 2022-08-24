package com.FlightPub.repository;

import com.FlightPub.model.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Implements database queries for flight availability
 */
public interface AvailabilityRepo extends MongoRepository<Availability, String> {

    @Query(value = "{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'}, 'DepartureTime': ?1 }")
    List<Availability> findByFlightCodeAndDate(String flight, Long date);

}
