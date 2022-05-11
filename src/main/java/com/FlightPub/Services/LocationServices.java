package com.FlightPub.Services;

import com.FlightPub.model.Location;
import com.FlightPub.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("LocationServices")
public class LocationServices {
        private final LocationRepo locationRepo;

    @Autowired
    public LocationServices(LocationRepo locationRepository) {
        this.locationRepo = locationRepository;
    }


    public List<Location> listAll() {
        List<Location> locations = new ArrayList<>();
        locationRepo.findAll().forEach(locations::add);
        return locations;
    }


    public Location getById(String id) {
        return locationRepo.findById(id).orElse(null);
    }


    public Location saveOrUpdate(Location location) {
        locationRepo.save(location);
        return location;
    }


    public void delete(String id) {
        locationRepo.deleteById(id);
    }


    public List<Location> findAllExcluding(String locationID) {
        return locationRepo.findAllExcluding(locationID);
    }


    public Location findByLocation(String originIn) {
        List<Location> out = locationRepo.findByLocation(originIn);
        if (!out.isEmpty()) {
            return out.get(0);
        }
        return null;
    }
}

