package lk.nibm.ticketservice.service;

import lk.nibm.ticketservice.model.Ticket;
import lk.nibm.ticketservice.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RestTemplate restTemplate;

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
        // Fetch basic event details
        String eventUrl = "http://localhost:8082/events/" + ticketRequest.getEventId();
        ResponseEntity<Map> eventResponse = restTemplate.getForEntity(eventUrl, Map.class);

        // Extract data you need from the response
        Map<String, Object> eventData = eventResponse.getBody();
        if (eventData == null) {
            throw new RuntimeException("Event not found");
        }
        String eventTitle = (String) eventData.get("title");

        // Check if tickets are available (example logic, adjust as necessary)
        Integer availableTickets = (Integer) eventData.get("availableTickets");
        if (availableTickets == null || availableTickets <= 0) {
            throw new RuntimeException("No tickets available");
        }

        // Create and save the ticket
        Ticket newTicket = new Ticket();
        newTicket.setEventId(ticketRequest.getEventId());
        newTicket.setUserId(ticketRequest.getUserId());
        newTicket.setSeatNumber(ticketRequest.getSeatNumber());
        newTicket.setTotalTickets(ticketRequest.getTotalTickets());
        newTicket.setTotalPrice(ticketRequest.getTotalPrice());
        newTicket.setBookingDate(ticketRequest.getBookingDate());
        newTicket.setEventName(eventTitle);

        return ticketRepository.save(newTicket);
    }
}
