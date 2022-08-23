package com.FlightPub.Services;

import com.FlightPub.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class ConfirmationEmailService implements EmailService {

    @Autowired private JavaMailSender javaEmailSender;

    @Value("${spring.mail.username}") private String sender;


    @Override
    public String sendSimpleMail(Email email) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(email.getRecipient());
            message.setSubject(email.getEmailSubject());
            message.setText(email.getEmailBody());
            message.setFrom(sender);

            javaEmailSender.send(message);

            return "Booking Confirmation Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending Booking Confirmation Email";
        }
    }

    @Override
    public String sendAttachmentMail(Email email) {
        return null;
    }

    @Override
    public void sendEmail(SimpleMailMessage email) {
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(email.getRecipient());
            mailMessage.setSubject(email.getEmailSubject());
            mailMessage.setText(String.valueOf(email.getTo()));
            mailMessage.setFrom(sender);

            javaEmailSender.send(mailMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
