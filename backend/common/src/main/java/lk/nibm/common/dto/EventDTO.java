package lk.nibm.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class EventDTO {
    private int eventId;
    private int availableTickets;
    private BigDecimal price;
}
