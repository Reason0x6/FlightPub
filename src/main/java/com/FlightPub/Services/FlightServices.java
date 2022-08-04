package com.FlightPub.Services;

import com.FlightPub.model.Availability;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import com.FlightPub.repository.AvailabilityRepo;
import com.FlightPub.repository.FlightRepo;
import com.FlightPub.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

@Service("FlightServices")
public class FlightServices{

    private FlightRepo flightRepo;
    private AvailabilityRepo availRepo;
    @Autowired
    private LocationServices locationServices;



    @Autowired
    public FlightServices(FlightRepo flightRepository, AvailabilityRepo availRepo) {
        this.availRepo = availRepo;
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

    public int getAvailableSeats(String id, Date departTime){
        List<Availability> avails = availRepo.findByFlightCodeAndDate(id, departTime);
        int out = 0;
        for (Availability a: avails) {
            out += a.getNumberAvailableSeatsLeg1() > a.getNumberAvailableSeatsLeg2() ? a.getNumberAvailableSeatsLeg2() : a.getNumberAvailableSeatsLeg1();
        }
        return out;
    }

    public List<Flight> getByOrigin(String dep) {
        List<Flight> out = new ArrayList<>();

        flightRepo.findByOrigin(dep).forEach(flight -> {

            Location loc = locationServices.getById(flight.getDestinationCode());

            if(!loc.isCovid_restricted()){
                out.add(flight);
            }
        });
        // Query defined in flightRepo
        return out;
    }

    public List<Flight> getByOriginAndDestination(String origin, String dep, Date dstart, Date dend) {
        // Query defined in flightRepo
        return flightRepo.findByOriginAndDestination(origin, dep, dstart, dend);
    }

    public List<Flight> getByOrigin(String origin, Date dstart, Date dend){
        // Query defined in flightRepo
        return flightRepo.findByOrigin(origin, dstart, dend);
    }

    public List<Flight> getByOriginAndDestinationAndArrivalTimes(String origin, String dep, Date dstart, Date dend) {
        // Query defined in flightRepo
        return flightRepo.findByOriginAndDestinationAndArrivalTimes(origin, dep, dstart, dend);
    }

    public List<Flight> getByOriginAndArrivalTimes(String origin, Date dstart, Date dend) {
        // Query defined in flightRepo
        return flightRepo.findByOriginAndArrivalTimes(origin, dstart, dend);
    }
}
