package com.FlightPub.Services;

import com.FlightPub.model.Airlines;
import com.FlightPub.repository.AirlineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AirlineServices")
public class AirlineServices {
    private final AirlineRepo airlineRepo;

    @Autowired
    public AirlineServices(AirlineRepo airlineRepo) {
        this.airlineRepo = airlineRepo;
    }

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

    public List<Airlines> getSponsoredAirlines() {
        List<Airlines> airlines = airlineRepo.findBySponsoredIsTrue();
        if (!airlines.isEmpty())
            return airlines;
        else
            return null;
    }

    public boolean airlineExists(String airlineID) {
        List<Airlines> airlines = airlineRepo.findAirline(airlineID);
        if(airlines != null && !airlines.isEmpty())
            return true;
        else
            return false;
    }

    public Airlines getAirlineByID(String id) {
        List<Airlines> airline = airlineRepo.findAirline(id);
        if(airline != null && !airline.isEmpty())
            return airline.get(0);
        else
            return null;
    }

    public Airlines getAirlineByAirlineName(String name) {
        List<Airlines> airline = airlineRepo.findAirlineByName(name);
        if(airline != null && !airline.isEmpty())
            return airline.get(0);
        else
            return null;
    }
}
