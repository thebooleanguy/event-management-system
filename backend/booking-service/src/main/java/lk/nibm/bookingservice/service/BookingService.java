package lk.nibm.bookingservice.service;

//import lk.nibm.common.dto.EventDTO;
import lk.nibm.bookingservice.dto.BookingDTO;
import lk.nibm.bookingservice.model.Booking;
import lk.nibm.bookingservice.repository.BookingRepository;
//import lk.nibm.common.dto.PaymentRequest;
//import lk.nibm.common.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Define the base URL for other services
    @Value("${event.service.url}")
    private String eventServiceUrl;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    // -----------------------------------------------------------
    // Standard CRUD Methods
    // -----------------------------------------------------------

    /**
     * Retrieve all bookings.
     *
     * @return list of all bookings.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Retrieve a booking by ID.
     *
     * @param id the ID of the booking to retrieve.
     * @return the booking if found, Optional.empty() otherwise.
     */
    public Optional<Booking> getBookingById(int id) {
        return bookingRepository.findById(id);
    }

    /**
     * Update an existing booking.
     *
     * @param id the ID of the booking to update.
     * @param updatedBooking the booking object with updated data.
     * @return the updated booking if found and updated, Optional.empty() otherwise.
     */
    public Optional<Booking> updateBooking(int id, Booking updatedBooking) {
        if (!bookingRepository.existsById(id)) {
            return Optional.empty();
        }
        updatedBooking.setId(id);
        return Optional.of(bookingRepository.save(updatedBooking));
    }

    /**
     * Delete a booking by ID.
     *
     * @param id the ID of the booking to delete.
     */
    public void cancelBooking(int id) {
        bookingRepository.deleteById(id);
    }

    // -----------------------------------------------------------
    // Custom Methods
    // -----------------------------------------------------------

    /**
     * Retrieve bookings by user ID.
     *
     * @param userId the ID of the user whose bookings to retrieve.
     * @return list of bookings for the specified user.
     */
    public List<Booking> getBookingsByUser(int userId) {
        return bookingRepository.findByUserId(userId);
    }

    /**
     * Book a new ticket.
     *
     * @param bookingRequestDTO the DTO containing event, user details, and optional payment info.
     * @return the saved booking.
     */
    public Booking bookTicket(BookingDTO bookingRequestDTO) {
        // Validate input to ensure that both eventId and userId are provided
        if (bookingRequestDTO.getEventId() == 0 || bookingRequestDTO.getUserId() == 0) {
            throw new IllegalArgumentException("Event ID and User ID must be provided");
        }

        try {
            // 1. Call the event-service to get available tickets
            ResponseEntity<Integer> availableTicketsResponse = restTemplate.exchange(
                    String.format("%s/tickets/available/{eventId}", eventServiceUrl),
                    HttpMethod.GET,
                    null,
                    Integer.class,
                    bookingRequestDTO.getEventId()
            );

            // Extract available tickets from the response
            Integer availableTickets = availableTicketsResponse.getBody();
            if (availableTickets == null || availableTickets < bookingRequestDTO.getTotalTickets()) {
                throw new RuntimeException("Not enough tickets available");
            }

            // 2. Call the event-service to get the ticket price
            ResponseEntity<BigDecimal> priceResponse = restTemplate.getForEntity(
                    String.format("%s/tickets/price/%d", eventServiceUrl, bookingRequestDTO.getEventId()),
                    BigDecimal.class
            );

            // Extract price from the response
            BigDecimal unitPrice = priceResponse.getBody();
            if (unitPrice == null) {
                throw new RuntimeException("Ticket price not found");
            }

            // 3. Calculate the total price for the booking
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(bookingRequestDTO.getTotalTickets()));
            bookingRequestDTO.setTotalPrice(totalPrice);  // Set the calculated total price

            // 4. Map the DTO to the entity and save the booking
            Booking booking = new Booking();
            booking.setEventId(bookingRequestDTO.getEventId());
            booking.setUserId(bookingRequestDTO.getUserId());
            booking.setTotalTickets(bookingRequestDTO.getTotalTickets());
            booking.setTotalPrice(totalPrice.doubleValue());
            booking.setPaymentId(null);  // Payment can be added later

            Booking savedBooking = bookingRepository.save(booking);

            // 5. Call the event-service to reduce available tickets
            restTemplate.postForEntity(
                    String.format("%s/tickets/reduce?eventId=%d&ticketsToReduce=%d", eventServiceUrl, bookingRequestDTO.getEventId(), bookingRequestDTO.getTotalTickets()),
                    null,
                    Void.class
            );

            // 6. If payment information is available, forward the payment info to the payment service
            if (bookingRequestDTO.getPaymentMethod() != null) {
                // Create a map with the required payment data
                Map<String, Object> paymentData = new HashMap<>();
                paymentData.put("userId", bookingRequestDTO.getUserId());
                paymentData.put("bookingId", savedBooking.getId());
                paymentData.put("amount", totalPrice);
                paymentData.put("paymentMethod", bookingRequestDTO.getPaymentMethod());

                // Send payment data to the payment service
                ResponseEntity<Map> paymentResponse = restTemplate.postForEntity(
                        String.format("%s/process", paymentServiceUrl),
                        paymentData,
                        Map.class
                );

                Map<String, Object> paymentResult = paymentResponse.getBody();
                if (paymentResult == null || !(boolean) paymentResult.get("success")) {
                    throw new RuntimeException("Payment processing failed: " + (paymentResult != null ? paymentResult.get("message") : "Unknown error"));
                }

                // Update the booking with payment information (assuming paymentId is returned)
                Integer paymentId = (Integer) paymentResult.get("paymentId");
                if (paymentId != null) {
                    savedBooking.setPaymentId(paymentId);
                    bookingRepository.save(savedBooking);
                }
            }

            return savedBooking;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error communicating with event or payment service: " + e.getMessage(), e);
        }
    }








}
