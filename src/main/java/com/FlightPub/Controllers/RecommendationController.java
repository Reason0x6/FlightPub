package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.Recommendation;
import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
public class RecommendationController {
    private Recommendation recommendation;
    private FlightServices flightServices;
    private LocationServices locationServices;

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
    public String loadRecommendations(@RequestParam String city, Model model) {
        recommendation = new Recommendation(locationServices, flightServices);
        recommendation.setRecommendationLocation(city);

        model.addAttribute("reco", recommendation.getRecommendation());
        model.addAttribute("currentLocation", recommendation.getRecommendationLocation());
        model.addAttribute("recommendationLocation", locationServices.listAll());

        return "Fragments/Recommendation :: recommendation_fragment";
    }

    @PostMapping("LocateNearestCity")
    public String locateNearestCity(@RequestParam double lat, @RequestParam double lon, Model model) {
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

        return loadRecommendations(currentNearestCity, model);
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
}
