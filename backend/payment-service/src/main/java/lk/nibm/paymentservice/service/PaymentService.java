package lk.nibm.paymentservice.service;

import lk.nibm.common.dto.PaymentRequest;
import lk.nibm.common.dto.PaymentResponse;
import lk.nibm.paymentservice.model.Payment;
import lk.nibm.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        // Logic to process payment
        // For simplicity, let's assume payment is always successful
        Payment payment = new Payment();
        payment.setBookingId(paymentRequest.getBookingId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setTransactionId("TX12345"); // This should be generated dynamically
        payment.setSuccess(true);

        paymentRepository.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setSuccess(true);
        response.setTransactionId(payment.getTransactionId());
        response.setMessage("Payment successful");

        return response;
    }
}
