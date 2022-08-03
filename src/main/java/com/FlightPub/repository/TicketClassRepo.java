package com.FlightPub.repository;


import com.FlightPub.model.TicketClass;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketClassRepo extends MongoRepository<TicketClass, String> {

}
