package com.FlightPub.repository;

import com.FlightPub.model.Airlines;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AirlineRepo extends MongoRepository<Airlines, String> {

    List<Airlines> findBySponsoredIsTrue();
}
