package com.FlightPub.Services;

import com.FlightPub.model.Booking;
import com.FlightPub.model.Traveller;
import com.FlightPub.repository.BookingRepo;
import com.FlightPub.repository.TravellerRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements database interaction for bookings
 */
@Service("BookingServices")
public class BookingServices {

    private final BookingRepo bookingRepo;

    private final TravellerRepo travellerRepo;

    @Getter
    private List<Booking> bookings;

    @Getter
    private List<Traveller> travellers;

    /**
     * Sets the classes' repository objects
     * @param bookingRepository, travellerRepository
     */
    @Autowired
    public BookingServices(BookingRepo bookingRepository, TravellerRepo travellerRepository) {
        this.bookingRepo = bookingRepository;
        this.travellerRepo = travellerRepository;
        this.bookings = new ArrayList<>();
        this.travellers = new ArrayList<>();
    }

    /**
     * Gets all bookings from the database
     * @return List of bookings
     */
    public List<Booking> listAll() {
        List<Booking> bookings = new ArrayList<>();
        bookingRepo.findAll().forEach(bookings::add);
        return bookings;
    }

    /**
     * Gets all travellers from the database
     * @return List of travellers
     */
    public List<Traveller> travellerListAll() {
        List<Traveller> travellers = new ArrayList<>();
        travellerRepo.findAll().forEach(travellers::add);
        return travellers;
    }

    /**
     * Get a list of booking from a user
     * @param userID to find in the booking repository
     * @return List if a users bookings if found or null if not found
     */
    public List<Booking> getUserBookings(String userID) {
        return bookingRepo.findByUser(userID);
    }

    /**
     * Get a booking from a user id
     * @param id to find in the booking repository
     * @return booking if a bookings is found or null if not found
     */
    public Booking getById(String id) {
        return bookingRepo.findById(id).orElse(null);
    }

    /**
     * Add a new booking or update a booking field in the database
     * @param toUpdate to save or update in the database
     */
    public void saveOrUpdate(Booking toUpdate) {
        bookingRepo.save(toUpdate);
    }

    /**
     * Deletes an booking from the database
     * @param id to find and delete the booking in the repository
     */
    public void delete(String id) {
    }

    /**
     * Add a new traveller or update a traveller field in the database
     * @param addTraveller to save or update in the database
     */
    public void addTraveller(Traveller addTraveller) {
        travellerRepo.save(addTraveller);
    }

    /**
     * Add a new booking or update a booking field in the database
     * @param booking to save or update in the database
     */
    public void addBooking(Booking booking) {
        bookingRepo.save(booking);
    }

    /**
     * Add a travellers into the traveller list
     * @param traveller to be added into the traveller list
     */
    public void addToTravellerList(Traveller traveller) {
        travellers = new ArrayList<>();
        travellers.add(traveller);
    }

    /**
     * Get a booking details
     * @param userID, flightID to find in the booking repository
     * @return seats booked by a user on a specific flight
     */
    public List<Booking> getBookingDetails(String userID, String flightID) {
        return bookingRepo.seatsBooked(userID, flightID);
    }

    /**
     * Get a traveller from id
     * @param userID to find in the traveller repository
     * @return traveller if id is found or null if not found
     */
    public Traveller getTravellers(String userID) {
        return travellerRepo.findByTraveller(userID);
    }
}
