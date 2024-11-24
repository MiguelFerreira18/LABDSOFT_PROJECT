package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Dto.EventsDto.EventRequestDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Service.EventService;
import isep.ipp.pt.Smart_cities.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

 @PostMapping
public ResponseEntity<Event> createEvent(@RequestBody EventRequestDTO createEventRequestDto) {
    try {
        Event event = eventService.createEvent(createEventRequestDto);
         // Initialize attendees count
        return ResponseEntity.ok(event);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(null);
    }
}


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/createdBy/{userId}")
    public ResponseEntity<List<Event>> getEventsByCreator(@PathVariable String userId) {
        try {
            List<Event> events = eventService.getEventsByCreator(userId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable String id, @RequestBody Event event) {
        event.setId(id);
        return ResponseEntity.ok(eventService.updateEvent(event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllEvents() {
        eventService.deleteAllEvents();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delsub")
    public ResponseEntity<Void> deleteEventWithSubscriptions(@PathVariable String id) {
        eventService.deleteEventWithSubscriptions(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllEventsWithSubscriptions() {
        eventService.deleteAllEventsWithSubscriptions();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/promote")
    public ResponseEntity<Event> promoteEvent(@PathVariable String id, @RequestParam String userId) {
        return ResponseEntity.ok(eventService.promoteEvent(id, userId));
    }

    @GetMapping("/promoted")
    public ResponseEntity<List<Event>> getPromotedEvents() {
        return ResponseEntity.ok(eventService.getPromotedEvents());
    }

    @GetMapping("/non-promoted")
    public ResponseEntity<List<Event>> getNonPromotedEvents() {
        return ResponseEntity.ok(eventService.getNonPromotedEvents());
    }
}
