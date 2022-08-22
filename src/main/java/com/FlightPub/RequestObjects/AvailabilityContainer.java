package com.FlightPub.RequestObjects;

import com.FlightPub.model.Price;
import lombok.Getter;
import lombok.Setter;

public class PriceContainer {
    @Getter
    @Setter
    private Price price;

    @Getter
    @Setter
    private String className;

    @Getter
    @Setter
    private String ticketName;

    public PriceContainer() {}

    public PriceContainer(Price price, String className, String ticketName) {
        this.price = price;
        this.className = className;
        this.ticketName = ticketName;
    }
}
