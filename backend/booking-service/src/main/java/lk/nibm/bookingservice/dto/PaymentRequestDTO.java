package lk.nibm.bookingservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentRequestDTO {
    private int bookingId;
    private int userId;
    private BigDecimal amount;
    private String paymentMethod; // E.g., "Credit Card", "PayPal", etc.
}
