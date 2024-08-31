package lk.nibm.eventservice.controller;

import lk.nibm.eventservice.model.Event;
import lk.nibm.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

    /**
     * Get the list of available categories.
     *
     * @return ResponseEntity with list of categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = eventService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    public List<Event> getEvents(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category) {
        // Convert the category string to EventCategory enum
        Event.EventCategory eventCategory = null;
        if (category != null) {
            try {
                eventCategory = Event.EventCategory.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Handle invalid category value (optional)
                // For example, you might want to return a 400 Bad Request status
            }
        }
        return eventService.searchEvents(title, eventCategory);
    }

}
