package com.FlightPub.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Notification")
public class Notification {
    @Id
    @Getter
    @Setter
    private String notificationID;

    @Getter
    @Setter
    private String FlightID;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private String notificationType;

    @Getter
    @Setter
    private int dateTime;
}
