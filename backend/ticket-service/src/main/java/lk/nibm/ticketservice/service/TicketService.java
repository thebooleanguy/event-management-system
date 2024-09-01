package lk.nibm.ticketservice.service;

import lk.nibm.ticketservice.model.AvailableTickets;
import lk.nibm.ticketservice.model.Ticket;
import lk.nibm.ticketservice.repository.AvailableTicketsRepository;
import lk.nibm.ticketservice.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AvailableTicketsRepository availableTicketsRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(int id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByUser(int userId) {
        return ticketRepository.findByUserId(userId);
    }

    public void cancelTicket(int id) {
        ticketRepository.deleteById(id);
    }

    public Ticket bookTicket(Ticket ticketRequest) {
        // Validate input
        if (ticketRequest.getEventId() == 0 || ticketRequest.getUserId() == 0) {
            throw new RuntimeException("Event ID and User ID must be provided");
        }

        // Fetch available tickets and unit price for the event
        AvailableTickets availableTickets = availableTicketsRepository.findById(ticketRequest.getEventId()).orElse(null);
        if (availableTickets == null) {
            throw new RuntimeException("Event not found");
        }

        if (availableTickets.getAvailableTickets() < ticketRequest.getTotalTickets()) {
            throw new RuntimeException("Not enough tickets available");
        }

        // Calculate total price
        BigDecimal unitPrice = availableTickets.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(ticketRequest.getTotalTickets()));
        ticketRequest.setTotalPrice(totalPrice.doubleValue());

        // Reduce available tickets
        availableTickets.setAvailableTickets(availableTickets.getAvailableTickets() - ticketRequest.getTotalTickets());
        availableTicketsRepository.save(availableTickets); // Save updated available tickets

        // Save the ticket
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

    public BigDecimal getUnitPrice(int eventId) {
        AvailableTickets availableTickets = availableTicketsRepository.findById(eventId).orElse(null);
        if (availableTickets == null) {
            throw new RuntimeException("Event not found");
        }
        return availableTickets.getPrice();
    }

    public void setUnitPrice(int eventId, BigDecimal unitPrice) {
        AvailableTickets eventTickets = availableTicketsRepository.findById(eventId).orElse(null);
        if (eventTickets == null) {
            eventTickets = new AvailableTickets();
            eventTickets.setEventId(eventId);
        }
        eventTickets.setPrice(unitPrice);
        availableTicketsRepository.save(eventTickets);
    }

    public AvailableTickets createAvailableTickets(AvailableTickets availableTickets) {
        return availableTicketsRepository.save(availableTickets);
    }
}
