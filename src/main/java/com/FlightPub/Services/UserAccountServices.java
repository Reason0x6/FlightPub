package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;
import com.FlightPub.model.WishListItem;
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

    @Autowired
    public UserAccountServices(UserAccountRepo userAccountRepository, WishListItemRepo wishlistRepo) {
        this.userRepo = userAccountRepository;
        this.wishlistRepo = wishlistRepo;
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
