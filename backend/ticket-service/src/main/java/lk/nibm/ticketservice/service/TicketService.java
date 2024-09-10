package lk.nibm.ticketservice.service;

import lk.nibm.ticketservice.model.Ticket;
import lk.nibm.ticketservice.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${event.service.name}")
    private String eventServiceName;

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

        // Fetch available tickets and unit price from the event-service
        String eventServiceUrl = discoveryClient.getInstances(eventServiceName)
                .stream()
                .findFirst()
                .map(instance -> instance.getUri().toString())
                .orElseThrow(() -> new RuntimeException("Event service not found"));

        ResponseEntity<EventDTO> response = restTemplate.getForEntity(
                String.format("%s/api/events/%d", eventServiceUrl, ticketRequest.getEventId()),
                EventDTO.class
        );
        EventDTO event = response.getBody();

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        if (event.getAvailableTickets() < ticketRequest.getTotalTickets()) {
            throw new RuntimeException("Not enough tickets available");
        }

        // Calculate total price
        BigDecimal unitPrice = event.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(ticketRequest.getTotalTickets()));
        ticketRequest.setTotalPrice(totalPrice.doubleValue());

        // Save the ticket
        return ticketRepository.save(ticketRequest);
    }

    // Define a DTO for the response from the event-service
    private static class EventDTO {
        private int eventId;
        private int availableTickets;
        private BigDecimal price;

        // Getters and setters

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public int getAvailableTickets() {
            return availableTickets;
        }

        public void setAvailableTickets(int availableTickets) {
            this.availableTickets = availableTickets;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
