package lk.nibm.bookingservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentResponseDTO {
    private boolean success;
    private String transactionId;
    private Integer id;
}
