package lk.nibm.paymentservice.service;

import lk.nibm.common.dto.PaymentRequest;
import lk.nibm.common.dto.PaymentResponse;
import lk.nibm.paymentservice.model.Payment;
import lk.nibm.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;


    // -----------------------------------------------------------
    // Standard CRUD Methods
    // -----------------------------------------------------------

    /**
     * Processes a payment.
     *
     * @param payment the payment entity containing payment details.
     * @return the payment entity indicating success or failure.
     */
    public Payment processPayment(Payment payment) {
        // Generate a unique transaction ID using UUID for this scenario
        // But this should actually be generated dynamically from a payment gateway
        String transactionId = "TX-" + UUID.randomUUID().toString();
        payment.setTransactionId(transactionId);
        // Assume a payment is always successful in this scenario
        payment.setSuccess(true);

        // Save the payment with the generated transaction ID
        return paymentRepository.save(payment);
    }

    /**
     * Retrieve all payments.
     *
     * @return list of all payments.
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Retrieve a payment by ID.
     *
     * @param id the ID of the payment to retrieve.
     * @return the payment if found, Optional.empty() otherwise.
     */
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    /**
     * Update an existing payment.
     *
     * @param id the ID of the payment to update.
     * @param updatedPayment the payment object with updated data.
     * @return the updated payment if found and updated, Optional.empty() otherwise.
     */
    public Optional<Payment> updatePayment(Long id, Payment updatedPayment) {
        if (!paymentRepository.existsById(id)) {
            return Optional.empty();
        }
        updatedPayment.setId(id);
        return Optional.of(paymentRepository.save(updatedPayment));
    }

    /**
     * Delete a payment by ID.
     *
     * @param id the ID of the payment to delete.
     */
    public void cancelPayment(Long id) {
        paymentRepository.deleteById(id);
    }

    // -----------------------------------------------------------
    // Custom Methods
    // -----------------------------------------------------------

    /**
     * Retrieve payments by user ID.
     *
     * @param userId the ID of the user whose payments to retrieve.
     * @return list of payments for the specified user.
     */
    public List<Payment> getPaymentsByUser(int userId) {
        return paymentRepository.findByUserId(userId);
    }

    /**
     * Retrieve payments with amount greater than the specified value.
     *
     * @param amount the amount threshold.
     * @return list of payments with amount greater than the specified value.
     */
    public List<Payment> getPaymentsByAmountGreaterThan(BigDecimal amount) {
        return paymentRepository.findByAmountGreaterThan(amount);
    }

}
