package com.FlightPub.Services;

import com.FlightPub.model.HolidayPackage;
import com.FlightPub.repository.HolidayPackageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements database interaction for holiday packages
 */
@Service("HolidayPackageServices")
public class HolidayPackageServices {

    private final HolidayPackageRepo holidayPackageRepo;

    /**
     * Sets the classes' repository objects
     * @param holidayPackageRepository
     */
    @Autowired
    public HolidayPackageServices(HolidayPackageRepo holidayPackageRepository) {
        this.holidayPackageRepo = holidayPackageRepository;
    }

    /**
     * Gets all holiday packages from the database
     * @return List of holiday packages
     */
    public List<HolidayPackage> listAll() {
        List<HolidayPackage> holidayPackages = new ArrayList<>();
        holidayPackageRepo.findAll().forEach(holidayPackages::add);
        return holidayPackages;
    }

    /**
     * Get a holiday package from a  id
     * @param id to find in the holiday package repository
     * @return holidaypackage if a holiday package is found or null if not found
     */
    public HolidayPackage getById(String id) {
        return holidayPackageRepo.findById(id).orElse(null);
    }

    /**
     * Add a new holiday package or update a holiday package field in the database
     * @param holidayPackage to save or update in the database
     * @return airline
     */
    public HolidayPackage saveOrUpdate(HolidayPackage holidayPackage) {
        holidayPackageRepo.save(holidayPackage);
        return holidayPackage;
    }

    /**
     * Deletes a holiday package from the database
     * @param id to find and delete the holiday package in the repository
     */
    public void delete(String id) {
        holidayPackageRepo.deleteById(id);
    }
}
