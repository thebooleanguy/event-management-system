package lk.nibm.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentRequest {
    private int bookingId;
    private BigDecimal amount;
    private String paymentMethod; // E.g., "Credit Card", "PayPal", etc.
}
