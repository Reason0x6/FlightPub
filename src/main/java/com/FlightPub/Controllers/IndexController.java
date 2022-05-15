package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.BasicSearch;
import com.FlightPub.RequestObjects.LoginRequest;
import com.FlightPub.RequestObjects.UserSession;
import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {

    private UserAccountServices usrServices;
    private LocationServices locationServices;
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

    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationsServices(LocationServices locService) {
        this.locationServices = locService;
    }


    @RequestMapping("/")
    public String loadIndex(Model model, HttpSession session) {

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

        // getRecommendation();
        model.addAttribute("reco", getRecommendation());

        return "index";
    }

    @RequestMapping("/login")
    public String loadLogin(Model model, HttpSession session){

        model.addAttribute("usr", getSession(session));
        return "login";
    }

    @RequestMapping("/Register")
    public String loadRegister(Model model, HttpSession session){

        model.addAttribute("locs", locationServices.listAll());
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

                return "redirect:account";
            }else{
                model.addAttribute("valid", false);
            }

        }catch(Exception e){
            model.addAttribute("valid", false);
        }



        return "login";
    }

    @RequestMapping("/account")
    public String account(Model model, HttpSession session){

        model.addAttribute("reco", getRecommendation());
        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("usr", getSession(session));
        return "Personalised";
    }

    @PostMapping("/advancedSearch")
    public String runAdvancedSearch(@ModelAttribute BasicSearch search, Model model, HttpSession session)
    {
        model = addDateAndTimeToModel(model);
        List<Flight> flights;
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        try{
            flights =  search.runAdvancedSearch(this.getSession(session).getUsr());
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }

        model.addAttribute("search", search);
        model.addAttribute("flights", flights);
        model.addAttribute("usr", getSession(session));
        return "search";
    }
    @PostMapping("/search")
    public String runSearch(@ModelAttribute BasicSearch search, Model model, HttpSession session){
        List<Flight> flights;
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        try{
            flights =  search.runBasicSearch(search.getStart(), search.getEnd());
        }catch (Exception e){
            return "index";
        }

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
        } catch(Exception e){}

        if(sessionUser == null){
            sessionUser =  new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }

    private List<Flight> getRecommendation() {
        // TODO If a user is logged in get their preferred location
        // Set current location
        Location currentLocation = locationServices.getById("SYD");

        // If no current location is found in database
        if (currentLocation == null) {
            System.err.println("No current location was found in database");
            return null;
        }

        // Create new search
        BasicSearch search = new BasicSearch();
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        search.setOriginIn(currentLocation.getLocationID());

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

        // Get currently popular locations
        List<Location> locations = locationServices.findAllSortedDescendingExcluding(currentLocation.getLocationID());

        // TODO maybe do this through a query call
        locations.sort(Comparator.comparing(Location::getPopularity).reversed());

        // Get 1 flight from each popular location
        for (Location popularLocation : locations) {
            // Set search destination to next popular location
            search.setDestinationIn(popularLocation.getLocationID());

            // Find flights that are going from current location to popular location
            try {
                List<Flight> recommendSearch = search.runBasicSearch(today, max);
                // If at least one flight was found add first flight to recommendation list
                if(!recommendSearch.isEmpty()) {
                    recommendedFlights.add(recommendSearch.get(0));
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Once 3 recommended flights have been found break for loop
            if (recommendedFlights.size() == 3) {
                break;
            }
        }


        // TODO remove
        System.out.println("Recommended Flights found:");
        for (Flight flight : recommendedFlights) {
            System.out.printf("FlightID: %s, Flight Origin: %s, Flight Destination: %s %n",flight.getFlightID(), flight.getOriginID(), flight.getDestinationID());
        }

        return recommendedFlights;
    }
    private Model addDateAndTimeToModel(Model model) {
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
        return model;
    }
}