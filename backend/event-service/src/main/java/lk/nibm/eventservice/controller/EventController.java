package lk.nibm.eventservice.controller;

import lk.nibm.eventservice.model.Event;
import lk.nibm.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name="/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping(path="/events")
    public List<Event> findAllEvents(){
        return eventService.getEvents();
    }
    @GetMapping(path = "/events/{id}")
    public Event findEventByID(@PathVariable int id){

        return eventService.getEventById(id);
    }

    @GetMapping(path = "/events", params = "name")
    public List<Event> findEventByTitle(@RequestParam String title){
        return eventService.findEventByTitle(title);
    }


    @PostMapping(path = "/events")
    public Event creatEvent(@RequestBody Event event){
        return eventService.createEvent(event);
    }


    @PutMapping(path = "/events")
    public Event updateEvent(@PathVariable  int id, @RequestBody Event event){

        return eventService.updateEvent(event);

    }

    @DeleteMapping (path = "/events/{id}")
    public Event deleteUserByID(@PathVariable int id){

        return  eventService.deleteUserById(id);
    }


}
