package com.FlightPub.Services;

import com.FlightPub.Controllers.HaversineCalculator;
import com.FlightPub.model.Location;
import com.FlightPub.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("LocationServices")
public class LocationServices {
    private final LocationRepo locationRepo;

    @Autowired
    public LocationServices(LocationRepo locationRepository) {
        this.locationRepo = locationRepository;

        updateAdjacency();
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
        try{
            // Prepares the Data for the database
            String locationName = location.getLocation();
            String country = location.getCountry();
            location.setLocationID(location.getLocationID().toUpperCase());
            location.setLocation(locationName.substring(0, 1).toUpperCase() + locationName.substring(1).toLowerCase());
            location.setCountry(country.substring(0, 1).toUpperCase() + country.substring(1).toLowerCase());

            locationRepo.save(location);

            updateAdjacency();

            return location;
        } catch(Exception e) {
            System.out.println("Error: "+e);
            return null;
        }
    }


    public void delete(String id) {
        locationRepo.deleteById(id);
    }


    public List<Location> findAllSortedAscendingExcluding(List<String> excludeList) {
        return locationRepo.findAllSortedAscendingExcluding(excludeList);
    }

    public Location mostPopular() {
        List<Location> out = locationRepo.findAllByOrderByPopularityAsc();
        System.out.println(out);
        if (!out.isEmpty()) {
            return out.get(0);
        }
        return null;
    }

    public Location findByLocation(String originIn) {
        // Ensures that the string contains a value
        if(originIn == null || originIn.equals(""))
            return null;

        List<Location> out = locationRepo.findByLocation(originIn);
        if (!out.isEmpty()) // returns only a single location
            return out.get(0);
        return null;
    }

    public void incrementPopularity(String location) {
        Location popularLocation = findByLocation(location);

        if (popularLocation != null) {
            int currentPopularity = popularLocation.getPopularity();

            // If current popularity is not already number 1
            if (currentPopularity != 1) {
                Location higherPopularityLocation = locationRepo.findFirstByPopularity(currentPopularity-1);
                Location currentPopularityLocation = locationRepo.findFirstByPopularity(currentPopularity);

                higherPopularityLocation.setPopularity(currentPopularity);
                currentPopularityLocation.setPopularity(currentPopularity-1);

                saveOrUpdate(higherPopularityLocation);
                saveOrUpdate(currentPopularityLocation);
            }
        } else {
            System.out.println("Could not find a increase location popularity for: " + location);
        }
    }


    public void updateAdjacency() {
        // For all locations
        for (Location allLocation: listAll()) {
            List<Double> distance = new ArrayList<>();
            Map<Double, String> locations = new HashMap<>();

            // For all locations excluding the current location
            for (Location excludeLocation: findAllSortedAscendingExcluding(Collections.singletonList(allLocation.getLocationID()))) {
                // Find the distance from the current location and another location
                double currentDistance = HaversineCalculator.distance (allLocation.getLatitude(), allLocation.getLongitude(), excludeLocation.getLatitude(), excludeLocation.getLongitude());
                distance.add(currentDistance);
                locations.put(currentDistance, excludeLocation.getLocationID());
            }

            // Sort the distances
            Collections.sort(distance);

            // For top 3 distances record location id
            ArrayList<String> adjacentLocations = new ArrayList<>();
            for (Object sortedDistance: distance.stream().limit(3).toArray()) {
                adjacentLocations.add(locations.get((Double) sortedDistance));
            }

            // Update the locations adjacency list
            allLocation.setAdjacentLocations(adjacentLocations);

            // Update location
            locationRepo.save(allLocation);
        }
    }
}
