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
import java.util.*;

@Service("FlightServices")
public class FlightServices{

    private FlightRepo flightRepo;
    private AvailabilityRepo availRepo;
    private PriceRepo priceRepo;
    @Autowired
    private LocationServices locationServices;

    private List<Availability> savedAvails;


    @Autowired
    public FlightServices(FlightRepo flightRepository, AvailabilityRepo availRepo, PriceRepo priceRepo) {
        this.availRepo = availRepo;
        this.flightRepo = flightRepository;
        this.priceRepo = priceRepo;
    }

    public List<Availability> getAvailability(String flightNumber, Date departureTime) {
        List<Availability> avail = availRepo.findByFlightCodeAndDate(flightNumber, departureTime);
        savedAvails = avail;
        return savedAvails;
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
            return flightRepo.findById(id).orElse(null);
    }

    public void saveOrUpdate(Flight toUpdate){
        flightRepo.save(toUpdate);
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

    public Object getSeatList(String a, List<Availability> availableSeats) {
        List<Map.Entry<String, Integer>> seats = new ArrayList<>();
        for (Availability ticket : availableSeats) {
            if (ticket.getClassCode().equals(a)) {
                int seatsAvailable = ticket.getNumberAvailableSeatsLeg1() > ticket.getNumberAvailableSeatsLeg2() ? ticket.getNumberAvailableSeatsLeg1() : ticket.getNumberAvailableSeatsLeg2();
                seats.add(new AbstractMap.SimpleEntry<>(ticket.getTicketCode(), seatsAvailable));
            }
        }
        return seats;
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
