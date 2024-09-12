package lk.nibm.eventservice.repository;

import lk.nibm.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Find events by title with case-insensitive matching.
     *
     * @param title the title to search for, with case insensitivity.
     * @return list of events that contain the specified title.
     */
    List<Event> findByTitleContainingIgnoreCase(String title);

    /**
     * Find events by title with case-insensitive matching and a specific category.
     *
     * @param title the title to search for, with case insensitivity.
     * @param category the category to filter by.
     * @return list of events that match the specified title and category.
     */
    List<Event> findByTitleContainingIgnoreCaseAndCategory(String title, Event.EventCategory category);
}
