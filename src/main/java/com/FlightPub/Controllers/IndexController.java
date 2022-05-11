package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.BasicSearch;
import com.FlightPub.RequestObjects.LoginRequest;
import com.FlightPub.RequestObjects.UserSession;
import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.model.*;
import com.sun.tools.javac.file.Locations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {

    private UserAccountServices usrServices;

    private FlightServices flightServices;

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    @RequestMapping("/")
    public String loadIndex(Model model, HttpSession session){

        // Get server time for flight date pickers
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String today = dateFormat.format(date);

        // get server time + 1 year for current max future booking date
        model.addAttribute("today", today); // Temp/placeholder
        cal.add(Calendar.YEAR, 1);
        date = cal.getTime();
        String max = dateFormat.format(date);
        model.addAttribute("max", max);

        model.addAttribute("usr", getSession(session));
        return "index";
    }

    @RequestMapping("/login")
    public String loadLogin(Model model, HttpSession session){

        model.addAttribute("usr", getSession(session));
        return "login";
    }

    @RequestMapping("/Register")
    public String loadRegister(Model model, HttpSession session){

        model.addAttribute("usr", getSession(session));
        return "Register";
    }

    @RequestMapping("/logout")
    public String loadLogout(Model model, HttpSession session){
        session.setAttribute("User", new UserSession(null));
        model.addAttribute("usr", getSession(session));
        return "login";
    }

    @PostMapping("/login")
    public String runLogin(@ModelAttribute LoginRequest req, Model model, HttpSession session){

        model.addAttribute("usr", getSession(session));
        try {

            UserAccount newUser = usrServices.getById(req.getEmail());

            if(req.getPassword().equals(newUser.getPassword())) {
                // Set post flag
                model.addAttribute("method", "post");

                // Set user session
                UserSession usr = new UserSession(newUser);
                session.setAttribute("User", usr);
                model.addAttribute("usr", usr);
            }else{

                model.addAttribute("valid", false);
            }

            model.addAttribute("user", req);

        }catch(Exception e){

            model.addAttribute("valid", false);

        }

        return "login";
    }

    @PostMapping("/search")
    public String runSearch(@ModelAttribute BasicSearch search, Model model, HttpSession session){

        search.setFlightServices(flightServices);
        List<Flight> flights = search.runBasicSearch();
        model.addAttribute("search", search);
        model.addAttribute("flights", flights);


        model.addAttribute("usr", getSession(session));
        return "search";

        // TODO: Add advanced searches
    }


    private UserSession getSession(HttpSession session){
        UserSession sessionUser = null;
        try{
           sessionUser = (UserSession) session.getAttribute("User");
        }catch(Exception e){}

        if(sessionUser == null){
            sessionUser =  new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }

    private List<Flight> getRecommendation() {
        // TODO work out how to get this dynamically
        //  Either though geolocation
        // Set current location
        // TODO integrate with actual Location Service
//        Location currentLocation = LocationServices.getLocationId("Syd");

        // Get currently popular locations
        // TODO see previous
//        List<Location> locations = Locations.listAll();
        List<Location> locations = new LinkedList<>();

        List<Location> popularLocations = new LinkedList<>();
        // Get top 3 locations
        for (int i = 0; i < 3; i++) {
            popularLocations.add(locations.get(i));
        }

        // Find flights that match that popular location from current location
        // Create new search
        BasicSearch search = new BasicSearch();
        // TODO see previous
//        search.setDeparture(currentLocation.getLocationID());

        List<Flight> recommendedFlights = new LinkedList<>();

        // Get 1 flight from each popular location
        for (Location popularLocation : popularLocations) {
            // TODO handle not finding flights
            search.setDestination(popularLocation.getLocationID());
            recommendedFlights.add(search.runBasicSearch().get(0));
        }

        return recommendedFlights;
    }
}
