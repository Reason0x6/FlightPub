package com.FlightPub.repository;


import com.FlightPub.model.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Implements database queries for flight prices
 */
public interface PriceRepo extends MongoRepository<Price, String> {

    @Query(value = "{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'},  'StartDate' : ?1 }", fields = "{ 'destination' : 1 }")
    List<Price> findPrices(String FlightNumber, Long StartDate);

    @Query(value = "{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'}}")
    List<Price> findFLight(String FlightNumber);

    @Query(value = "{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'}, 'ClassCode' : { '$regex' : ?2 , $options: 'i'}, 'TicketCode' : { '$regex' : ?3 , $options: 'i'}, 'StartDate' : {$lte : ?1}, 'EndDate' : {$gte : ?1} }")
    List<Price> findSpecificPrice(String flightNumber, long date, String ticketClass, String ticketCode);

    @Query(value = "{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'}, 'ClassCode' : { '$regex' : ?3 , $options: 'i'}, 'TicketCode' : { '$regex' : ?4 , $options: 'i'}, 'StartDate' : ?1, 'EndDate' : ?2 }")
    List<Price> findSpecificPriceTimeframe(String flightNumber, long startDate, long endDate, String ticketClass, String ticketCode);

    @Query(value="{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'},  'StartDate' : {$lte : ?1}, 'EndDate' : {$gte : ?1}}")
    List<Price> findPricesOfaDateRange(String FlightNumber, Long departure);

    @Query(value = "{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'}, 'ClassCode' : { '$regex' : ?1 , $options: 'i'}, 'TicketCode' : { '$regex' : ?2 , $options: 'i'} }")
    List<Price> findPriceByClassTicketCode(String FlightNumber, String ClassCode, String TicketCode);

    @Query(value = "{ 'FlightNumber' : { '$regex' : ?0 , $options: 'i'} }", fields = "{ 'flightNumber' : 1, 'classCode' : 1, 'ticketCode' : 1, 'price' : 1, 'startDate' : 1, 'endDate' : 1 }")
    List<Price> findCheapestFlights(String FlightNumber);
}
