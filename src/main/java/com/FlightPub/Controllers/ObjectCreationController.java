package com.FlightPub.Controllers;

import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.model.Flight;
import com.FlightPub.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ObjectCreationController {
    private UserAccountServices usrServices;
    private FlightServices flightServices;
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



    @RequestMapping("/usr/add") //e.g localhost:8080/usr/add?id=1&username=Toby&email=tchruches@bigpond.com&password=123
    public String addUSR( @RequestParam String username, @RequestParam String email, @RequestParam String password, Model mod){
        UserAccount newUser = new UserAccount(username,email, password, 1);
        usrServices.saveOrUpdate(newUser);
        mod.addAttribute("usr", newUser);
        return "basic";
    }

    @RequestMapping("/flight/add") //e.g localhost:8080/flight/add?flightID=1021&originID=Syd&desinationID=Tam&airline=QANTAS
    public String addFlight( @RequestParam int flightID, @RequestParam String originID,
                             @RequestParam String destinationID, @RequestParam String airline, Model mod){

        Flight newFlight = new Flight();

        newFlight.setFlightID(flightID);
        newFlight.setOriginID(originID);
        newFlight.setDestinationID(destinationID);;
        newFlight.setAirline(airline);

        flightServices.saveOrUpdate(newFlight);
        return "basic";
    }


}
