package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.Recommendation;
import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.LocationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String loadRecommendations(@RequestParam("city") String city, Model model) {
        recommendation = new Recommendation(locationServices, flightServices);
        recommendation.setRecommendationLocation(city);

        model.addAttribute("reco", recommendation.getRecommendation());
        model.addAttribute("currentLocation", recommendation.getRecommendationLocation());
        model.addAttribute("recommendationLocation", locationServices.listAll());

        return "Fragments/Recommendation :: recommendation_fragment";
    }
}
