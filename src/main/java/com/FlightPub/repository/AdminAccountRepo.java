package com.FlightPub.repository;

import com.FlightPub.model.AdminAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Implements database queries for admin accounts
 */
public interface AdminAccountRepo extends MongoRepository<AdminAccount, String> {
}
