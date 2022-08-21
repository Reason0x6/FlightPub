package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.UserSession;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.model.HaversineCalculator;
import com.FlightPub.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class RecommendationController {
    private Location userLocation;                  // Stores user location
    private List<Location> searchRecommendations;   // Stores recommendations based on users last searched destination

    // Services
    private LocationServices locationServices;

    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationsServices(LocationServices locService) {
        this.locationServices = locService;
    }

    /**
     * Returns a html fragment containing 4 location recommendations
     *
     * @param city    users departure city
     * @param model   interface that defines a holder for model attributes
     * @param session current session
     * @return html recommendation fragment
     */
    @PostMapping("/recommendations")
    public String loadRecommendations(@RequestParam String city, Model model, HttpSession session) {
        userLocation = locationServices.getById(city);

        model.addAttribute("reco", getRecommendation(getSession(session)));

        if (getSession(session).getLastSearchedDestination() != null) {
            model.addAttribute("searchRecommendation", searchRecommendations.toArray());
        }

        model.addAttribute("currentLocation", getCurrentLocation());
        model.addAttribute("recommendationLocation", locationServices.listAll());

        model.addAttribute("usr", getSession(session));

        return "Fragments/Recommendation :: recommendation_fragment";
    }

    /**
     * Locates the nearest location to a given latitude and longitude
     *
     * @param lat     latitude of location
     * @param lon     longitude of location
     * @param model   interface that defines a holder for model attributes
     * @param session current session
     * @return html fragment with the nearest city as departure location
     */
    @PostMapping("LocateNearestCity")
    public String locateNearestCity(@RequestParam double lat, @RequestParam double lon, Model model, HttpSession session) {
        // Defaults for nearest city comparison
        String currentNearestCity = "";
        double currentSmallestDistance = 0;

        // Loop though available locations and compare them to provided client lat and lon
        for (Location location : locationServices.listAll()) {
            // Distance between client lat/lon and location lat/lon
            double tempDistance = HaversineCalculator.distance(lat, lon, location.getLatitude(), location.getLongitude());

            // Compare new distance to existing smallest distance
            if (currentNearestCity.equals("") || tempDistance < currentSmallestDistance) {
                currentNearestCity = location.getLocationID();
                currentSmallestDistance = tempDistance;
            }
        }

        return loadRecommendations(currentNearestCity, model, session);
    }

    /**
     * Returns a list of recommended locations
     *
     * @param user current user in session
     * @return list of recommended locations
     */
    private List<Location> getRecommendation(UserSession user) {
        if (getCurrentLocation() == null) {
            return null;
        }

        ArrayList<String> ignoredLocations = new ArrayList<>();
        ignoredLocations.add(getCurrentLocation().getLocationID());

        // If user has a search location
        if (user.getLastSearchedDestination() != null) {
            Location lastSearchedLocation = locationServices.findByLocation(user.getLastSearchedDestination());

            List<String> adjacentLocations = lastSearchedLocation.getAdjacentLocations();
            searchRecommendations = new ArrayList<>();

            // Find two adjacent locations to last search location
            for (String locationString : adjacentLocations) {
                // If adjacent location matches current user location ignore it
                if (!locationString.equals(getCurrentLocation().getLocationID())) {
                    ignoredLocations.add(locationString);
                    searchRecommendations.add(locationServices.getById(locationString));
                }
                if (searchRecommendations.size() == 2) break;
            }
        }

        // Get currently popular locations
        List<Location> locations = locationServices.findAllSortedAscendingExcluding(ignoredLocations);

        // Limit locations to top 10 most popular
        locations = locations.stream().limit(10).collect(Collectors.toList());
        // Shuffle top 10
        Collections.shuffle(locations);

        if (user.getLastSearchedDestination() == null) {
            // The final list of recommended locations
            return locations.stream().limit(4).collect(Collectors.toList());
        } else {
            // The final list of recommended locations
            return locations.stream().limit(2).collect(Collectors.toList());
        }
    }

    /**
     * Get current location of user
     *
     * @return Location of user
     */
    private Location getCurrentLocation() {
        // Set current location
        Location currentLocation;
        if (userLocation == null) {
            currentLocation = locationServices.mostPopular();
        } else {
            currentLocation = locationServices.getById(userLocation.getLocationID());
        }

        // If no current location is found in database
        if (currentLocation == null) {
            System.err.println("No current location was found in database");
            return null;
        }

        return currentLocation;
    }

    /**
     * Get current session
     *
     * @param session current session
     * @return current user session
     */
    private UserSession getSession(HttpSession session) {
        UserSession sessionUser = null;
        try {
            sessionUser = (UserSession) session.getAttribute("User");
        } catch (Exception e) {
        }

        if (sessionUser == null) {
            sessionUser = new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }
}
