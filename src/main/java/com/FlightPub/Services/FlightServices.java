package com.FlightPub.Services;

import com.FlightPub.model.Availability;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import com.FlightPub.model.Price;
import com.FlightPub.repository.AvailabilityRepo;
import com.FlightPub.repository.FlightRepo;
import com.FlightPub.repository.PriceRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Object getSeatList(String classCode, List<Availability> availableSeats) {
        List<Map.Entry<String, Integer>> seats = new ArrayList<>();
        for (Availability ticket : availableSeats) {
            if (ticket.getClassCode().equals(classCode)) {
                int seatsAvailable = ticket.getNumberAvailableSeatsLeg1() > ticket.getNumberAvailableSeatsLeg2() ? ticket.getNumberAvailableSeatsLeg1() : ticket.getNumberAvailableSeatsLeg2();
                if (seatsAvailable > 0) {
                    String ticketCode = ticket.getTicketCode();
                    switch (ticketCode) {
                        case "A":
                            seats.add(new AbstractMap.SimpleEntry<>("Standby", seatsAvailable));
                            break;
                        case "B":
                            seats.add(new AbstractMap.SimpleEntry<>("Premium Discounted", seatsAvailable));
                            break;
                        case "C":
                            seats.add(new AbstractMap.SimpleEntry<>("Discounted", seatsAvailable));
                            break;
                        case "D":
                            seats.add(new AbstractMap.SimpleEntry<>("Standard", seatsAvailable));
                            break;
                        case "E":
                            seats.add(new AbstractMap.SimpleEntry<>("Long Distance", seatsAvailable));
                            break;
                        case "F":
                            seats.add(new AbstractMap.SimpleEntry<>("Platinum", seatsAvailable));
                            break;
                    }
                }
            }
        }
        return seats;
    }

    public Object getPrice(String classCode, List<Availability> availableSeats) {
        String ticketFlightNumber;
        String ticketCode;
        Date ticketDepartureDate;
        for (Availability ticketAvailability : availableSeats) {
            if (ticketAvailability.getClassCode().equals(classCode)) {
                ticketFlightNumber = ticketAvailability.getFlightNumber();
                ticketCode = ticketAvailability.getTicketCode();
                ticketDepartureDate = ticketAvailability.getDepartureTime();
                switch (ticketCode) {
                    case "A":
                        return getPriceForTicketType(ticketFlightNumber, classCode, "A", ticketDepartureDate);
                    case "B":
                        return getPriceForTicketType(ticketFlightNumber, classCode, "B", ticketDepartureDate);
                    case "C":
                        return getPriceForTicketType(ticketFlightNumber, classCode, "C", ticketDepartureDate);
                    case "D":
                        return getPriceForTicketType(ticketFlightNumber, classCode, "D", ticketDepartureDate);
                    case "E":
                        return getPriceForTicketType(ticketFlightNumber, classCode, "E", ticketDepartureDate);
                    case "F":
                        return getPriceForTicketType(ticketFlightNumber, classCode, "F", ticketDepartureDate);
                    case "G":
                        return getPriceForTicketType(ticketFlightNumber, classCode, "G", ticketDepartureDate);
                    default:
                        throw new IllegalStateException("Unexpected value: " + ticketCode);
                }
            }
        }
        return null;
    }

    private @NotNull String getPriceForTicketType(String ticketFlightNumber , String classCode, String ticketCode, Date ticketDepartureDate) {
        List<Price> price = priceRepo.findPriceByClassTicketCode(ticketFlightNumber, classCode, ticketCode);
        Date startDate;
        Date endDate;
        Double pricePerTicket;
        boolean dateInRange;
        for (int i = 0; i < price.size(); i++) {
            startDate = price.get(i).getStartDate();
            endDate = price.get(i).getEndDate();
            dateInRange = startDate.compareTo(ticketDepartureDate) <= 0 && endDate.compareTo(ticketDepartureDate) >= 0;
            pricePerTicket = price.get(i).getPrice();
            if (dateInRange) {
                System.out.println("Price Per Ticket: " + pricePerTicket + " Ticket Code: " + ticketCode + " Class Code: " + classCode);
                return "$" + pricePerTicket;
            }
            i++;
        }
        return "No Price Available";
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
