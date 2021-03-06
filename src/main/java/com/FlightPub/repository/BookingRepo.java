package com.FlightPub.repository;

import com.FlightPub.model.Booking;
import com.FlightPub.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookingRepo extends MongoRepository<Booking, String> {

    @Query(value="{ 'userID' :?0 }")
    List<Booking> findByUser(String in);

}