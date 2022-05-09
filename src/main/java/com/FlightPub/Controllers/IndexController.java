package com.FlightPub.Controllers;

import com.FlightPub.Services.FlightServices;
import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.model.BasicSearch;
import com.FlightPub.model.LoginRequest;
import com.FlightPub.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    private UserAccountServices usrServices;
    private UserAccount SessionUser;
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
    public String loadIndex(Model model){
        model.addAttribute("usr", ""); // Temp/placeholder
        return "index";
    }

    @RequestMapping("/login")
    public String loadLogin(Model model){
        model.addAttribute("usr", ""); // Temp/placeholder
        return "login";
    }

    @PostMapping("/login")
    public String runLogin(@ModelAttribute LoginRequest req, Model model){
        try {

            UserAccount newUser = usrServices.getById(req.getEmail());

            if(req.getPassword().equals(newUser.getPassword())) {
                System.out.println(true);
            }else{
                System.out.println(false);
                System.out.println(req.getPassword());
                System.out.println(newUser.getPassword());
            }

            model.addAttribute("user", req);
            model.addAttribute("method", "post");


        }catch(Exception e){
            System.out.println(req.getPassword());

            e.printStackTrace();

        }

        return "login";
    }


    @PostMapping("/search")
    public String runSearch(@ModelAttribute BasicSearch search, Model model){
        search.setFlightServices(flightServices);
        model.addAttribute("search", search);
        return "search";
    }
}
