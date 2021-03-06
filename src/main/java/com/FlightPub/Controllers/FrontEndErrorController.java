package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.UserSession;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class FrontEndErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH )
    public String myerror(Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("Error", "General Error");
        return "Error/404";
    }

    public String getErrorPath(Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("Error", "General Error");
        return "404";
    }
    private UserSession getSession(HttpSession session){
        UserSession sessionUser = null;
        try{
            sessionUser = (UserSession) session.getAttribute("User");
        }catch(Exception e){
            e.printStackTrace();
        }

        if(sessionUser == null){
            sessionUser =  new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }

}
