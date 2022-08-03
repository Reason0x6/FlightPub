package com.FlightPub.Services;

import com.FlightPub.model.Plane;
import com.FlightPub.model.TicketClass;
import com.FlightPub.model.TicketType;
import com.FlightPub.repository.PlaneRepo;
import com.FlightPub.repository.TicketClassRepo;
import com.FlightPub.repository.TicketTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PlaneServices")
public class PlaneServices {
    private final PlaneRepo planeRepo;
    private Plane workingPlane;

    @Autowired
    public PlaneServices(PlaneRepo planeRepository) {
        this.planeRepo = planeRepository;
    }

    public int getPlane(String planeID){
        return workingPlane.getEconomy();
    }
}
