package com.FlightPub.Services;

import com.FlightPub.model.*;
import com.FlightPub.repository.AvailabilityRepo;
import com.FlightPub.repository.FlightRepo;
import com.FlightPub.repository.PriceRepo;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implements database interaction for flights
 */
@Service("FlightServices")
public class FlightServices {
    private HashMap<String, Map.Entry<Date, List<Flight>>> flightCache;
    private HashMap<String, Map.Entry<Date, List<Availability>>> availCache;
    private HashMap<String, Map.Entry<Date, List<Price>>> priceCache;
    private final FlightRepo flightRepo;
    private final AvailabilityRepo availRepo;
    private final PriceRepo priceRepo;
    @Autowired
    private LocationServices locationServices;

    private List<Availability> savedAvails;

    /**
     * Sets the classes' repository objects
     * @param flightRepository, availRepo, priceRepo
     */
    @Autowired
    public FlightServices(FlightRepo flightRepository, AvailabilityRepo availRepo, PriceRepo priceRepo) {
        this.availRepo = availRepo;
        this.flightRepo = flightRepository;
        this.priceRepo = priceRepo;
        flightCache = new HashMap<>();
        availCache = new HashMap<>();
        priceCache = new HashMap<>();
    }

    /**
     * Get a list of available flights
     * @param flightNumber, depatureTime
     * @return List if a available flights
     */
    public List<Availability> getAvailability(String flightNumber, Long departureTime) {

        if (availCache.containsKey(flightNumber + departureTime.toString()) && availCache.get(flightNumber + departureTime).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            return availCache.get(flightNumber + departureTime).getValue();
        } else {
            List<Availability> avail = availRepo.findByFlightCodeAndDate(flightNumber, departureTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();


            Map.Entry<Date, List<Availability>> input = new AbstractMap.SimpleEntry<>(time, avail);

            availCache.put(flightNumber + departureTime, input);
            return avail;
        }
    }

    /**
     * Add a new availability or update a availability field in the database
     * @param availability to save or update in the database
     */
    public Availability saveOrUpdateAvailability(Availability availability) {
        try {
            availability.setTicketCode(availability.getTicketCode().toUpperCase());
            availability.setClassCode(availability.getClassCode().toUpperCase());
            availability.setFlightNumber(availability.getFlightNumber().toUpperCase());
            availability.setAirlineCode(availability.getAirlineCode().toUpperCase());

            if(availability.getID() == null)
                availability.setID(new ObjectId().toString());

            Availability a = availRepo.save(availability);
            invalidate();
            return a;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * @return list of all flights
     */
    public List<Flight> listAll(){

        if (flightCache.containsKey("ALL") && flightCache.get("ALL").getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            return flightCache.get("ALL").getValue();
        } else {
            List<Flight> flights = new ArrayList<>();
            flightRepo.findAll().forEach(flights::add);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();
            for (Flight x : flights) {
                if (x.getStopoverCode() != null) {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            locationServices.getById(x.getStopoverCode()).getLocation());
                } else {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            "");
                }
            }
            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, flights);

            flightCache.put("ALL", input);
            return flights;
        }

    }

    /**
     * Get a flight from id
     * @param id to find in the flight repository
     * @return flight if a flight is found or null if not found
     */
    public Flight getById(String id) {
        if (id == null)
            return null;
        else {
            Flight f = flightRepo.findById(id).orElse(null);
            if (f != null) {
                f.loadNames(locationServices.getById(f.getDepartureCode()).getLocation(),
                        locationServices.getById(f.getDestinationCode()).getLocation(),
                        "");
            }
            return f;
        }

    }

