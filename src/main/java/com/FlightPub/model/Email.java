package com.FlightPub.model;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @Getter
    @Setter
    private String emailRecipient = "bella_andrews@me.com";

    @Getter
    private String emailSubject = "FlightPub Booking Confirmation";

    @Getter
    @Setter
    private String emailBody = "Thank you for booking with FlightPub. Your booking is confirmed.";
}
