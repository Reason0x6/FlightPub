package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.UserRegister;
import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.LocationServices;
import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.Services.UserGroupServices;
import com.FlightPub.model.Flight;
import com.FlightPub.model.Location;
import com.FlightPub.model.UserAccount;
import com.FlightPub.RequestObjects.UserSession;
import com.FlightPub.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ObjectCreationController {
    private UserAccountServices usrServices;
    private FlightServices flightServices;
    private LocationServices locationServices;
    private UserGroupServices userGroupServices;
    private UserAccount SessionUser;

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    @Autowired
    @Qualifier(value = "FlightServices")
    public void setFlightServices(FlightServices flightService) {
        this.flightServices = flightService;
    }

    @Autowired
    @Qualifier(value = "LocationServices")
    public void setLocationsServices(LocationServices locService) {
        this.locationServices = locService;
    }

    @Autowired
    @Qualifier(value = "UserGroupServices")
    public void setUserGroupServices(UserGroupServices userGroupServices) {
        this.userGroupServices = userGroupServices;
    }


    @RequestMapping("/location/add") //e.g localhost:8080/location/add?id=Hob&country=Australia&location=Hobart&lat=-42.3&lng=147.3&pop=1
    public String addLoc( @RequestParam String id, @RequestParam String country, @RequestParam String location, @RequestParam double lat,
                          @RequestParam double lng, @RequestParam int pop,
                          Model model, HttpSession session){

        Location newLoc = new Location(id, country,location, lat,lng,pop);
        locationServices.saveOrUpdate(newLoc);

        model.addAttribute("addedLoc", newLoc);
        model.addAttribute("usr", getSession(session));

        return "Confirmations/NewLocation";
    }

    @RequestMapping("/usr/add") //e.g localhost:8080/usr/add?id=1&username=Toby&email=tchruches@bigpond.com&prefairport=Syd&password=123
    public String addUSR( @RequestParam String username, @RequestParam String email, @RequestParam String prefairport, @RequestParam String password, Model model, HttpSession session){
        UserAccount newUser = new UserAccount(username,email, prefairport, password, 1);
        usrServices.saveOrUpdate(newUser);

        model.addAttribute("addedUser", newUser);
        model.addAttribute("usr", getSession(session));

        return "Confirmations/NewUser";
    }

    @PostMapping("/RegisterUser")
    public String registerUSR(@ModelAttribute UserRegister newUser, Model model, HttpSession session){
        model.addAttribute("usr", getSession(session));
        if(usrServices.getById(newUser.getEmail()) != null){
            model.addAttribute("exists", "User already exists");
            model.addAttribute("Error", "User already exists");
            return "404";
        }
        else if(newUser.isValid()){
            UserAccount nUser = new UserAccount(newUser.getFirstname(),newUser.getEmail(), newUser.getPassword());
            usrServices.saveOrUpdate(nUser);

            // TODO: Notification of new user account to be sent to newUser.getEmail()

            model.addAttribute("addedUser", nUser);
            return "Confirmations/RegisterUser";
        }

        return "/Register";
    }

    @RequestMapping("/flight/add") //e.g localhost:8080/flight/add?flightID=1021&originID=Syd&destinationID=Tam&airline=QANTAS&departure=202205101132AM&arrival=202202101231PM&flightCode=VH302&ticketprice=112.00
    public String addFlight( @RequestParam String flightID, @RequestParam String originID,
                             @RequestParam String destinationID, @RequestParam String airline,
                             @RequestParam String departure, @RequestParam String arrival,
                             @RequestParam String flightCode, @RequestParam double ticketprice,
                             Model model, HttpSession session){

        Flight newFlight = new Flight(flightID, originID.toUpperCase(),
                destinationID.toUpperCase(), departure, arrival, flightCode, airline,ticketprice);

        if(flightServices.getById(flightID) != null){
            // TODO: Notification of flight detail change to be sent to users
        }

        flightServices.saveOrUpdate(newFlight);
        model.addAttribute("flight", newFlight);
        model.addAttribute("usr", getSession(session));

        return "Confirmations/NewFlight";
    }

    @RequestMapping("/group/add") //e.g localhost:8080/group/add?groupName=group1
    public String addUSR( @RequestParam String groupName, Model model, HttpSession session){
        if(!getSession(session).isLoggedIn()){
            return "login";
        }

        UserGroup newGroup = new UserGroup(getSession(session).getEmail(), groupName);
        userGroupServices.saveUsers(newGroup);

        model.addAttribute("group", newGroup);
        model.addAttribute("usr", getSession(session));

        return "Confirmations/NewGroup";
    }

    private UserSession getSession(HttpSession session){
        UserSession sessionUser;
        try{
            sessionUser = (UserSession) session.getAttribute("User");
        }catch(Exception e){
            sessionUser = null;
        }

        if(sessionUser == null){
            sessionUser =  new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }


}
