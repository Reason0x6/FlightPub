package com.FlightPub.repository;


import com.FlightPub.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Implements database queries for user accounts
 */
public interface UserAccountRepo extends MongoRepository<UserAccount, String> {

}
