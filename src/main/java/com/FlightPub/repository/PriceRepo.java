package com.FlightPub.repository;


import com.FlightPub.model.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface PriceRepo extends MongoRepository<Price, String> {

    @Query(value="{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'},  'StartDate' : ?1 }", fields="{ 'destination' : 1 }")
    List<Price> findPrices(String FlightNumber, Long StartDate);

    @Query(value="{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'}}")
    List<Price> findFLight(String FlightNumber);

    @Query(value="{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'}, 'ClassCode' : { '$regex' : ?1 , $options: 'i'}, 'TicketCode' : { '$regex' : ?2 , $options: 'i'} }")
    List<Price> findPriceByClassTicketCode(String FlightNumber, String ClassCode, String TicketCode);

    @Query(value="{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'} }", fields="{ 'flightNumber' : 1, 'classCode' : 1, 'ticketCode' : 1, 'price' : 1, 'startDate' : 1, 'endDate' : 1 }")
    List<Price> findCheapestFlights(String FlightNumber);
}
