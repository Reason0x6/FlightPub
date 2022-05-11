package com.FlightPub.Services;

import com.FlightPub.model.Location;
import com.FlightPub.model.UserAccount;
import com.FlightPub.repository.LocationRepo;
import com.FlightPub.repository.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("LocationServices")
public class LocationServices {


        private LocationRepo locationRepo;

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

    }

