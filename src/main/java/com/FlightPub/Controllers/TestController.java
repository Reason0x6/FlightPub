package com.FlightPub.Controllers;

import com.FlightPub.Services.UserAccountServices;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public class TestController {
    private UserAccountServices usrServices;

    @RequestMapping("/")
    public String redirToList(){
        return "redirect:/users/list";
    }

    @RequestMapping({"/users/list", "/users"})
    public String listProducts(Model model){
        model.addAttribute("products", usrServices.listAll());
        return "users/list";
    }
}
