package com.FlightPub.Controllers;

import com.FlightPub.RequestObjects.UserSession;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Implements error handling functionality
 */
@Controller
public class FrontEndErrorController implements ErrorController {

    private static final String PATH = "/error";

    /**
     * Returns a html fragment containing error message
     *
     * @param model   interface that defines a holder for model attributes
     * @param session current session
     * @return html error fragment
     */
    @RequestMapping(value = PATH)
    public String myerror(Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("Error", "General Error");
        return "Error/404";
    }

    /**
     * Returns the path of the error page
     * @return path of the error page
     */
    public String getErrorPath(Model model, HttpSession session) {
        model.addAttribute("usr", getSession(session));
        model.addAttribute("Error", "General Error");
        return "404";
    }

    /**
     * Returns the session from the current request
     * @return session from the current request
     */
    private UserSession getSession(HttpSession session) {
        UserSession sessionUser = null;
        try {
            sessionUser = (UserSession) session.getAttribute("User");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sessionUser == null) {
            sessionUser = new UserSession(null);
            session.setAttribute("User", sessionUser);
        }

        return sessionUser;
    }
}
