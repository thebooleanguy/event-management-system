package lk.nibm.bookingservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * A DTO class representing the booking request, including event, user details, and payment info if needed.
 */
@Data
@NoArgsConstructor
public class BookingDTO {
    private int eventId;
    private int userId;
    private int totalTickets;
    private String paymentMethod; // Optional, in case the user provides payment details
    private BigDecimal totalPrice; // Calculated by the service
}
