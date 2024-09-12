package lk.nibm.bookingservice.service;

import lk.nibm.common.dto.EventDTO;
import lk.nibm.bookingservice.model.Booking;
import lk.nibm.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
            throw new RuntimeException("Event ID and User ID must be provided");
        }

        // Fetch the URL of the event-service using the DiscoveryClient
        // DiscoveryClient helps to locate the service instance registered with Eureka
        String eventServiceUrl = discoveryClient.getInstances(eventServiceName)
                .stream()
                .findFirst()
                .map(instance -> instance.getUri().toString())
                .orElseThrow(() -> new RuntimeException("Event service not found"));

        // Call the event-service to get event details using RestTemplate
        ResponseEntity<EventDTO> response = restTemplate.getForEntity(
                String.format("%s/api/events/%d", eventServiceUrl, bookingRequest.getEventId()),
                EventDTO.class
        );

        // Extract event data from the response
        EventDTO event = response.getBody();

        // Check if the event exists
        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        // Check if there are enough available tickets for the booking request
        if (event.getAvailableTickets() < bookingRequest.getTotalTickets()) {
            throw new RuntimeException("Not enough tickets available");
        }

        // Calculate the total price for the booking
        BigDecimal unitPrice = event.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(bookingRequest.getTotalTickets()));
        bookingRequest.setTotalPrice(totalPrice.doubleValue());

        // Save the booking information to the database
        return bookingRepository.save(bookingRequest);
    }
}
