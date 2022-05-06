package com.FlightPub.Controllers;

import com.FlightPub.Services.UserAccountServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class TestController {
    private UserAccountServices usrServices;

    @RequestMapping("/")
    public String redirToUSers(){
        return "redirect:/users/list";
    }

    @RequestMapping({"/users/list", "/users"})
    public String listUsers(){
       // model.addAttribute("products", usrServices.listAll());
        return usrServices.listAll().toString();
    }
}
