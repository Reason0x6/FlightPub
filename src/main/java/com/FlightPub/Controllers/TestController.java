package com.FlightPub.Controllers;

import com.FlightPub.Services.UserAccountServices;
import com.FlightPub.repository.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class TestController {
    private UserAccountServices usrServices;

    @Autowired
    @Qualifier(value = "UserAccountServices")
    public void setUserService(UserAccountServices usrService) {
        this.usrServices = usrService;
    }

    @RequestMapping("/")
    public String redirToUSers(){
        return "redirect:/users/list";
    }

    @RequestMapping("/users/list")
    public String listUsers(Model model){
        model.addAttribute("usrs", usrServices.listAll());
        return "users/list";
    }
}
