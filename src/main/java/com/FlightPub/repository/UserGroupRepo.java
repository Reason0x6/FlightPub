package com.FlightPub.repository;


import com.FlightPub.model.UserGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserGroupRepo extends MongoRepository<UserGroup, String> {

}
