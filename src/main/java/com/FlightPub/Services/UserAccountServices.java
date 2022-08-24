package com.FlightPub.Services;

import com.FlightPub.model.Booking;
import com.FlightPub.model.Location;
import com.FlightPub.model.UserAccount;
import com.FlightPub.model.WishListItem;
import com.FlightPub.repository.BookingRepo;
import com.FlightPub.repository.LocationRepo;
import com.FlightPub.repository.UserAccountRepo;
import com.FlightPub.repository.WishListItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implements database interaction for locations
 */
@Service("UserAccountServices")
public class UserAccountServices {
    private final UserAccountRepo userRepo;
    private final WishListItemRepo wishlistRepo;
    private final LocationRepo locationRepo;
    private final BookingRepo bookingRepo;

    /**
     * Sets the classes' repository objects
     * @param userAccountRepository, wishlistRepo, locationRepo, bookingRepo
     */
    @Autowired
    public UserAccountServices(UserAccountRepo userAccountRepository, WishListItemRepo wishlistRepo, LocationRepo locationRepo, BookingRepo bookingRepo) {
        this.userRepo = userAccountRepository;
        this.wishlistRepo = wishlistRepo;
        this.locationRepo = locationRepo;
        this.bookingRepo = bookingRepo;
    }

    /**
     * Adds a location a specific users wish list
     * @param Location, userID
     * @return boolean
     */
    public boolean addToWishList(String Location, String UserID) {
        if (wishlistRepo.findIfExist(UserID, Location).size() == 0) {
            WishListItem tempW = new WishListItem(UserID, Location);
            wishlistRepo.save(tempW);
            return true;
        }
        return false;
    }

    /**
     * Checks if a user has book a specific flight
     * @param userID, flightID
     * @return String confirmtion
     */
    String isBooked(String userID, String flightID){
        List<Booking> bookings = bookingRepo.seatsBooked(userID, flightID);

        System.out.println(bookings.size() + " " + userID + " " + flightID);
        return bookings.size() > 0 ? "Booked" : "Yet To Book";
    }

    /**
     * Returns all locations from the list of a specific users wish list
     * @param u user account
     * @return List of all locations from a users wish list
     */
    public List<Map.Entry<String, String>> getWishList(UserAccount u) {
        List<Map.Entry<String, String>> locs = new ArrayList<>();
        for (WishListItem w : wishlistRepo.findAllByUserIDs(u.getEmail())) {
            Location l = locationRepo.findById(w.getDestinationID()).orElse(null);
            locs.add(new AbstractMap.SimpleEntry<>(l.getLocationID(), l.getLocation()));
        }

        return locs;
    }

    /**
     * Deletes a users wish list item from the database
     * @param id to find and delete the wish list in the repository
     */
    public void removeWIL(String id, String uid) {

        List<WishListItem> wTemp = wishlistRepo.findIfExist(uid, id);
        for (WishListItem w : wTemp) {

            System.out.println(w.getDestinationID());
            wishlistRepo.deleteById(w.getWLID());
        }

    }

    /**
     * Gets all user accounts from the database
     * @return List of user accounts
     */
    public List<UserAccount> listAll() {
        List<UserAccount> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users;
    }

    /**
     * Get a user account from a user id
     * @param id to find in the user repository
     * @return user account if a user is found or null if not found
     */
    public UserAccount getById(String id) {
        return userRepo.findById(id).orElse(null);
    }

    /**
     * Save or update a user account
     *
     * @param user to update
     * @return updated user
     */
    public UserAccount saveOrUpdate(UserAccount user) {
        userRepo.save(user);
        return user;
    }

    /**
     * Deletes a user account from the database
     * @param id to find and delete the user account in the repository
     */
    public void delete(String id) {
        userRepo.deleteById(id);
    }

}
