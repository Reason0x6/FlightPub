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

    private final AdminAccountRepo adminRepo;

    /**
     * Sets the classes' repository object
     * @param adminAccountRepository
     */
    @Autowired
    public AdminAccountServices(AdminAccountRepo adminAccountRepository) {
        this.adminRepo = adminAccountRepository;
    }

    /**
     * Get an admin from id
     * @param id to find in the admin repository
     * @return admin account if id is found or null if not found
     */
    public AdminAccount getById(String id) {
        return adminRepo.findById(id).orElse(null);
    }

    /**
     * Add a new admin or update a admin field in the database
     * @param admin to save or update in the database
     * @return admin account
     */
    public AdminAccount saveOrUpdate(AdminAccount admin) {
        adminRepo.save(admin);
        return admin;
    }

    /**
     * Deletes an admin from the database
     * @param id to find and delete the admin in the repository
     */
    public void delete(String id) {
        adminRepo.deleteById(id);
    }
}
