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

    @Autowired
    public HolidayPackageServices(HolidayPackageRepo holidayPackageRepository) {
        this.holidayPackageRepo = holidayPackageRepository;
    }

    public List<HolidayPackage> listAll() {
        List<HolidayPackage> holidayPackages = new ArrayList<>();
        holidayPackageRepo.findAll().forEach(holidayPackages::add);
        return holidayPackages;
    }


    public HolidayPackage getById(String id) {
        return holidayPackageRepo.findById(id).orElse(null);
    }


    public HolidayPackage saveOrUpdate(HolidayPackage holidayPackage) {
        holidayPackageRepo.save(holidayPackage);
        return holidayPackage;
    }


    public void delete(String id) {
        holidayPackageRepo.deleteById(id);
    }
}
