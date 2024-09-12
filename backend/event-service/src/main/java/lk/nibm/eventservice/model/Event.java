package lk.nibm.eventservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "location")
    private String location;

    @Column(name = "organizerId")
    private int organizerId;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "available_tickets")
    private int availableTickets;

    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;

    @PrePersist
    public void setDefaults() {
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            this.imageUrl = "/images/default.jpg";
        }

        if (availableTickets <= 0) {
            this.availableTickets = 1; // Default to 1
        }
        if (ticketPrice == null) {
            this.ticketPrice = BigDecimal.ONE; // Default to 1
        }
    }

    public enum EventCategory {
        MUSIC,
        THEATER,
        CONCERT,
        SPORT,
        CONFERENCE,
        OTHER
    }
}
