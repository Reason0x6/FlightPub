package com.FlightPub.repository;

import com.FlightPub.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookingRepo extends MongoRepository<Booking, String> {

}