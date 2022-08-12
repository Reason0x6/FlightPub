package com.FlightPub.Services;

import com.FlightPub.model.Booking;
import com.FlightPub.model.HolidayPackage;
import com.FlightPub.model.WishListItem;
import com.FlightPub.repository.BookingRepo;
import com.FlightPub.repository.WishListItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("WishListServices")
public class WishListServices {

    private WishListItemRepo wishlistRepo;

    @Autowired
    public WishListServices(WishListItemRepo wishlistRepository) {
        this.wishlistRepo = wishlistRepository;
    }


    public List<WishListItem> findAllByUserIDs(String userID){
        return wishlistRepo.findAllByUserIDs(userID);
    }

    public WishListItem getById(String id){
        return wishlistRepo.findById(id).orElse(null);
    }

    public void saveOrUpdate(WishListItem toUpdate){
        wishlistRepo.save(toUpdate);
    }

    public void delete(String id){}

    public List<WishListItem> listAll() {
        List<WishListItem> wishListItems = new ArrayList<>();
        wishlistRepo.findAll().forEach(wishListItems::add);
        return wishListItems;
    }

    public List<WishListItem> findAllByPopularitySortDesc(){
        return wishlistRepo.findAllByPopularitySortDesc();
    }
}

