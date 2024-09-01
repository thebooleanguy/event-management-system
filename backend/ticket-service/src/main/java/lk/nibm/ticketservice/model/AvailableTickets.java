package lk.nibm.ticketservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "available_tickets")
@Getter
@Setter
public class AvailableTickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventId;

    @Column(name = "available_tickets")
    private int availableTickets;

    @Column(name = "price", nullable = false) // New field for the unit price of a ticket
    private BigDecimal price;

    public AvailableTickets() {
        // Default constructor
    }
}