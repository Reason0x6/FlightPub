package com.FlightPub.repository;

import com.FlightPub.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BookingRepo extends MongoRepository<Booking, String> {

    @Query(value = "{ 'accountEmail' :?0 }")
    List<Booking> findByUser(String in);

    @Query(value = "{ 'accountEmail' :?0, 'flightID': ?1 }", fields = "{ 'id' : 1, 'confirmationID' : 1, 'travellerID' : 1, 'seat' : 1 }")
    List<Booking> seatsBooked(String user, String flight);

}