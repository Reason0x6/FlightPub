package com.FlightPub.Services;

import com.FlightPub.model.UserAccount;
import com.FlightPub.repository.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("UserAccountServices")
public class UserAccountServices implements UserAccountServicesINT{
    private UserAccountRepo userRepo;

    @Autowired
    public UserAccountServices(UserAccountRepo userAccountRepository) {
        this.userRepo = userAccountRepository;
    }


    @Override
    public List<UserAccount> listAll() {
        List<UserAccount> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users;
    }

    @Override
    public UserAccount getById(String id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public UserAccount saveOrUpdate(UserAccount user) {
        userRepo.save(user);
        return user;
    }

    @Override
    public void delete(String id) {
        userRepo.deleteById(id);
    }


}
