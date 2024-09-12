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
     * @param paymentRequest the payment request containing payment details.
     * @return the payment response indicating success or failure.
     */
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        // Logic to process payment
        // Assuming payment is always successful
        Payment payment = new Payment();
        payment.setUserId(paymentRequest.getUserId());
        payment.setBookingId(paymentRequest.getBookingId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setTransactionId("TX12345"); // Should be generated dynamically
        payment.setSuccess(true);

        paymentRepository.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setSuccess(true);
        response.setTransactionId(payment.getTransactionId());
        response.setMessage("Payment successful");

        return response;
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
