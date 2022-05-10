package com.FlightPub.Controllers;

import com.FlightPub.Services.FlightServices;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String today = dateFormat.format(date);

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

    @RequestMapping("/logout")
    public String loadLogout(Model model, HttpSession session){
        session.setAttribute("User", new UserSession(null));
        model.addAttribute("usr", getSession(session));
        return "login";
    }

    @PostMapping("/login")
    public String runLogin(@ModelAttribute LoginRequest req, Model model, HttpSession session){
        try {

            UserAccount newUser = usrServices.getById(req.getEmail());

            if(req.getPassword().equals(newUser.getPassword())) {
                System.out.println(true);
                model.addAttribute("method", "post");
                UserSession usr = new UserSession(newUser);
                session.setAttribute("User", usr);
                model.addAttribute("usr", usr);
            }else{
                System.out.println(false);
                System.out.println(req.getPassword());
                System.out.println(newUser.getPassword());

                // TODO: Add a 'forgot password' workflow

                model.addAttribute("valid", false);
            }

            model.addAttribute("user", req);

        }catch(Exception e){
            System.out.println(req.getPassword());
            e.printStackTrace();
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
        UserSession sessionUser = (UserSession) session.getAttribute("User");
        if(sessionUser == null){
            sessionUser =  new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }
}
