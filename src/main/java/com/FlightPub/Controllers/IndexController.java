package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.*;
import com.FlightPub.Services.*;
import com.FlightPub.model.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {

    private UserAccountServices usrServices;
    private LocationServices locationServices;
    private FlightServices flightServices;
    private BookingServices bookingServices;

    private UserGroupServices groupServices;
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

    @Autowired
    @Qualifier(value = "BookingServices")
    public void setBookingServices(BookingServices bookingService) {
        this.bookingServices = bookingService;
    }

    @Autowired
    @Qualifier(value = "UserGroupServices")
    public void setUserGroupServices(UserGroupServices userGroupServices) {
        this.groupServices = userGroupServices;
    }


    @RequestMapping("/")
    public String loadIndex(@ModelAttribute Recommendation recommendation, Model model, HttpSession session) {

        model = addDateAndTimeToModel(model);

        model.addAttribute("usr", getSession(session));

        model.addAttribute("recommendationLocation", locationServices.listAll());

        recommendation.setFlightServices(flightServices);
        recommendation.setLocationServices(locationServices);
        model.addAttribute("reco", recommendation.getRecommendation());
        model.addAttribute("currentLocation", recommendation.getRecommendationLocation());

        return "index";
    }

    @RequestMapping("/login")
    public String loadLogin(Model model, HttpSession session){

        model.addAttribute("usr", getSession(session));
        return "User/login";
    }

    @RequestMapping("/newuser")
    public String user(Model model){
        return "Notifications/newuser";
    }

    @RequestMapping("/Register")
    public String loadRegister(Model model, HttpSession session){

        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("usr", getSession(session));
        return "User/Register";
    }

    @RequestMapping("/logout")
    public String loadLogout(Model model, HttpSession session){
        session.setAttribute("User", new UserSession(null));
        model.addAttribute("usr", getSession(session));
        return "User/login";
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

        return "User/login";
    }

    @RequestMapping("/account")
    public String account(Model model, HttpSession session){
        if(!getSession(session).isLoggedIn()){
            return "redirect:login";
        }


        List<Booking> bookings = bookingServices.getUserBookings(getSession(session).getEmail());
        if(bookings.size() > 0){
            model.addAttribute("bookings", bookings);
            model.addAttribute("flights", flightServices);
        }else{
            model.addAttribute("bookings", null);
        }

        List<UserGroup> groups = groupServices.findGroupsContaining(getSession(session).getEmail());
        List<UserGroup> invitedGroups = groupServices.findInvitedGroupsContaining(getSession(session).getEmail());
        model.addAttribute("groups", groups);
        model.addAttribute("invitedGroups", invitedGroups);

        model.addAttribute("reco", new Recommendation(locationServices, flightServices).getRecommendation());
        model.addAttribute("locs", locationServices.listAll());
        model.addAttribute("usr", getSession(session));
        return "User/Personalised";
    }

    @RequestMapping("/flight") //e.g localhost:8080/location/add?id=Hob&country=Australia&location=Hobart&lat=-42.3&lng=147.3&pop=1
    public String viewFlight(@RequestParam String id, Model model, HttpSession session){

        Flight f = flightServices.getById(id);

        model.addAttribute("Dest", locationServices.getById(f.getDestinationID()));
        model.addAttribute("Dep", locationServices.getById(f.getOriginID()));

        model.addAttribute("Flight", f);
        model.addAttribute("usr", getSession(session));

        return "Flight";
    }

    @RequestMapping("/flight/book") //e.g localhost:8080/flight/book?id=1001&seats=2
    public String bookFlight(@RequestParam String id, @RequestParam Integer seats ,Model model, HttpSession session){

        Flight f = flightServices.getById(id);

        getSession(session).addToCart(seats, id);

        model.addAttribute("Dest", locationServices.getById(f.getDestinationID()));
        model.addAttribute("Dep", locationServices.getById(f.getOriginID()));

        model.addAttribute("Flight", f);
        model.addAttribute("usr", getSession(session));

        return "Flight";
    }

    @PostMapping("/search")
    public String runSearch(@ModelAttribute BasicSearch search, Model model, HttpSession session){
        model = addDateAndTimeToModel(model);
        List<Flight> flights;
        List<SingleStopOver> flights1Stop;
        List<MultiStopOver> flights2Stop;
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        try{
           flights = search.runBasicSearch(search.getStart(), search.getEnd(), false);
           flights1Stop = search.basicSingleStopSearch();
           flights2Stop = search.basicMultiStopSearch();
        }catch (Exception e){
            e.printStackTrace();
            return "index";
        }

        model.addAttribute("search", search);
        model.addAttribute("flights", flights);
        model.addAttribute("flightsSingleStop" , flights1Stop);
        model.addAttribute("flightsMultiStop" , flights2Stop);

        model.addAttribute("usr", getSession(session));
        return "search";
    }

    @PostMapping("/advancedSearch")
    public String runAdvancedSearch(@ModelAttribute BasicSearch search, Model model, HttpSession session)
    {
        model = addDateAndTimeToModel(model);
        List<Flight> flights;
        List<SingleStopOver> flights1Stop = null;
        List<MultiStopOver> flights2Stop = null;
        search.setFlightServices(flightServices);
        search.setLocationServices(locationServices);
        try{
            flights =  search.runAdvancedSearch(this.getSession(session).getUsr());
            if(!search.isDirectFlight()) {
                flights1Stop =  search.advancedSingleStopSearch(this.getSession(session).getUsr());
                flights2Stop =  search.advancedMultiStopSearch(this.getSession(session).getUsr());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }

        model.addAttribute("search", search);
        model.addAttribute("flights", flights);
        model.addAttribute("flights1Stop", flights1Stop);
        model.addAttribute("flights2Stop", flights2Stop);

        model.addAttribute("usr", getSession(session));
        return "search";

        // TODO: Add advanced searches
    }

    @RequestMapping("/cart")
    public String cart(Model model, HttpSession session){
         if(!getSession(session).isLoggedIn()){
            return "redirect:login";
        }

        getSession(session).setFlightServices(flightServices);
        model.addAttribute("usr", getSession(session));
        return "Booking/Cart";
    }

    @PostMapping("/cart")
    public String updateCart(Model model, HttpSession session){
      if(!getSession(session).isLoggedIn()){
            return "redirect:login";
        }

        model.addAttribute("usr", getSession(session));
        return "Booking/Cart";
    }

    @RequestMapping("/checkout")
    public String checkout(Model model, HttpSession session){

        if(!getSession(session).isLoggedIn()){
            return "redirect:login";
        }

        return "Booking/Checkout";
    }

    @RequestMapping("/bookingConfirmation")
    public String bookingConfirmation(Model model){
        return "Confirmations/BookingConfirmation";
    }

    @RequestMapping("/bookingalert")
    public String booking(Model model, HttpSession session){
        if(!getSession(session).isLoggedIn()){
            return "redirect:login";
        }

        model.addAttribute("usr", getSession(session));
        return "Notifications/booking";
    }
    @RequestMapping("/registeredalert")
    public String user(Model model, HttpSession session){
        if(!getSession(session).isLoggedIn()){
            return "redirect:login";
        }

        model.addAttribute("usr", getSession(session));
        return "Notifications/newuser";
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
