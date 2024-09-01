package lk.nibm.ticketservice.service;

import lk.nibm.ticketservice.model.AvailableTickets;
import lk.nibm.ticketservice.model.Ticket;
import lk.nibm.ticketservice.repository.AvailableTicketsRepository;
import lk.nibm.ticketservice.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;

import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private AvailableTicketsRepository availableTicketsRepository;



//    public Ticket createTickets(Ticket ticket){
//        return ticketRepository.save(ticket);
//    }

    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(int id){

        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByUser(int userId) {
        return ticketRepository.findByUserId(userId);
    }

    public void cancelTicket( int id) {
        ticketRepository.deleteById(id);
    }

    public Ticket bookTicket(Ticket ticketRequest) {
        // Validate input
        if (ticketRequest.getEventId() == 0 || ticketRequest.getUserId() == 0) {
            throw new RuntimeException("Event ID and User ID must be provided");
        }

        // Fetch available tickets for the event
        AvailableTickets availableTickets = availableTicketsRepository.findById(ticketRequest.getEventId()).orElse(null);
        if (availableTickets == null) {
            throw new RuntimeException("Event not found");
        }

        if (availableTickets.getAvailableTickets() < ticketRequest.getTotalTickets()) {
            throw new RuntimeException("Not enough tickets available");
        }

        // Reduce available tickets
        availableTickets.setAvailableTickets(availableTickets.getAvailableTickets() - ticketRequest.getTotalTickets());
        availableTicketsRepository.save(availableTickets); // Save updated available tickets

        // Save the ticket
        ticketRequest.setEventName(""); // Set event name as required or fetch from other sources
        return ticketRepository.save(ticketRequest);
    }

    public int getAvailableTickets(int eventId) {
        AvailableTickets availableTickets = availableTicketsRepository.findById(eventId).orElse(null);
        if (availableTickets == null) {
            throw new RuntimeException("Event not found");
        }
        return availableTickets.getAvailableTickets();
    }

    public void setAvailableTickets(int eventId, int availableTickets) {
        AvailableTickets eventTickets = availableTicketsRepository.findById(eventId).orElse(null);
        if (eventTickets == null) {
            eventTickets = new AvailableTickets();
            eventTickets.setEventId(eventId);
        }
        eventTickets.setAvailableTickets(availableTickets);
        availableTicketsRepository.save(eventTickets);
    }
}
