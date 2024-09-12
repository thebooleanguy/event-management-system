package lk.nibm.bookingservice.controller;

import lk.nibm.bookingservice.dto.BookingDTO;
import lk.nibm.bookingservice.model.Booking;
import lk.nibm.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings/")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // -----------------------------------------------------------
    // Standard CRUD Endpoints
    // -----------------------------------------------------------

    /**
     * Retrieve all bookings.
     *
     * @return list of all bookings.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    /**
     * Retrieve a booking by ID.
     *
     * @param id the ID of the booking to retrieve.
     * @return the booking if found, 404 Not Found otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable int id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update an existing booking.
     *
     * @param id the ID of the booking to update.
     * @param updatedBooking the booking object with updated data.
     * @return the updated booking if found and updated, 404 Not Found otherwise.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable int id, @RequestBody Booking updatedBooking) {
        Optional<Booking> booking = bookingService.updateBooking(id, updatedBooking);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a booking by ID.
     *
     * @param id the ID of the booking to delete.
     * @return 204 No Content if deleted, 404 Not Found if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable int id) {
        if (bookingService.getBookingById(id).isPresent()) {
            bookingService.cancelBooking(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable int userId) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Book a new ticket.
     *
     * @param bookingRequestDTO the booking request containing event, user, and optional payment details.
     * @return the saved booking or a detailed error message if validation fails.
     */
    @PostMapping("/book")
    public ResponseEntity<?> bookTicket(@RequestBody BookingDTO bookingRequestDTO) {
        try {
            // 1. Validate the DTO if necessary (e.g., using annotations or custom checks)
            if (bookingRequestDTO.getEventId() == 0 || bookingRequestDTO.getUserId() == 0) {
                return ResponseEntity.badRequest().body("Event ID and User ID must be provided.");
            }

            // 2. Book the ticket using the service
            Booking newBooking = bookingService.bookTicket(bookingRequestDTO);

            // 3. Return the new booking as the response
            return ResponseEntity.ok(newBooking);

        } catch (IllegalArgumentException ex) {
            // 4. Handle specific validation or illegal argument errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } catch (RuntimeException e) {
            // 5. Catch general exceptions and return a bad request response with error details
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing booking: " + e.getMessage());
        }
    }
}
