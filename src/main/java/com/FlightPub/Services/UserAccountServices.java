package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;
import com.FlightPub.repository.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("UserAccountServices")
public class UserAccountServices{
    private UserAccountRepo userRepo;

    @Autowired
    public UserAccountServices(UserAccountRepo userAccountRepository) {
        this.userRepo = userAccountRepository;
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
