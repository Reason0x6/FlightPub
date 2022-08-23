package com.FlightPub.repository;


import com.FlightPub.model.TicketClass;
import com.FlightPub.model.TicketType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketTypeRepo extends MongoRepository<TicketType, String> {

    List<TicketType> findAll();
}
