package lk.nibm.ticketservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "event_id", nullable = false)
    @NotNull
    private int eventId;

    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private int userId;

    @Column(name = "total_tickets", nullable = false)
    @NotNull
    @Min(1)
    private int totalTickets;

    @Column(name = "price", nullable = false)
    @NotNull
    private double totalPrice;

    @Column(name = "booking_date", nullable = false)
    @NotNull
    private LocalDate bookingDate;

    public Ticket() {
        // Initialize bookingDate to the current date
        this.bookingDate = LocalDate.now();
    }
}
