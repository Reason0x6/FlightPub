package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.BasicSearch;
import com.FlightPub.RequestObjects.UserSession;
import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RecommendationController {
    private Location recommendationLocation;
    private FlightServices flightServices;
    private LocationServices locationServices;

    private List<Location> searchRecommendations;

    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationsServices(LocationServices locService) {
        this.locationServices = locService;
    }

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    @PostMapping("/recommendations")
    public String loadRecommendations(@RequestParam String city, Model model, HttpSession session) {
        recommendationLocation = locationServices.getById(city);

        model.addAttribute("reco", getRecommendation(getSession(session)));

        if (getSession(session).getLastSearchedDestination() != null) {
            model.addAttribute("searchRecommendation", searchRecommendations.toArray());
        }

        model.addAttribute("currentLocation", getRecommendationLocation());
        model.addAttribute("recommendationLocation", locationServices.listAll());

        model.addAttribute("usr", getSession(session));

        return "Fragments/Recommendation :: recommendation_fragment";
    }


    @PostMapping("LocateNearestCity")
    public String locateNearestCity(@RequestParam double lat, @RequestParam double lon, Model model, HttpSession session) {
        // Defaults for nearest city comparison
        String currentNearestCity = "";
        double currentSmallestDistance = 0;

        // Loop though available locations and compare them to provided client lat and lon
        for (Location location: locationServices.listAll()) {
            // Distance between client lat/lon and location lat/lon
            double tempDistance = distance(lat, lon, location.getLatitude(), location.getLongitude());

            // Compare new distance to existing smallest distance
            if(currentNearestCity.equals("") || tempDistance < currentSmallestDistance) {
                currentNearestCity = location.getLocationID();
                currentSmallestDistance = tempDistance;
            }
        }

        return loadRecommendations(currentNearestCity, model, session);
    }

    private List<Location> getRecommendation(UserSession user) {
        // TODO Remove if not needed
//        return searchForRecommendedFlights(currentLocation);

        if(getCurrentLocation() == null) {
            return null;
        }

        ArrayList<String> ignoredLocations = new ArrayList<>();
        ignoredLocations.add(getCurrentLocation().getLocationID());

        // need to get adjacent locations to
        if (user.getLastSearchedDestination() != null) {
            Location lastSearchedLocation = locationServices.findByLocation(user.getLastSearchedDestination());

            List<String> adjacentLocations = lastSearchedLocation.getAdjacentLocations();
            searchRecommendations =  new ArrayList<>();
            for(String locationString: adjacentLocations) {
                if (!locationString.equals(getCurrentLocation().getLocationID())) {
                    ignoredLocations.add(locationString);
                    searchRecommendations.add(locationServices.getById(locationString));
                }
                if (searchRecommendations.size() == 2) break;
            }
        }

        // Get currently popular locations
        List<Location> locations = locationServices.findAllSortedAscendingExcluding(ignoredLocations);

        // TODO add as separate commit
//        // Limit locations to top 10 most popular
//        locations = locations.stream().limit(10).collect(Collectors.toList());
//        // Shuffle top 10
//        Collections.shuffle(locations);

        // Also ensure that normal recommendations dont have duplicates
        // Also ensure that current location is skipped

        if (user.getLastSearchedDestination() == null) {
            // The final list of recommended locations
            return locations.stream().limit(4).collect(Collectors.toList());
        } else {
            // The final list of recommended locations
            return locations.stream().limit(2).collect(Collectors.toList());
        }
    }

    private List<Flight> searchForRecommendedFlights(Location currentLocation) {
        // Get currently popular locations
        List<Location> locations = locationServices.findAllSortedAscendingExcluding(Collections.singletonList(currentLocation.getLocationID()));

        // The final list of recommended flights
        List<Flight> recommendedFlights = new LinkedList<>();

        // Get current date and date in 3 months
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String today = dateFormat.format(date);

        cal.add(Calendar.MONTH, 3);
        date = cal.getTime();
        String max = dateFormat.format(date);

        // Create new search
        BasicSearch search = new BasicSearch();
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        search.setOriginIn(currentLocation.getLocationID());

        // Get 1 flight from each popular location
        for (Location popularLocation : locations) {

            // Set search destination to next popular location
            search.setDestinationIn(popularLocation.getLocationID());

            // Find flights that are going from current location to popular location
            try {
                // TODO look into why this doesnt set the destination correctly
                // List<Flight> recommendSearch = search.runBasicSearch(today, max, false);
                List<Flight> recommendSearch = flightServices.getByOriginAndDestination(
                        currentLocation.getLocationID(),
                        popularLocation.getLocationID(),
                        Flight.stringToLong(today),
                        Flight.stringToLong(max));
                // If at least one flight was found add first flight to recommendation list
                if(!recommendSearch.isEmpty()) {
                    recommendedFlights.add(recommendSearch.get(0));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Once 3 recommended flights have been found break for loop
            if (recommendedFlights.size() == 4) {
                break;
            }
        }

        return recommendedFlights;
    }

    private Location getCurrentLocation() {
        // Set current location
        Location currentLocation;
        if(recommendationLocation == null) {
            currentLocation = locationServices.mostPopular();
        } else {
            currentLocation = locationServices.getById(recommendationLocation.getLocationID());
        }

        // If no current location is found in database
        if (currentLocation == null) {
            System.err.println("No current location was found in database");
            return null;
        }

        return currentLocation;
    }

    private Location getRecommendationLocation() {
        if(recommendationLocation == null) {
            recommendationLocation = locationServices.mostPopular();
        }
        return recommendationLocation;
    }

    // --- Haversine formula for finding distances between two coordinates ---
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    private UserSession getSession(HttpSession session){
        UserSession sessionUser = null;
        try{
            sessionUser = (UserSession) session.getAttribute("User");
        } catch(Exception e){}

        if(sessionUser == null){
            sessionUser =  new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }
}
