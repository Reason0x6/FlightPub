package com.FlightPub.Services;

import com.FlightPub.model.AdminAccount;
import com.FlightPub.repository.AdminAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Implements database interaction for admin accounts
 */
@Service("AdminAccountServices")
public class AdminAccountServices {

    private AdminAccountRepo adminRepo;

    @Autowired
    public AdminAccountServices(AdminAccountRepo adminAccountRepository) {
        this.adminRepo = adminAccountRepository;
    }

    public AdminAccount getById(String id) {
        return adminRepo.findById(id).orElse(null);
    }


    public AdminAccount saveOrUpdate(AdminAccount admin) {
        adminRepo.save(admin);
        return admin;
    }


    public void delete(String id) {
        adminRepo.deleteById(id);
    }
}
