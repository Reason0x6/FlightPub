package com.FlightPub.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.FlightPub.model.UserAccount;

public interface ItemRepository extends MongoRepository<UserAccount, String> {
	
	@Query("{email:'?0'}")
	UserAccount findUserByEmail(String email);

	@Query("{username:'?0'}")
	UserAccount getUserByUserName(String username);
	
	@Query("{}")
	List<UserAccount> findAll();
	
	public long count();

}
