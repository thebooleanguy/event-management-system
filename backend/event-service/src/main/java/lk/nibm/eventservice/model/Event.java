package lk.nibm.eventservice.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name ="events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private  int id;

    @Column(name="title")
    private String title;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;

    @Column(name="date")
    private LocalDate date;

    @Column(name="location")
    private String location;

    @Column(name="organizerId")
    private  int organizerId;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Column(name="imageUrl")
    private String imageUrl;

    @Column(name = "available_tickets")
    private int availableTickets;

    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;

    public  Event() {

    }

    public enum EventCategory {
        MUSIC,
        THEATER,
        CONCERT,
        SPORT,
        CONFERENCE,
        OTHER
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @PrePersist
    public void setDefaults() {
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            this.imageUrl = "/images/default.jpg";
        }

        if (availableTickets == 0) {
            this.availableTickets = 1; // Default to 1
        }
        if (ticketPrice == null) {
            this.ticketPrice = BigDecimal.ONE; // Default to 1
        }
    }

}
