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

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    public List<Event> findEventByTitle(String title) {
        return eventRepository.findEventByTitle(title);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(int id, Event event) {
        if (!eventRepository.existsById(id)) {
            return null; // Handle the case where the event does not exist
        }
        event.setId(id); // Ensure the event has the correct ID
        return eventRepository.save(event);
    }

    public boolean deleteEventById(int id) {
        if (!eventRepository.existsById(id)) {
            return false; // Handle the case where the event does not exist
        }
        eventRepository.deleteById(id);
        return true;
    }

    public List<Event> searchEvents(String title, Event.EventCategory category) {
        if (category == null) {
            return eventRepository.findByTitleContainingIgnoreCase(title);
        } else {
            return eventRepository.findByTitleContainingIgnoreCaseAndCategory(title, category);
        }
    }

    public List<String> getCategories() {
        return Arrays.stream(Event.EventCategory.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    // Method to get available tickets for an event
    public int getAvailableTickets(int eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        return (event != null) ? event.getAvailableTickets() : 0;
    }

    // Method to set available tickets for an event
    public void setAvailableTickets(int eventId, int availableTickets) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setAvailableTickets(availableTickets);
            eventRepository.save(event);
        }
    }

    // Method to get the unit price for an event
    public BigDecimal getUnitPrice(int eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        return (event != null) ? event.getTicketPrice() : null;
    }

    // Method to set the unit price for an event
    public void setUnitPrice(int eventId, BigDecimal unitPrice) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setTicketPrice(unitPrice);
            eventRepository.save(event);
        }
    }
}