    /**
     * Add a new price or update a price field in the database
     * @param price to save or update in the database
     */
    public Price saveOrUpdatePrice(Price price) {
        try {
            price.setFlightNumber(price.getFlightNumber().toUpperCase());
            price.setAirlineCode(price.getAirlineCode().toUpperCase());
            price.setClassCode(price.getClassCode().toUpperCase());
            price.setTicketCode(price.getTicketCode().toUpperCase());

            if(price.getID() == null)
                price.setID(new ObjectId().toString());

            Price p = priceRepo.save(price);
            invalidate();
            return p;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Add a new flight or update a flight field in the database
     * @param flight to save or update in the database
     */
    public Flight saveOrUpdate(Flight flight) {
        // Attempts to align the string values with the database standard
        try {
            flight.setDepartureCode(flight.getDepartureCode().toUpperCase());
            flight.setDestinationCode(flight.getDestinationCode().toUpperCase());
            flight.setPlaneCode(flight.getPlaneCode().toUpperCase());
            flight.setFlightNumber(flight.getFlightNumber().toUpperCase());
            flight.setAirlineCode(flight.getAirlineCode().toUpperCase());
            if (flight.getStopoverCode() != null)
                flight.setStopoverCode(flight.getStopoverCode().toUpperCase());

            // Tests if the Flight exists
            Flight existing = getByFlightNumberAndDeparture(flight.getFlightNumber(), flight.getDepartureTime());
            if (existing != null && existing.getFlightID() != null)
                flight.setFlightID(existing.getFlightID());

            flightRepo.save(flight);
            return flight;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets a list of prices for a flight
     * @param flight
     * @return list of prices for a specific flight
     */
    public List<Price> getPrices(Flight flight) {

        String key = flight.getFlightNumber() + flight.getDepartureTime().toString();
        if (flightCache.containsKey(key) && priceCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            System.out.println("Price by Flight Cache Used");
            return priceCache.get(key).getValue();
        } else {
            System.out.println("Price by Flight Cache Not Used");
            List<Price> out = priceRepo.findPrices(flight.getFlightNumber(), flight.getDepartureTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Price>> input = new AbstractMap.SimpleEntry<>(time, out);

            priceCache.put(key, input);
            return out;
        }
    }

    /**
     * Deletes a flight from the database
     * @param id to find and delete the booking in the repository
     */
    public void delete(String id){}

    /**
     * Gets a list of flights for a specific destination
     * @param dest
     * @return list of flights
     */
    public List<Flight> getByDestination(String dest) {

        if (flightCache.containsKey("DEST" + dest) && flightCache.get("DEST" + dest).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            return flightCache.get("DEST" + dest).getValue();
        } else {
            List<Flight> flights = flightRepo.findByDestination(dest);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();
            for (Flight x : flights) {
                if (x.getStopoverCode() != null) {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            locationServices.getById(x.getStopoverCode()).getLocation());
                } else {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            "");
                }
            }

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, flights);

            flightCache.put("DEST" + dest, input);
            return flights;
        }

    }

    /**
     * Gets the number of available seats for a specific destination
     * @param id, departTime, stopoverCode
     * @return total number of available seats on a flight
     */
    public int getAvailableSeats(String id, Long departTime, String stopoverCode) {
        List<Availability> outArr;
        if (availCache.containsKey(id + departTime.toString()) && availCache.get(id + departTime).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            outArr = availCache.get(id + departTime).getValue();
        } else {
            outArr = availRepo.findByFlightCodeAndDate(id, departTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();


            Map.Entry<Date, List<Availability>> input = new AbstractMap.SimpleEntry<>(time, outArr);

            availCache.put(id + departTime, input);

        }

        int out = 0;
        for (Availability a : outArr) {
            if (stopoverCode != null)
                out += a.getNumberAvailableSeatsLeg1() > a.getNumberAvailableSeatsLeg2() ? a.getNumberAvailableSeatsLeg2() : a.getNumberAvailableSeatsLeg1();
            else
                out += a.getNumberAvailableSeatsLeg1();
        }
        return out;
    }

    /**
     * Gets the list of seats for a class code
     * @param classCode, availableSeats
     * @return list of seats
     */
    public ArrayList<String[]> getSeatList(String classCode, List<Availability> availableSeats) {
        ArrayList<String[]> seatList = new ArrayList<>();

        for (Availability ticket : availableSeats) {
            if (ticket.getClassCode().equals(classCode)) {
                int seatsAvailable = ticket.getNumberAvailableSeatsLeg1() > ticket.getNumberAvailableSeatsLeg2() ? ticket.getNumberAvailableSeatsLeg1() : ticket.getNumberAvailableSeatsLeg2();
                if (seatsAvailable > 0) {
                    String seatsAvailableString = Integer.toString(seatsAvailable);
                    String ticketCode = ticket.getTicketCode();
                    String ticketFlightNumber = ticket.getFlightNumber();
                    Long ticketDepartureDate = ticket.getDepartureTime();
                    String[] seat = getSeatDetails(seatsAvailableString, classCode, ticketCode, ticketFlightNumber, ticketDepartureDate);

                    if (seat != null) {
                        seatList.add(seat);
                    }
                }
            }
        }

        return seatList;
    }

    /**
     * Gets the a specific seats for details for a flight
     * @param seatsAvailableString, classCode, ticketCode, ticketFlightNumber, ticketDepartureDate
     * @return String array of seat details
     */
    private String[] getSeatDetails(String seatsAvailableString, String classCode, String ticketCode, String ticketFlightNumber, Long ticketDepartureDate) {
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
            return null;
        }
        return seatDetails;
    }

    /**
     * Gets a flights price from the stored cache
     * @param ticketFlightNumber, classCode, ticketCode
     * @return List of prices
     */
    public List<Price> getFlightCachePrice(String ticketFlightNumber, String classCode, String ticketCode) {

        String key = ticketFlightNumber;
        if (priceCache.containsKey(key) && priceCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            List<Price> out = priceCache.get(key).getValue();
            List<Price> filter = new ArrayList<>();

            for (Price p : out) {
                if (p.getClassCode().equals(classCode) && p.getTicketCode().equals(ticketCode)) {
                    filter.add(p);
                }
            }
            return filter;
        } else {
            List<Price> out = priceRepo.findFLight(ticketFlightNumber);
            List<Price> filter = new ArrayList<>();

            for (Price p : out) {
                if (p.getClassCode().equals(classCode) && p.getTicketCode().equals(ticketCode)) {
                    filter.add(p);
                }
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();

            Map.Entry<Date, List<Price>> input = new AbstractMap.SimpleEntry<>(time, out);

            priceCache.put(key, input);
            return filter;
        }

    }

    /**
     * Gets a price from a specific flight and ticket class and code
     * @param ticketFlightNumber, classCode, ticketCode, ticketDepartureDate
     * @return price as string
     */
    public @NotNull String getPrice(String ticketFlightNumber, String classCode, String ticketCode, Long ticketDepartureDate) {
        List<Price> price = getFlightCachePrice(ticketFlightNumber, classCode, ticketCode);
        for (int i = 0; i < price.size(); i++) {
            long startDate = price.get(i).getStartDate();
            long endDate = price.get(i).getEndDate();
            Date currentDate = new Date();
            boolean dateInRange = startDate <= (currentDate.getTime()) && endDate >= (currentDate.getTime());
            Double pricePerTicket = price.get(i).getPrice();

            if (dateInRange) {
                return String.valueOf(pricePerTicket);
            }
        }
        return "0";
    }

    /**
     * Gets a price from a specific flight and ticket class and code
     * @param flightNumber, date, ticketClass, ticketCode
     * @return price
     */
    public Price getSpecificPrice(String flightNumber, long date, String ticketClass, String ticketCode) {
        List<Price> prices = priceRepo.findSpecificPrice(flightNumber, date, ticketClass, ticketCode);
        if(prices != null && !prices.isEmpty())
            return prices.get(0);
        else
            return null;
    }

    /**
     * Returns a boolean whether a price per ticket exsists in a given time frame
     * @param price
     * @return boolean
     */
    public boolean existingPriceTimeframe(Price price) {
        List<Price> prices = priceRepo.findPriceByClassTicketCode(price.getFlightNumber(), price.getClassCode(), price.getTicketCode());

        // Iterate through the list and test whether the dates are within a existing time period
        for(Price p : prices) {
            if(p.getStartDate() <= price.getStartDate() && p.getEndDate() >= price.getStartDate())
                return true;
            if(p.getStartDate() <= price.getEndDate() && p.getEndDate() >= price.getEndDate())
                return true;
            if(p.getStartDate() >= price.getStartDate() && p.getEndDate() <= price.getEndDate())
                return true;
        }

        return false;
    }

    /**
     * Returns a price for a ticket for specific time frame
     * @param flightNumber, startDate, endDate, ticketClass, ticketCode
     * @return price
     */
    public Price getSpecificPriceTimeframe(String flightNumber, long startDate, long endDate, String ticketClass, String ticketCode) {
        List<Price> price = priceRepo.findSpecificPriceTimeframe(flightNumber, startDate, endDate, ticketClass, ticketCode);
        if(price != null && !price.isEmpty())
            return price.get(0);
        else
            return null;
    }

    /**
     * Returns the cheapest price for a specific flight
     * @param flightID, flightNumber, departureDate
     * @return price as string
     */
    public String findCheapestPrice(String flightID, String flightNumber, long departureDate) {
        List<Availability> availableSeats = getAvailability(flightNumber, departureDate);
        double minPrice = Integer.MAX_VALUE + 0.0;
        Date currentDate = new Date();
        for (int i = 0; i < availableSeats.size(); i++) {
            List<Price> prices = getFlightCachePrice(flightNumber, availableSeats.get(i).getClassCode(), availableSeats.get(i).getTicketCode());
            for (Price x : prices) {
                if (x.getPrice() < minPrice && (x.getStartDate() <= currentDate.getTime()) && x.getEndDate() >= currentDate.getTime()) {
                    minPrice = x.getPrice();
                }
            }
        }
        return minPrice + "";
    }

    /**
     * Returns a boolean if a price exists for a specific flight
     * @param flight
     * @return boolean
     */
    public boolean priceExists(String flight) {
        List<Price> prices= priceRepo.findFLight(flight);
        if(prices != null & !prices.isEmpty())
            return true;
        else
            return false;
    }

    /**
     * Returns list of all flights from a specific departure location
     * @param dep
     * @return list of flights
     */
    public List<Flight> getByOrigin(String dep) {

        if (flightCache.containsKey("DEP" + dep) && flightCache.get("DEP" + dep).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            return flightCache.get("DEP" + dep).getValue();
        } else {
            List<Flight> out = new ArrayList<>();
            flightRepo.findByOrigin(dep).forEach(flight -> {

                Location loc = locationServices.getById(flight.getDestinationCode());

                if (!loc.isCovid_restricted()) {
                    out.add(flight);
                }
            });

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();
            for (Flight x : out) {
                if (x.getStopoverCode() != null) {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            locationServices.getById(x.getStopoverCode()).getLocation());
                } else {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            "");
                }
            }

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put("DEP" + dep, input);
            return out;
        }
    }

    /**
     * Returns list of all flights from a specific departure location and arrival location within a time frame
     * @param origin, dest, dstart, dend
     * @return list of flights
     */
    public List<Flight> getByOriginAndDestination(String origin, String dest, Long dstart, Long dend) {
        String key = "DEP" + origin + "DEST" + dest + "DEP" + dstart.toString() + dend.toString();
        if (flightCache.containsKey(key) && flightCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {

            return flightCache.get(key).getValue();
        } else {
            List<Flight> out = flightRepo.findByOriginAndDestination(origin, dest, dstart, dend);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();
            for (Flight x : out) {
                if (x.getStopoverCode() != null) {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            locationServices.getById(x.getStopoverCode()).getLocation());
                } else {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            "");
                }
            }

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put(key, input);
            return out;
        }

    }

