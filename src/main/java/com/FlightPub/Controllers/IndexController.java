package com.FlightPub.Controllers;

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

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    @RequestMapping("/usr/add") //e.g localhost:8080/usr/add?id=1&username=Toby&email=tchruches@bigpond.com
    public String addUSR(@RequestParam String id, @RequestParam String username, @RequestParam String email, Model mod){
        UserAccount newUser = new UserAccount(id,username,email);
        usrServices.saveOrUpdate(newUser);
        mod.addAttribute("usr", newUser);
        return "basic";
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
        model.addAttribute("user", req);
        model.addAttribute("method", "post");
        System.out.println(req.getPassword());
        return "login";
    }


    @PostMapping("/search")
    public String runSearch(@ModelAttribute BasicSearch search, Model model){
        model.addAttribute("search", search);
        return "search";
    }
}
