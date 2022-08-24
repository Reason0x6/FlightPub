package com.FlightPub.Services;

import com.FlightPub.model.Airlines;
import com.FlightPub.repository.AirlineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements database interaction for airlines
 */
@Service("AirlineServices")
public class AirlineServices {
    private final AirlineRepo airlineRepo;

    /**
     * Sets the classes' repository object
     * @param airlineRepo
     */
    @Autowired
    public AirlineServices(AirlineRepo airlineRepo) {
        this.airlineRepo = airlineRepo;
    }

    /**
     * Add a new airline or update an airline field in the database
     * @param airlines to save or update in the database
     * @return airline
     */
    public Airlines saveOrUpdate(Airlines airlines) {
        try {
            airlines.setAirlineID(airlines.getAirlineID().toUpperCase());
            airlines.setCountryCode(airlines.getCountryCode().toUpperCase());
            String name = airlines.getAirlineName().substring(0,1).toUpperCase()+airlines.getAirlineName().substring(1).toLowerCase();
            airlines.setAirlineName(name);

            return airlineRepo.save(airlines);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get a list of sponsored airlines from the database
     * @return List of airlines or null
     */
    public List<Airlines> getSponsoredAirlines() {
        List<Airlines> airlines = airlineRepo.findBySponsoredIsTrue();
        if (!airlines.isEmpty())
            return airlines;
        else
            return null;
    }

    /**
     * Checks if an airline exists
     * @param airlineID to search for in the database
     * @return boolean
     */
    public boolean airlineExists(String airlineID) {
        List<Airlines> airlines = airlineRepo.findAirline(airlineID);
        if(airlines != null && !airlines.isEmpty())
            return true;
        else
            return false;
    }

    /**
     * Get an airline from id
     * @param id to find in the airline repository
     * @return airline if id is found or null if not found
     */
    public Airlines getAirlineByID(String id) {
        List<Airlines> airline = airlineRepo.findAirline(id);
        if(airline != null && !airline.isEmpty())
            return airline.get(0);
        else
            return null;
    }

    /**
     * Get an airline from airline name
     * @param name to find in the airline repository
     * @return airline if id is found or null if not found
     */
    public Airlines getAirlineByAirlineName(String name) {
        List<Airlines> airline = airlineRepo.findAirlineByName(name);
        if(airline != null && !airline.isEmpty())
            return airline.get(0);
        else
            return null;
    }
}
