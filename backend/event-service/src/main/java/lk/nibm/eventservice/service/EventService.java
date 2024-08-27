package lk.nibm.eventservice.service;

import lk.nibm.eventservice.model.Event;
import lk.nibm.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List < Event > getEvents(){
        return eventRepository.findAll();
    }
    public Event  getEventById(int id){

        Optional <Event> event =  eventRepository.findById(id);
        if(event.isPresent()){
            return event.get();
        }

        return null;
    }
    public List<Event> findEventByName(String name){

        return eventRepository.findEventByName(name);
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
}

