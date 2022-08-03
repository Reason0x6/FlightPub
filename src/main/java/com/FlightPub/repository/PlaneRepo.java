package com.FlightPub.repository;


import com.FlightPub.model.Plane;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PlaneRepo extends MongoRepository<Plane, String> {

    @Query("{ _id: { '$regex' : ?0 , $options: 'i'}}")
    Plane findPlaneById(String planeID);

}
