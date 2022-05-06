package com.FlightPub.Controllers;

import com.FlightPub.Services.UserAccountServices;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontEndErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH )
    public String myerror() {
        return "404";
    }

    public String getErrorPath() {
        return "404";
    }


}
