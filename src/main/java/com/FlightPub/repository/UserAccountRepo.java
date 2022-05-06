package com.FlightPub.repository;


import com.FlightPub.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepo extends MongoRepository<UserAccount, String> {

}
