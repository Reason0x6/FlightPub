package com.FlightPub.Services;

import com.FlightPub.model.Booking;
import com.FlightPub.model.Traveller;
import com.FlightPub.repository.BookingRepo;
import com.FlightPub.repository.TravellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements database interaction for bookings
 */
@Service("BookingServices")
public class BookingServices {

    private BookingRepo bookingRepo;

    private TravellerRepo travellerRepo;

    @Autowired
    public BookingServices(BookingRepo bookingRepository, TravellerRepo travellerRepository) {
        this.bookingRepo = bookingRepository;
        this.travellerRepo = travellerRepository;
    }

    public List<Booking> listAll(){
        List<Booking> bookings = new ArrayList<>();
        bookingRepo.findAll().forEach(bookings::add);
        return bookings;
    }

    public List<Traveller> travellerListAll(){
        List<Traveller> travellers = new ArrayList<>();
        travellerRepo.findAll().forEach(travellers::add);
        return travellers;
    }

    public List<Booking> getUserBookings(String userID){
        return bookingRepo.findByUser(userID);
    }

    public Booking getById(String id){
        return bookingRepo.findById(id).orElse(null);
    }

    public void saveOrUpdate(Booking toUpdate){
        bookingRepo.save(toUpdate);
    }



    public void delete(String id){}

    public void addTraveller(Traveller addTraveller) {
        travellerRepo.save(addTraveller);
    }

    public void addBooking(Booking booking) {
        bookingRepo.save(booking);
    }
}
