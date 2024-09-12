package lk.nibm.paymentservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents a payment entity in the system.
 */
@Data
@Table(name = "payments")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "booking_id", nullable = false)
    private int bookingId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false) // (e.g., Credit Card, PayPal)
    private String paymentMethod;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "success", nullable = false)
    private boolean success;
}
