package com.FlightPub.Services;

import com.FlightPub.model.Flight;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FlightServicesINT {
    List<Flight> listAll();

    Flight getById(String id);

    void saveOrUpdate(Flight toUpdate);

   void delete(String id);

    List<Flight> getByDesination(String dest);
    List<Flight> getByDeparture(String dep);

}
