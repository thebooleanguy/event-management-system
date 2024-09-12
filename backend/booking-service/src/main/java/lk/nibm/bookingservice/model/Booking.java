package lk.nibm.bookingservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a booking entity in the system.
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "event_id", nullable = false)
    @NotNull
    private int eventId;

    @Column(name = "payment_id")
    private Integer paymentId;  // Integer allows null values

    @Column(name = "user_id", nullable = false)
    @NotNull
    private int userId;

    @Column(name = "total_tickets", nullable = false)
    @NotNull
    @Min(1)
    private int totalTickets;

    @Column(name = "total_price", nullable = false)
    @NotNull
    private double totalPrice;

    @Column(name = "booking_date", nullable = false)
    @NotNull
    private LocalDate bookingDate;

    public Booking() {
        // Initialize bookingDate to the current date
        this.bookingDate = LocalDate.now();
    }
}
