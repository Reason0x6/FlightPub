package com.FlightPub.repository;


import com.FlightPub.model.TicketClass;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Implements database queries for tickets
 */
public interface TicketClassRepo extends MongoRepository<TicketClass, String> {

    List<TicketClass> findAll();

}