    /**
     * Returns list of all flights from a specific departure location within a time frame
     * @param origin, dstart, dend
     * @return list of flights
     */
    public List<Flight> getByOrigin(String origin, Long dstart, Long dend) {
        String key = "DEP" + origin + dstart.toString() + dend.toString();
        if (flightCache.containsKey(key) && flightCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            return flightCache.get(key).getValue();
        } else {
            List<Flight> out = flightRepo.findByOrigin(origin, dstart, dend);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();
            for (Flight x : out) {
                if (x.getStopoverCode() != null) {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            locationServices.getById(x.getStopoverCode()).getLocation());
                } else {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            "");
                }

            }

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put(key, input);
            return out;
        }
    }

    /**
     * Returns list of all flights from a specific departure and destination location before a specific arrival time
     * @param origin, dep, dstart, dend
     * @return list of flights
     */
    public List<Flight> getByOriginAndDestinationAndArrivalTimes(String origin, String dep, Long dstart, Long dend) {
        String key = "DEP" + origin + "DEST" + dep + "DEST" + dstart.toString() + dend.toString();
        if (flightCache.containsKey(key) && flightCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            return flightCache.get(key).getValue();
        } else {
            List<Flight> out = flightRepo.findByOriginAndDestinationAndArrivalTimes(origin, dep, dstart, dend);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();
            for (Flight x : out) {
                if (x.getStopoverCode() != null) {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            locationServices.getById(x.getStopoverCode()).getLocation());
                } else {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            "");
                }
            }

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put(key, input);
            return out;
        }

    }

    /**
     * Returns list of all flights from a specific departure location before a specific arriavl time
     * @param origin, dep, dstart, dend
     * @return list of flights
     */
    public List<Flight> getByOriginAndArrivalTimes(String origin, Long dstart, Long dend) {

        String key = "DEP" + origin + "DEST" + dstart.toString() + dend.toString();
        if (flightCache.containsKey(key) && flightCache.get(key).getKey().compareTo(new Date(System.currentTimeMillis())) > 0) {
            return flightCache.get(key).getValue();
        } else {
            List<Flight> out = flightRepo.findByOriginAndArrivalTimes(origin, dstart, dend);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();
            for (Flight x : out) {
                if (x.getStopoverCode() != null) {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            locationServices.getById(x.getStopoverCode()).getLocation());
                } else {
                    x.loadNames(locationServices.getById(x.getDepartureCode()).getLocation(),
                            locationServices.getById(x.getDestinationCode()).getLocation(),
                            "");
                }
            }

            Map.Entry<Date, List<Flight>> input = new AbstractMap.SimpleEntry<>(time, out);

            flightCache.put(key, input);
            return out;
        }

    }

    /**
     * Returns a flight from a specific departure location
     * @param flightNumber, departure
     * @return flight
     */
    public Flight getByFlightNumberAndDeparture(String flightNumber, Long departure) {
        if (flightNumber == null || departure == null)
            return null;

        List<Flight> out = flightRepo.findByFlightNumberAndDeparture(flightNumber, departure);

        if (!out.isEmpty()) {
            out.get(0).loadNames(locationServices.getById(out.get(0).getDepartureCode()).getLocation(),
                    locationServices.getById(out.get(0).getDestinationCode()).getLocation(),
                    locationServices.getById(out.get(0).getStopoverCode()).getLocation());
            return out.get(0);
        } else {
            return null;
        }
    }

    /**
     * Clears the cache so new data is pulled
     */
    public void invalidate() {
        flightCache = new HashMap<>();
        availCache = new HashMap<>();
        priceCache = new HashMap<>();

    }
}
