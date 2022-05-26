package com.FlightPub.Services;

import com.FlightPub.model.Booking;
import com.FlightPub.model.Flight;
import com.FlightPub.repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("BookingServices")
public class BookingServices {

    private BookingRepo bookingRepo;

    @Autowired
    public BookingServices(BookingRepo bookingRepository) {
        this.bookingRepo = bookingRepository;
    }

    public List<Booking> listAll(){
        List<Booking> bookings = new ArrayList<>();
        bookingRepo.findAll().forEach(bookings::add);
        return bookings;
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
}
