package lk.nibm.bookingservice.repository;

import lk.nibm.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing Booking entities.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    /**
     * Find bookings by user ID.
     *
     * @param userId the ID of the user whose bookings are to be retrieved.
     * @return a list of bookings associated with the specified user ID.
     */
    @Query("SELECT b FROM Booking b WHERE b.userId = :userId")
    List<Booking> findByUserId(int userId);
}
