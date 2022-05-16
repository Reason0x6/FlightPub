package com.FlightPub.Services;

import com.FlightPub.model.Flight;
import com.FlightPub.repository.FlightRepo;
import com.FlightPub.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("FlightServices")
public class FlightServices{

    private FlightRepo flightRepo;

    @Autowired
    public FlightServices(FlightRepo flightRepository) {
        this.flightRepo = flightRepository;
    }

    public List<Flight> listAll(){
        List<Flight> flights = new ArrayList<>();
        flightRepo.findAll().forEach(flights::add);
        return flights;
    }

    public Flight getById(String id){
        return flightRepo.findById(id).orElse(null);
    }

    public void saveOrUpdate(Flight toUpdate){
        flightRepo.save(toUpdate);
    }


    public void delete(String id){}

    public List<Flight> getByDestination(String dest) {

        // Query defined in flightRepo
       return flightRepo.findByDestination(dest);
    }

    public List<Flight> getByOrigin(String dep) {
        // Query defined in flightRepo
        return flightRepo.findByOrigin(dep);
    }

    public List<Flight> getByOriginAndDestination(String origin, String dep, Date dstart, Date dend) {
        // Query defined in flightRepo
        return flightRepo.findByOriginAndDesitination(origin, dep, dstart, dend);
    }

    public List<Flight> getByOrigin(String origin, Date dstart, Date dend){
        // Query defined in flightRepo
        return flightRepo.findByOrigin(origin, dstart, dend);
    }

    public List<Flight> getByOriginAndDestinationAndArrivalTimes(String origin, String dep, Date dstart, Date dend) {
        // Query defined in flightRepo
        return flightRepo.findByOriginAndDesitinationAndArrivalTimes(origin, dep, dstart, dend);
    }

    public List<Flight> getByOriginAndArrivalTimes(String origin, Date dstart, Date dend) {
        // Query defined in flightRepo
        return flightRepo.findByOriginAndArrivalTimes(origin, dstart, dend);
    }
}
