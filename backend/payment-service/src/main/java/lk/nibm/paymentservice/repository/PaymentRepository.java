package lk.nibm.paymentservice.repository;

import lk.nibm.paymentservice.model.Payment;
import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(int userId);
    List<Payment> findByAmountGreaterThan(BigDecimal amount);
}
