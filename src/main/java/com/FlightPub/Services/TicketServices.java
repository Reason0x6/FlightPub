package com.FlightPub.Services;

import com.FlightPub.model.TicketClass;
import com.FlightPub.model.TicketType;
import com.FlightPub.repository.TicketClassRepo;
import com.FlightPub.repository.TicketTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements database interaction for ticketing
 */
@Service("TicketServices")
public class TicketServices {
    private final TicketClassRepo classRepo;
    private final TicketTypeRepo ticketRepo;

    @Autowired
    public TicketServices(TicketClassRepo ticketClassRepository, TicketTypeRepo ticketTypeRepository) {
        this.classRepo = ticketClassRepository;
        this.ticketRepo = ticketTypeRepository;
    }


}
