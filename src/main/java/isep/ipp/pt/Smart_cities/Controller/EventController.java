package isep.ipp.pt.Smart_cities.Controller;

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
public ResponseEntity<Event> createEvent(@RequestBody Map<String, Object> requestData) {
    try {
        // Extract and validate fields from the request
        String title = (String) requestData.get("title");
        String location = (String) requestData.get("location");
        LocalDate startDate = LocalDate.parse((String) requestData.get("startDate"));
        LocalDate endDate = LocalDate.parse((String) requestData.get("endDate"));
        String description = (String) requestData.get("description");
        String category = (String) requestData.get("category");

        // Fetch the user from the authenticatedUserDetails
        User creator = null;
        
            creator =userService.findUserByEmail("admin@smartcity.com");
        
        

        // Create a new Event object
        Event event = new Event();
        event.setTitle(title);
        event.setLocation(location);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setDescription(description);
        event.setCreator(creator); // Set the creator
        event.addCategory(category); // Add category

        // Save and return the created event
        return ResponseEntity.ok(eventService.createEvent(event));
    } catch (Exception e) {
        // Handle errors gracefully
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
}
