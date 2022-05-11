package com.FlightPub.repository;


import com.FlightPub.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepo extends MongoRepository<Location, String> {

}
