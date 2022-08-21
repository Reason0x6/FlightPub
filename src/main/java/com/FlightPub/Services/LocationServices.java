package com.FlightPub.Services;

import com.FlightPub.model.Availability;
import com.FlightPub.model.HaversineCalculator;
import com.FlightPub.model.Location;
import com.FlightPub.model.Price;
import com.FlightPub.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implements database interaction for locations
 */
@Service("LocationServices")
public class LocationServices {
    private final LocationRepo locationRepo;
    private HashMap<String, Map.Entry<Date, Location>> locCache;
    @Autowired
    public LocationServices(LocationRepo locationRepository) {
        this.locationRepo = locationRepository;

        locCache = new HashMap<>();
        // Updates location adjacency on startup
        updateAdjacency();
    }

    /**
     * @return list of all locations
     */
    public List<Location> listAll() {
        List<Location> locations = new ArrayList<>();
        locationRepo.findAll().forEach(locations::add);
        return locations;
    }

    /**
     * Get a location by id
     * @param id location id to retrieve
     * @return Location
     */
    public Location getById(String id) {

        if(locCache.containsKey(id) && locCache.get(id).getKey().compareTo(new Date(System.currentTimeMillis())) > 0){
            return locCache.get(id).getValue();
        }else {
            Location loc = locationRepo.findById(id).orElse(null);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.MINUTE, 5);
            Date time = cal.getTime();


            Map.Entry<Date, Location> input = new AbstractMap.SimpleEntry<>(time, loc);

            locCache.put(id, input);
            return loc;
        }
    }

    /**
     * Save or update a location
     * @param location location to update
     * @return updated location
     */
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

    /**
     * Delete a location
     * @param id location id to delete
     */
    public void delete(String id) {
        locationRepo.deleteById(id);
    }

    /**
     * Returns list of flights sorted in ascending popularity order excluding locations in exclude list
     * @param excludeList list locations to exclude from sorted list
     * @return sorted list of locations
     */
    public List<Location> findAllSortedAscendingExcluding(List<String> excludeList) {
        return locationRepo.findAllSortedAscendingExcluding(excludeList);
    }

    /**
     * @return Most popular location
     */
    public Location mostPopular() {
        List<Location> out = locationRepo.findAllByOrderByPopularityAsc();
        System.out.println(out);
        if (!out.isEmpty()) {
            return out.get(0);
        }
        return null;
    }

    public List<Location> topTen() {
        List<Location> out = locationRepo.findAllByOrderByPopularityAsc();
        System.out.println(out);
        if (!out.isEmpty()) {
            return out.subList(0, 10);
        }else{
           out = locationRepo.findAll().subList(0, 10);
        }
        return null;
    }

    /**
     * Finds a location
     * @param locationName name of location to find
     * @return Location matching locationName
     */
    public Location findByLocation(String locationName) {
        // Ensures that the string contains a value
        if(locationName == null || locationName.equals(""))
            return null;

        List<Location> out = locationRepo.findByLocation(locationName);
        if (!out.isEmpty()) // returns only a single location
            return out.get(0);
        return null;
    }

    /**
     * Increase popularity of specified location
     * @param location location to increase popularity for
     */
    public void incrementPopularity(String location) {
        Location popularLocation = findByLocation(location);

        if (popularLocation != null) {
            System.out.println("Increasing popularity for " + popularLocation.getLocationID());

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

    /**
     * Update list of 3 adjacent locations to a specific location
     */
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

    public int getLocationCount() {
        return locationRepo.countLocations();
    }
}
