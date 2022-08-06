package com.FlightPub.repository;


import com.FlightPub.model.TicketType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketTypeRepo extends MongoRepository<TicketType, String> {

}
