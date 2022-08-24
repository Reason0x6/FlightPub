package com.FlightPub.Services;

import com.FlightPub.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Service class for sending emails
 */
@Service("EmailServices")
public class EmailServices {

    @Autowired private JavaMailSender javaEmailSender;

    @Value("${spring.mail.username}") private String sender;

    /**
     * Sends a booking confirmation email to the user
     * @param email to send confirmation to
     * @return string response of email success or error
     */
    public String sendSimpleMail(Email email) {
        try {

            SimpleMailMessage bookingConfirmation = new SimpleMailMessage();

            bookingConfirmation.setTo(email.getEmailRecipient());
            bookingConfirmation.setSubject(email.getEmailSubject());
            bookingConfirmation.setText(email.getEmailBody());
            bookingConfirmation.setFrom(sender);

            javaEmailSender.send(bookingConfirmation);

            return "Booking Confirmation Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending Booking Confirmation Email";
        }
    }
}
