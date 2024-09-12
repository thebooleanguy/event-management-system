package lk.nibm.eventservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an event entity in the system.
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date")
    private LocalDate date;

    @Column(name = "location")
    private String location;

    @Column(name = "organizer_id")
    private int organizerId;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "available_tickets")
    private int availableTickets;

    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;

    @PrePersist
    public void setDefaults() {
        // Set default image URL if not provided
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            this.imageUrl = "/images/default.jpg";
        }

        // Set default values for available tickets and ticket price
        if (availableTickets <= 0) {
            this.availableTickets = 1; // Default to 1 if not set or set to a non-positive value
        }
        if (ticketPrice == null) {
            this.ticketPrice = BigDecimal.ONE; // Default to 1 if not set
        }
    }

    /**
     * Enumeration for event categories.
     */
    public enum EventCategory {
        MUSIC,
        THEATER,
        CONCERT,
        SPORT,
        CONFERENCE,
        OTHER
    }
}
