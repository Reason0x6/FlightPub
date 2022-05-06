package com.FlightPub.repository;

import com.FlightPub.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.UpdateResult;

@Component
public class UserAccountRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	
	public void updateEmail(String username, String email) {
		Query query = new Query(Criteria.where("userName").is(username));
		Update update = new Update();
		update.set("email", email);
		
		UpdateResult result = mongoTemplate.updateFirst(query, update, UserAccount.class);
		
		if(result == null)
			System.out.println("No documents updated");
		else
			System.out.println(result.getModifiedCount() + " document(s) updated..");

	}

}
