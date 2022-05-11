package com.FlightPub.repository;


import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LocationRepo extends MongoRepository<Location, String> {
    @Query(value="{ 'locationID' : {$ne : ?0} }")
    List<Location> findAllExcluding(String dest);
}
