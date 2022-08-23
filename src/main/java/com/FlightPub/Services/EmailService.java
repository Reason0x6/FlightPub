package com.FlightPub.Services;

import com.FlightPub.model.Email;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    String sendSimpleMail(Email email);

    String sendAttachmentMail(Email email);

    void sendEmail(SimpleMailMessage message);
}
