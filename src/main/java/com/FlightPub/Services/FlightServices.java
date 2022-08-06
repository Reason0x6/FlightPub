package com.FlightPub.Services;

import com.FlightPub.model.Availability;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import com.FlightPub.model.Price;
import com.FlightPub.repository.AvailabilityRepo;
import com.FlightPub.repository.FlightRepo;
import com.FlightPub.repository.LocationRepo;
import com.FlightPub.repository.PriceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

@Service("FlightServices")
public class FlightServices{

    private FlightRepo flightRepo;
    private AvailabilityRepo availRepo;
    private PriceRepo priceRepo;
    @Autowired
    private LocationServices locationServices;

    @Autowired
    public FlightServices(FlightRepo flightRepository, AvailabilityRepo availRepo, PriceRepo priceRepo) {
        this.availRepo = availRepo;
        this.flightRepo = flightRepository;
        this.priceRepo = priceRepo;
    }

    public List<Flight> listAll(){
        List<Flight> flights = new ArrayList<>();
        flightRepo.findAll().forEach(flights::add);
        return flights;
    }

    public Flight getById(String id){
        if(id == null)
            return null;
        else
            return flightRepo.findById(id.toUpperCase()).orElse(null);
    }

    public Flight saveOrUpdate(Flight flight){
        // Attempts to align the string values with the database standard
        try {
            flight.setFlightID(flight.getFlightID().toUpperCase());
            flight.setOriginID(flight.getOriginID().toUpperCase());
            flight.setDestinationID(flight.getDestinationID().toUpperCase());
            flight.setFlightCode(flight.getFlightCode().toUpperCase());
            flight.setAirline(flight.getAirline().toUpperCase());
            flightRepo.save(flight);
            return flight;
        } catch (Exception e) {
            System.out.println("Error: "+e);
            return null;
        }
    }

    public List<Price> getPrices(Flight flight){
        return priceRepo.findPrices(flight.getFlightNumber(), flight.getDepartureTime());
    }

    public void delete(String id){}

    public List<Flight> getByDestination(String dest) {

        // Query defined in flightRepo
       return flightRepo.findByDestination(dest);
    }

    public int getAvailableSeats(String id, Date departTime){

        int out = 0;
        for (Availability a: availRepo.findByFlightCodeAndDate(id, departTime)) {
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

    public List<Flight> getByOriginAndDestination(String origin, String dest, Date dstart, Date dend) {
        // Query defined in flightRepo
        return flightRepo.findByOriginAndDestination(origin, dest, dstart, dend);
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
