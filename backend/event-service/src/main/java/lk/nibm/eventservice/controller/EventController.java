package lk.nibm.eventservice.controller;

import lk.nibm.eventservice.model.Event;
import lk.nibm.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/events/")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // -----------------------------------------------------------
    // Standard CRUD Endpoints (Create, Read, Update, Delete)
    // -----------------------------------------------------------

    /**
     * Retrieve all events.
     * Standard CRUD Read (findAll).
     *
     * @return list of all events.
     */
    @GetMapping("/all")
    public List<Event> findAllEvents() {
        return eventService.getEvents();
    }

    /**
     * Retrieve an event by ID.
     * Standard CRUD Read (findById).
     *
     * @param id the ID of the event to retrieve.
     * @return the event if found, 404 otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> findEventById(@PathVariable int id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    /**
     * Create a new event.
     * Standard CRUD Create.
     *
     * @param event the event to create.
     * @return the created event with 201 status.
     */
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.status(201).body(createdEvent);
    }

    /**
     * Update an event by ID.
     * Standard CRUD Update.
     *
     * @param id the ID of the event to update.
     * @param event the event object with updated data.
     * @return the updated event, or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable int id, @RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(id, event);
        if (updatedEvent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Delete an event by ID.
     * Standard CRUD Delete.
     *
     * @param id the ID of the event to delete.
     * @return 204 No Content if successful, 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable int id) {
        boolean deleted = eventService.deleteEventById(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // -----------------------------------------------------------
    // Custom Endpoints
    // -----------------------------------------------------------

    /**
     * Search for events by title and/or category.
     *
     * @param title the title to search for (optional).
     * @param category the category to filter by (optional).
     * @return list of matching events.
     */
    @GetMapping("/search")
    public List<Event> getEvents(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category) {
        Event.EventCategory eventCategory = null;
        if (category != null) {
            try {
                eventCategory = Event.EventCategory.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category value");
            }
        }
        return eventService.searchEvents(title, eventCategory);
    }

    /**
     * Retrieve all available event categories.
     *
     * @return list of category names.
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = eventService.getCategories();
        return ResponseEntity.ok(categories);
    }

    // -----------------------------------------------------------
    // Ticket-Related Endpoints
    // -----------------------------------------------------------

    /**
     * Retrieve the number of available tickets for an event by ID.
     *
     * @param id the ID of the event.
     * @return the number of available tickets.
     */
    @GetMapping("/tickets/available/{id}")
    public ResponseEntity<Integer> getAvailableTickets(@PathVariable int id) {
        int availableTickets = eventService.getAvailableTickets(id);
        return ResponseEntity.ok(availableTickets);
    }

    /**
     * Set the number of available tickets for an event by ID.
     *
     * @param id the ID of the event.
     * @param availableTickets the number of available tickets to set.
     * @return 200 OK if successful.
     */
    @PutMapping("/tickets/available/{id}")
    public ResponseEntity<Void> setAvailableTickets(
            @PathVariable int id,
            @RequestParam int availableTickets) {
        eventService.setAvailableTickets(id, availableTickets);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieve the ticket price (unit price) for an event by ID.
     *
     * @param id the ID of the event.
     * @return the ticket price, or 404 if not found.
     */
    @GetMapping("/tickets/price/{id}")
    public ResponseEntity<BigDecimal> getUnitPrice(@PathVariable int id) {
        BigDecimal unitPrice = eventService.getUnitPrice(id);
        if (unitPrice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unitPrice);
    }

    /**
     * Set the ticket price (unit price) for an event by ID.
     *
     * @param id the ID of the event.
     * @param unitPrice the new ticket price to set.
     * @return 200 OK if successful.
     */
    @PutMapping("/tickets/price/{id}")
    public ResponseEntity<Void> setUnitPrice(
            @PathVariable int id,
            @RequestParam BigDecimal unitPrice) {
        eventService.setUnitPrice(id, unitPrice);
        return ResponseEntity.ok().build();
    }
}
