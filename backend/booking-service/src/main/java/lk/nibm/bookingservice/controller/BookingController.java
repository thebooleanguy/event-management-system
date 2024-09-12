package lk.nibm.bookingservice.controller;

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
    @GetMapping
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
     * @param bookingRequest the booking request containing event and user details.
     * @return the saved booking or 400 Bad Request if validation fails.
     */
    @PostMapping
    public ResponseEntity<Booking> bookTicket(@RequestBody Booking bookingRequest) {
        try {
            Booking newBooking = bookingService.bookTicket(bookingRequest);
            return ResponseEntity.ok(newBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
