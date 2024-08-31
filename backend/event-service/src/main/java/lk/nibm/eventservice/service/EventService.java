package lk.nibm.eventservice.service;

import lk.nibm.eventservice.model.Event;
import lk.nibm.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List <Event> getEvents(){
        return eventRepository.findAll();
    }
    public Event  getEventById(int id){

        Optional <Event> event =  eventRepository.findById(id);
         if(event.isPresent()){
            return event.get();
        }

        return null;
    }
    public List<Event> findEventByTitle(String title){

        return eventRepository.findEventByTitle(title);
    }


    public Event createEvent(Event event){

        return eventRepository.save(event);
    }

    public Event updateEvent(Event event){

        return eventRepository.save(event);
    }

    public Event  deleteUserById(int id) {

        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {

            eventRepository.deleteById(id);
            return event.get();

        }

        return null;
    }

    public List<Event> searchEvents(String title, Event.EventCategory category) {
        if (category == null) {
            // Handle the case where no category is provided
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

}

