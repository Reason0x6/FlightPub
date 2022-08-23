package com.FlightPub.Controllers;

import com.FlightPub.Services.EmailService;
import com.FlightPub.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class EmailController {

    @Autowired private EmailService emailService;

    @PostMapping("/sendConfirmationEmail")
    public String sendConfirmationEmail(@RequestBody Email email) {
        return emailService.sendSimpleMail(email);
    }
}
