package lk.nibm.eventservice.service;

import lk.nibm.eventservice.model.Event;
import lk.nibm.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // -----------------------------------------------------------
    // Standard CRUD Methods (Create, Read, Update, Delete)
    // -----------------------------------------------------------

    /**
     * Retrieve all events.
     * Standard CRUD Read (findAll).
     *
     * @return list of all events.
     */
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    /**
     * Retrieve a specific event by ID.
     * Standard CRUD Read (findById).
     *
     * @param id the ID of the event.
     * @return the event if found, null otherwise.
     */
    public Event getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    /**
     * Create a new event.
     * Standard CRUD Create.
     *
     * @param event the event to create.
     * @return the saved event.
     */
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Update an existing event by ID.
     * Standard CRUD Update.
     *
     * @param id the ID of the event to update.
     * @param event the updated event object.
     * @return the updated event if it exists, null otherwise.
     */
    public Event updateEvent(int id, Event event) {
        if (!eventRepository.existsById(id)) {
            return null; // Handle the case where the event does not exist
        }
        event.setId(id); // Ensure the event has the correct ID
        return eventRepository.save(event);
    }

    /**
     * Delete an event by ID.
     * Standard CRUD Delete.
     *
     * @param id the ID of the event to delete.
     * @return true if the event was deleted, false if it doesn't exist.
     */
    public boolean deleteEventById(int id) {
        if (!eventRepository.existsById(id)) {
            return false; // Handle the case where the event does not exist
        }
        eventRepository.deleteById(id);
        return true;
    }

    // -----------------------------------------------------------
    // Custom Methods
    // -----------------------------------------------------------

    /**
     * Search for events by title and optionally by category.
     * Custom search method.
     *
     * @param title the title to search for.
     * @param category optional event category to filter by.
     * @return list of matching events.
     */
    public List<Event> searchEvents(String title, Event.EventCategory category) {
        if (category == null) {
            return eventRepository.findByTitleContainingIgnoreCase(title);
        } else {
            return eventRepository.findByTitleContainingIgnoreCaseAndCategory(title, category);
        }
    }

    /**
     * Retrieve all available event categories.
     * Custom method.
     *
     * @return list of category names.
     */
    public List<String> getCategories() {
        return Arrays.stream(Event.EventCategory.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------
    // Ticket-Related Methods
    // -----------------------------------------------------------

    /**
     * Retrieve available tickets for an event by its ID.
     *
     * @param eventId the ID of the event.
     * @return number of available tickets, or 0 if event is not found.
     */
    public int getAvailableTickets(int eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        return (event != null) ? event.getAvailableTickets() : 0;
    }

    /**
     * Set the available tickets for an event.
     *
     * @param eventId the ID of the event.
     * @param availableTickets the number of available tickets to set.
     */
    public void setAvailableTickets(int eventId, int availableTickets) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setAvailableTickets(availableTickets);
            eventRepository.save(event);
        }
    }

    /**
     * Retrieve the unit price (ticket price) for an event by its ID.
     *
     * @param eventId the ID of the event.
     * @return the ticket price, or null if event is not found.
     */
    public BigDecimal getUnitPrice(int eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        return (event != null) ? event.getTicketPrice() : null;
    }

    /**
     * Set the unit price (ticket price) for an event.
     *
     * @param eventId the ID of the event.
     * @param unitPrice the new ticket price to set.
     */
    public void setUnitPrice(int eventId, BigDecimal unitPrice) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setTicketPrice(unitPrice);
            eventRepository.save(event);
        }
    }
}
