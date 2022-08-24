package com.FlightPub.Controllers;

import com.FlightPub.Services.EmailServices;
import com.FlightPub.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired private EmailServices emailServices;

    @PostMapping("/sendEmail")
    public String sendConfirmationEmail(@RequestBody Email email) {
        return emailServices.sendSimpleMail(email);
    }
}
