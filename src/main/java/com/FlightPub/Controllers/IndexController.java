package com.FlightPub.Controllers;

import com.FlightPub.Services.UserAccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private UserAccountServices usrServices;

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    @RequestMapping("/")
    public String loadIndex(Model model){
        model.addAttribute("usr", "");
        return "index";
    }
}
