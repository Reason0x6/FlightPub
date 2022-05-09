package com.FlightPub.Services;

import com.FlightPub.model.Flight;

import java.util.List;

public interface FlightServicesINT {
    List<Flight> listAll();

    Flight getById(String id);

    void saveOrUpdate(Flight toUpdate);

   void delete(String id);

    List<Flight> getByDesination(String dest);
    List<Flight> getByOrigin(String dep);

    List<Flight> getByOriginAndDestination(String origin, String dept);

}
