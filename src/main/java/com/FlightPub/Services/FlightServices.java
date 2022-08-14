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
    private HashMap<String, Map.Entry<Date, List<Flight>>> flightCache;
    private HashMap<String, Map.Entry<Date, List<Availability>>> availCache;
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
        flightCache = new HashMap<>();
        availCache = new HashMap<>();
    }

    public List<Availability> getAvailability(String flightNumber, Long departureTime) {

        if(availCache.containsKey(flightNumber+departureTime.toString()) && availCache.get(flightNumber+departureTime.toString()).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println(flightNumber+departureTime.toString() +" was returned from cache");
            return availCache.get(flightNumber+departureTime.toString()).getValue();
        }else {
            List<Availability> avail = availRepo.findByFlightCodeAndDate(flightNumber, departureTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();


            Map.Entry<Date, List<Availability>> input = new AbstractMap.SimpleEntry<>(time, avail);

            availCache.put(flightNumber + departureTime.toString(), input);
            return avail;
        }
    }

    public List<Flight> listAll(){

        if(flightCache.containsKey("ALL") && flightCache.get("ALL").getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println("* was returned from cache");
            return flightCache.get("ALL").getValue();
        }else {
            List<Flight> flights = new ArrayList<>();
            flightRepo.findAll().forEach(flights::add);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, flights);

            flightCache.put("ALL", input);
            return flights;
        }

    }

    public Flight getById(String id){
        if(id == null)
            return null;
        else
            return flightRepo.findById(id).orElse(null);
    }

    public Flight saveOrUpdate(Flight flight){
        // Attempts to align the string values with the database standard
        try {
            flight.setDepartureCode(flight.getDepartureCode().toUpperCase());
            flight.setDestinationCode(flight.getDestinationCode().toUpperCase());
            flight.setPlaneCode(flight.getPlaneCode().toUpperCase());
            flight.setFlightNumber(flight.getFlightNumber().toUpperCase());
            flight.setAirlineCode(flight.getAirlineCode().toUpperCase());
            if(flight.getStopoverCode() != null)
                flight.setStopoverCode(flight.getStopoverCode().toUpperCase());

            // Tests if the Flight exists
            Flight existing = getByFlightNumberAndDeparture(flight.getFlightNumber(), flight.getDepartureTime());
            if(existing != null && existing.getFlightID() != null)
                flight.setFlightID(existing.getFlightID());

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

        if(flightCache.containsKey("DEST" + dest) && flightCache.get("DEST" + dest).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println("DEST" + dest + " was returned from cache");
            return flightCache.get("DEST" + dest).getValue();
        }else {
            List<Flight> flights = flightRepo.findByDestination(dest);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, flights);

            flightCache.put("DEST" + dest, input);
            return flights;
        }

    }

    public int getAvailableSeats(String id, Long departTime){
        List<Availability> outArr;
        if(availCache.containsKey(id+departTime.toString()) && availCache.get(id+departTime.toString()).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println(id+departTime.toString() + " was returned from cache");
            outArr = availCache.get(id+departTime.toString()).getValue();
        }else {
            outArr = availRepo.findByFlightCodeAndDate(id, departTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();


            Map.Entry<Date, List<Availability>> input = new AbstractMap.SimpleEntry<>(time, outArr);

            availCache.put(id + departTime.toString(), input);

        }

        int out = 0;
        for (Availability a: outArr) {
            out += a.getNumberAvailableSeatsLeg1() > a.getNumberAvailableSeatsLeg2() ? a.getNumberAvailableSeatsLeg2() : a.getNumberAvailableSeatsLeg1();
        }
        return out;
    }

    public Object getSeatList(String classCode, List<Availability> availableSeats) {
        ArrayList<String[]> seatList = new ArrayList<>();
        for (Availability ticket : availableSeats) {
            if (ticket.getClassCode().equals(classCode)) {
                int seatsAvailable = ticket.getNumberAvailableSeatsLeg1() > ticket.getNumberAvailableSeatsLeg2() ? ticket.getNumberAvailableSeatsLeg1() : ticket.getNumberAvailableSeatsLeg2();
                if (seatsAvailable > 0) {
                    String seatsAvailableString = Integer.toString(seatsAvailable);
                    String ticketCode = ticket.getTicketCode();
                    String ticketFlightNumber = ticket.getFlightNumber();
                    Date ticketDepartureDate = ticket.getDepartureTime();
                    seatList.add(getSeatDetails(seatsAvailableString, classCode, ticketCode, ticketFlightNumber, ticketDepartureDate));
                    }
                }
            }
        return seatList;
    }

    private String[] getSeatDetails(String seatsAvailableString, String classCode, String ticketCode, String ticketFlightNumber, Date ticketDepartureDate) {
        String[] seatDetails = new String[4];
        seatDetails[0] = ticketCode;
        seatDetails[1] = seatsAvailableString;
        seatDetails[2] = classCode;
        seatDetails[3] = getPrice(ticketFlightNumber, classCode, ticketCode, ticketDepartureDate);
        switch (seatDetails[0]) {
            case "A":
                seatDetails[0] = "Standby";
                break;
            case "B":
                seatDetails[0] = "Premium Discounted";
                break;
            case "C":
                seatDetails[0] = "Discounted";
                break;
            case "D":
                seatDetails[0] = "Standard";
                break;
            case "E":
                seatDetails[0] = "Premium";
                break;
            case "F":
                seatDetails[0] = "Long Distance";
                break;
            case "G":
                seatDetails[0] = "Platinum";
                break;
        }
        if (seatDetails[3].equals("0")) {
            seatDetails[1] = "0";
            seatDetails[3] = "Not Available";
        }
        return seatDetails;
    }

    public @NotNull String getPrice(String ticketFlightNumber , String classCode, String ticketCode, Date ticketDepartureDate) {
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
                return "$" + pricePerTicket;
            }
            i++;
        }
        return "0";
    }

    public String[] findCheapestFlight(String flightID, String flightNumber, long departureDate) {
        List<Price> priceList = priceRepo.findCheapestFlights(flightNumber);
        List<String[]> flightPriceList = new ArrayList<>();

        String[] flight = new String[5];
        String[] cheapestFlight = new String[5];

        //List<Double> flightPriceList = new ArrayList<>();
        Date travelDate = new Date(departureDate);
        Date startDate;
        Date endDate;
        boolean dateInRange;

        for (int i = 0; i < priceList.size(); i++) {
            startDate = new Date(priceList.get(i).getStartDate().getTime());
            endDate = new Date(priceList.get(i).getEndDate().getTime());
            dateInRange = startDate.compareTo(travelDate) <= 0 && endDate.compareTo(travelDate) >= 0;
            if (dateInRange) {
                flight = new String[5];
                flight[0] = flightID;
                flight[1] = flightNumber;
                flight[2] = priceList.get(i).getClassCode();
                flight[3] = priceList.get(i).getTicketCode();
                flight[4] = priceList.get(i).getPrice().toString();
                flightPriceList.add(flight);
            }
        }

        for (int i = 0; i < flightPriceList.size(); i++) {
            if (i == 0) {
                cheapestFlight = flightPriceList.get(i);
            } else {
                if (Double.parseDouble(flightPriceList.get(i)[4]) < Double.parseDouble(cheapestFlight[4])) {
                    cheapestFlight = new String[5];
                    cheapestFlight[0] = flightPriceList.get(i)[0];
                    cheapestFlight[1] = flightPriceList.get(i)[1];
                    cheapestFlight[2] = flightPriceList.get(i)[2];
                    cheapestFlight[3] = flightPriceList.get(i)[3];
                    cheapestFlight[4] = flightPriceList.get(i)[4];
                }
            }
        }

        System.out.println("Cheapest Flight: " + cheapestFlight[0] + " " + cheapestFlight[1] + " " + cheapestFlight[2] + " " + cheapestFlight[3] + " " + cheapestFlight[4]);

        return cheapestFlight;
    }

    public List<Flight> getByOrigin(String dep) {

        if(flightCache.containsKey("DEP"+dep) && flightCache.get("DEP"+dep).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println("DEP"+dep + " was returned from cache");
            return flightCache.get("DEP"+dep).getValue();
        }else {
            System.out.println("DEP"+dep + " was returned from cache");
            List<Flight> out = new ArrayList<>();
            flightRepo.findByOrigin(dep).forEach(flight -> {

                Location loc = locationServices.getById(flight.getDestinationCode());

                if(!loc.isCovid_restricted()){
                    out.add(flight);
                }
            });

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put("DEP"+dep, input);
            return out;
        }
    }

    public List<Flight> getByOriginAndDestination(String origin, String dest, Long dstart, Long dend) {
        String key = "DEP"+origin+"DEST"+dest+"DEP"+dstart.toString()+dend.toString();
        if(flightCache.containsKey(key) && flightCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println(key + " was returned from cache");
            return flightCache.get(key).getValue();
        }else {
            List<Flight> out = flightRepo.findByOriginAndDestination(origin, dest, dstart, dend);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put(key, input);
            return out;
        }

    }

    public List<Flight> getByOrigin(String origin, Long dstart, Long dend){
        String key = "DEP"+origin+dstart.toString()+dend.toString();
        if(flightCache.containsKey(key) && flightCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println(key + " was returned from cache");
            return flightCache.get(key).getValue();
        }else {
            System.out.println(key + " was generated");
            List<Flight> out = flightRepo.findByOrigin(origin, dstart, dend);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put(key, input);
            return out;
        }
    }

    public List<Flight> getByOriginAndDestinationAndArrivalTimes(String origin, String dep, Long dstart, Long dend) {
        String key = "DEP"+origin+"DEST"+dep+"DEST"+dstart.toString()+dend.toString();
        if(flightCache.containsKey(key) && flightCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println(key + " was returned from cache");
            return flightCache.get(key).getValue();
        }else {
            List<Flight> out = flightRepo.findByOriginAndDestinationAndArrivalTimes(origin, dep, dstart, dend);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put(key, input);
            return out;
        }

    }

    public List<Flight> getByOriginAndArrivalTimes(String origin, Long dstart, Long dend) {

        String key = "DEP"+origin+"DEST"+dstart.toString()+dend.toString();
        if(flightCache.containsKey(key) && flightCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            System.out.println(key + " was returned from cache");
            return flightCache.get(key).getValue();
        }else {
            List<Flight> out = flightRepo.findByOriginAndArrivalTimes(origin, dstart, dend);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put(key, input);
            return out;
        }

    }

    public Flight getByFlightNumberAndDeparture(String flightNumber, Long departure) {
        if(flightNumber == null || departure == null)
            return null;

        List<Flight> out = flightRepo.findByFlightNumberAndDeparture(flightNumber, departure);

        if(!out.isEmpty())
            return out.get(0);
        else
            return null;
    }

    public void invalidate(){

        flightCache = new HashMap<>();
        availCache = new HashMap<>();

    }
}
