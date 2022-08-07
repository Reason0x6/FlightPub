package com.FlightPub.Services;

import com.FlightPub.model.Location;
import com.FlightPub.model.UserAccount;
import com.FlightPub.model.WishListItem;
import com.FlightPub.repository.LocationRepo;
import com.FlightPub.repository.UserAccountRepo;
import com.FlightPub.repository.WishListItemRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("UserAccountServices")
public class UserAccountServices{
    private UserAccountRepo userRepo;
    private WishListItemRepo wishlistRepo;
    private LocationRepo locationRepo;

    @Autowired
    public UserAccountServices(UserAccountRepo userAccountRepository, WishListItemRepo wishlistRepo, LocationRepo locationRepo) {
        this.userRepo = userAccountRepository;
        this.wishlistRepo = wishlistRepo;
        this.locationRepo = locationRepo;
    }

    public boolean addToWishList(String Location, String UserID){
        System.out.println(wishlistRepo.findIfExist(UserID, Location).size());
        if(wishlistRepo.findIfExist(UserID, Location).size() == 0){
            WishListItem tempW = new WishListItem(UserID, Location);
            wishlistRepo.save(tempW);
            return true;
        }
        return false;
    }

    public List<Map.Entry<String, String>> getWishList(UserAccount u) {
        List<Map.Entry<String, String>> locs = new ArrayList<>();
        for(WishListItem w : wishlistRepo.findAllByUserIDs(u.getEmail())){
            Location l = locationRepo.findById(w.getDestinationID()).orElse(null);
            locs.add(new AbstractMap.SimpleEntry<>(l.getLocationID(), l.getLocation()));
        }

        return locs;
    }

    public void removeWIL(String id, String uid){

       List<WishListItem> wTemp = wishlistRepo.findIfExist(uid, id);
       for(WishListItem w: wTemp){

           System.out.println(w.getDestinationID());
           wishlistRepo.deleteById(w.getWLID());
       }

    }
    public List<UserAccount> listAll() {
        List<UserAccount> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users;
    }




    public UserAccount getById(String id) {
        return userRepo.findById(id).orElse(null);
    }


    public UserAccount saveOrUpdate(UserAccount user) {
        userRepo.save(user);
        return user;
    }

   
    public void delete(String id) {
        userRepo.deleteById(id);
    }

}
