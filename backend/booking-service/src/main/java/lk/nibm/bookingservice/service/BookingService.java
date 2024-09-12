package lk.nibm.bookingservice.service;

import lk.nibm.common.dto.EventDTO;
import lk.nibm.bookingservice.model.Booking;
import lk.nibm.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${event.service.name}")
    private String eventServiceName;

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

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
     * @param bookingRequest the booking request containing event and user details.
     * @return the saved booking.
     */
    public Booking bookTicket(Booking bookingRequest) {
        // Validate input to ensure that both eventId and userId are provided
        if (bookingRequest.getEventId() == 0 || bookingRequest.getUserId() == 0) {
            throw new IllegalArgumentException("Event ID and User ID must be provided");
        }

        // Define the base URL for the event-service
        String eventServiceUrl = "http://event-service/api/events/";

        try {
            // 1. Call the event-service to get available tickets
            ResponseEntity<Integer> availableTicketsResponse = restTemplate.exchange(
                    String.format("%s/tickets/available/{eventId}", eventServiceUrl),
                    HttpMethod.GET,
                    null,
                    Integer.class,
                    bookingRequest.getEventId()
            );

            // Extract available tickets from the response
            Integer availableTickets = availableTicketsResponse.getBody();
            if (availableTickets == null || availableTickets < bookingRequest.getTotalTickets()) {
                throw new RuntimeException("Not enough tickets available");
            }

            // 2. Call the event-service to get the ticket price
            ResponseEntity<BigDecimal> priceResponse = restTemplate.getForEntity(
                    String.format("%s/tickets/price/%d", eventServiceUrl, bookingRequest.getEventId()),
                    BigDecimal.class
            );

            // Extract price from the response
            BigDecimal unitPrice = priceResponse.getBody();
            if (unitPrice == null) {
                throw new RuntimeException("Ticket price not found");
            }

            // 3. Calculate the total price for the booking
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(bookingRequest.getTotalTickets()));
            bookingRequest.setTotalPrice(totalPrice.doubleValue());

            // 4. Save the booking information to the database
            return bookingRepository.save(bookingRequest);

        } catch (HttpClientErrorException e) {
            log.error("Client error when calling event service: {}", e.getResponseBodyAsString(), e);
            throw new RuntimeException("Error communicating with event service: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            log.error("Server error from event service: {}", e.getResponseBodyAsString(), e);
            throw new RuntimeException("Event service encountered an error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during booking: ", e);
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }




}
