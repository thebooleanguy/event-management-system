package lk.nibm.eventservice.controller;

import lk.nibm.eventservice.model.Event;
import lk.nibm.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/findAll")
    public List<Event> findAllEvents(){
        return eventService.getEvents();
    }
    @GetMapping(path = "/find/{id}")
    public Event findEventByID(@PathVariable int id){

        return eventService.getEventById(id);
    }

    @GetMapping(path = "/findByTitle", params = "title")
    public List<Event> findEventByTitle(@RequestParam String title){
        return eventService.findEventByTitle(title);
    }

 /*   @GetMapping(path = "/events", params = "age")
     public List<Event> findEventByAge(@RequestParam int age) {

         return userService.findUserByAge(age);
     }*/

    @PostMapping(path = "/create")
    public Event creatEvent(@RequestBody Event  event){
        return eventService.createEvent(event);
    }


    @PutMapping(path = "/update/{id}")
    public Event updateEvent(@PathVariable  int id, @RequestBody Event event){

        return eventService.updateEvent(event);

    }

    @DeleteMapping (path = "/delete/{id}")
    public Event deleteUserByID(@PathVariable int id){

        return  eventService.deleteUserById(id);
    }


}
