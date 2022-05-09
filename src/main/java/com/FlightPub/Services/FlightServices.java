package com.FlightPub.Services;

import com.FlightPub.model.Flight;
import com.FlightPub.repository.FlightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("FlightServices")
public class FlightServices implements FlightServicesINT{

    private FlightRepo flightRepo;

    @Autowired
    public FlightServices(FlightRepo flightRepository) {
        this.flightRepo = flightRepository;
    }

    @Override
    public List<Flight> listAll(){
        List<Flight> flights = new ArrayList<>();
        flightRepo.findAll().forEach(flights::add);
        return flights;
    }

    @Override
    public Flight getById(String id){
        return flightRepo.findById(id).orElse(null);
    }

    @Override
    public void saveOrUpdate(Flight toUpdate){
        flightRepo.save(toUpdate);
    }

    @Override
    public void delete(String id){}

    public List<Flight> getByDesination(String dest) {

        // Query defined in flightRepo
       return flightRepo.findByDesitination(dest);
    }

    public List<Flight> getByDeparture(String dep) {

        // Query defined in flightRepo
        return flightRepo.findByDeparture(dep);
    }
}
