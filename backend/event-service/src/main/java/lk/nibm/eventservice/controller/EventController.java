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

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> findAllEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> findEventById(@PathVariable int id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @GetMapping(params = "title")
    public List<Event> findEventByTitle(@RequestParam String title) {
        return eventService.findEventByTitle(title);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.status(201).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable int id, @RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(id, event);
        if (updatedEvent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable int id) {
        boolean deleted = eventService.deleteEventById(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = eventService.getCategories();
        return ResponseEntity.ok(categories);
    }

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

    @GetMapping("/available-tickets/{id}")
    public ResponseEntity<Integer> getAvailableTickets(@PathVariable int id) {
        int availableTickets = eventService.getAvailableTickets(id);
        return ResponseEntity.ok(availableTickets);
    }

    @PutMapping("/available-tickets/{id}")
    public ResponseEntity<Void> setAvailableTickets(
            @PathVariable int id,
            @RequestParam int availableTickets) {
        eventService.setAvailableTickets(id, availableTickets);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unit-price/{id}")
    public ResponseEntity<BigDecimal> getUnitPrice(@PathVariable int id) {
        BigDecimal unitPrice = eventService.getUnitPrice(id);
        if (unitPrice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unitPrice);
    }

    @PutMapping("/unit-price/{id}")
    public ResponseEntity<Void> setUnitPrice(
            @PathVariable int id,
            @RequestParam BigDecimal unitPrice) {
        eventService.setUnitPrice(id, unitPrice);
        return ResponseEntity.ok().build();
    }
}

