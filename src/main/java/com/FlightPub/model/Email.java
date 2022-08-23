package com.FlightPub.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    private String recipient;
    private String emailBody;
    private String emailSubject;
    private String attachment;
}
