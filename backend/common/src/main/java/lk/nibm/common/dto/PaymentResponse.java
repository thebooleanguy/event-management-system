package lk.nibm.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentResponse {
    private boolean success;
    private String transactionId;
    private String message; // Message about the payment process (e.g., error messages)
}
