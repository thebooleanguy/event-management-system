package lk.nibm.ticketservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "event_id")
    private int eventId;

    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "seat_number")
    private int seatNumber;

    @Column(name = "total_tickets")
    private int totalTickets;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "booking_date")
    private String bookingDate;

    @Column(name = "event_time")
    private String eventTime;

    public Ticket() {
        // Default constructor
    }

    @Override
    public String toString() {
        return String.format(
                "Ticket[id=%d, eventId=%d, paymentId=%d, eventName='%s', userId=%d, venueName='%s', seatNumber=%d, totalTickets=%d, totalPrice=%.2f, bookingDate='%s', eventTime='%s']",
                id, eventId, paymentId, eventName, userId, venueName, seatNumber, totalTickets, totalPrice, bookingDate, eventTime);
    }
}
