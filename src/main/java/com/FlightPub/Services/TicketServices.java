package com.FlightPub.Services;

import com.FlightPub.model.TicketClass;
import com.FlightPub.model.TicketType;
import com.FlightPub.repository.TicketClassRepo;
import com.FlightPub.repository.TicketTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements database interaction for ticketing
 */
@Service("TicketServices")
public class TicketServices {
    private final TicketClassRepo classRepo;
    private final TicketTypeRepo ticketRepo;

    /**
     * Sets the classes' repository objects
     * @param ticketClassRepository, ticketTypeRepository
     */
    @Autowired
    public TicketServices(TicketClassRepo ticketClassRepository, TicketTypeRepo ticketTypeRepository) {
        this.classRepo = ticketClassRepository;
        this.ticketRepo = ticketTypeRepository;
    }

    /**
     * Gets all ticket classes from the database
     * @return List of ticket classes
     */
    public List<TicketClass> getAllTicketClass() { return classRepo.findAll(); }

    /**
     * Gets all ticket types from the database
     * @return List of ticket types
     */
    public List<TicketType> getAllTicketType() { return ticketRepo.findAll(); }
}
