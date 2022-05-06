package com.FlightPub.Controllers;

import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    private UserAccountServices usrServices;

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    @RequestMapping("/usr/add")
    public String addUSR(@RequestParam String id, @RequestParam String username, @RequestParam String email, Model mod){
        UserAccount newUser = new UserAccount(id,username,email);
        usrServices.saveOrUpdate(newUser);
        mod.addAttribute("usr", newUser);
        return "basic";
    }


    @RequestMapping("/")
    public String loadIndex(Model model){
        model.addAttribute("usr", "");
        return "index";
    }
}