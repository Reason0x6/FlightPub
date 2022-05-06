package com.FlightPub.Controllers;

import com.FlightPub.Services.UserAccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {
    private UserAccountServices usrServices;

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }



    @RequestMapping("/hi")
    public String listUsers(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model){

        model.addAttribute("hi", name);
        return "list";
    }
}
