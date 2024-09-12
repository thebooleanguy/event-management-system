package lk.nibm.paymentservice.controller;

import lk.nibm.common.dto.PaymentRequest;
import lk.nibm.common.dto.PaymentResponse;
import lk.nibm.paymentservice.model.Payment;
import lk.nibm.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // -----------------------------------------------------------
    // Standard CRUD Endpoints
    // -----------------------------------------------------------

    /**
     * Processes a payment.
     *
     * @param payment the payment entity containing payment details.
     * @return a response indicating success or failure.
     */
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) {
        try {
            Payment processedPayment = paymentService.processPayment(payment);
            return ResponseEntity.ok(processedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Payment());
        }
    }

    /**
     * Retrieve all payments.
     *
     * @return list of all payments.
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Retrieve a payment by ID.
     *
     * @param id the ID of the payment to retrieve.
     * @return the payment if found, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update an existing payment.
     *
     * @param id the ID of the payment to update.
     * @param updatedPayment the payment object with updated data.
     * @return the updated payment if found and updated, or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment updatedPayment) {
        Optional<Payment> payment = paymentService.updatePayment(id, updatedPayment);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a payment by ID.
     *
     * @param id the ID of the payment to delete.
     * @return 204 No Content if deletion is successful, or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long id) {
        paymentService.cancelPayment(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------------------------------------------
    // Custom Endpoints
    // -----------------------------------------------------------

    /**
     * Retrieve payments by user ID.
     *
     * @param userId the ID of the user whose payments to retrieve.
     * @return list of payments for the specified user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUser(@PathVariable int userId) {
        List<Payment> payments = paymentService.getPaymentsByUser(userId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Retrieve payments with amount greater than the specified value.
     *
     * @param amount the amount threshold.
     * @return list of payments with amount greater than the specified value.
     */
    @GetMapping("/amountGreaterThan/{amount}")
    public ResponseEntity<List<Payment>> getPaymentsByAmountGreaterThan(@PathVariable BigDecimal amount) {
        List<Payment> payments = paymentService.getPaymentsByAmountGreaterThan(amount);
        return ResponseEntity.ok(payments);
    }
}
