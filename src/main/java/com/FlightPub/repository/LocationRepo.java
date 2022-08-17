package com.FlightPub.repository;

import com.FlightPub.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LocationRepo extends MongoRepository<Location, String> {
    @Query(value="{ 'locationID' : {$nin : ?0} }", sort = "{'popularity' : 1}")
    List<Location> findAllSortedAscendingExcluding(List<String> excludeList);

    @Query(value="{ 'location' : { '$regex' : ?0 , $options: 'i'} }")
    List<Location> findByLocation(String in);

    List<Location> findAllByOrderByPopularityAsc();

    Location findFirstByPopularity(int popularity);
}
