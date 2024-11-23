package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeRequestDTO;
import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Service.SubscribeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscribeController {
    @Autowired
    private SubscribeService subscribeService;


    @PostMapping("/subscribe")
    public ResponseEntity<Response> subscribe(@RequestBody @Valid SubscribeRequestDTO subscribeRequestDTO) {
        return subscribeService.subscribe(subscribeRequestDTO.getUuid(), subscribeRequestDTO.getEventId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/unsubscribe/{subscribeId}")
    public ResponseEntity<Response> unsubscribe(@PathVariable @Valid Long subscribeId) {

        return subscribeService.unsubscribe(subscribeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @GetMapping("/user/{uuid}")
    public ResponseEntity<List<SubscribeResponseDTO>> getSubscriptionsByUserUUID(@PathVariable @Valid String uuid) {
        return subscribeService.getSubscriptionsByUserUUID(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/event/count/{eventId}")
    public ResponseEntity<Response> getSubscriptionCount(@PathVariable @Valid String eventId) {
        Response response = subscribeService.countAllSubscriptionsToAnEvent(eventId)
                .orElse(Response.notFound("Event not found"));
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/attended/event/{userId}")
    public ResponseEntity<List<Event>> getAttendedSubscriptionsByUserId(@PathVariable @Valid String userId) {
        return subscribeService.getAttendedEventsByUserUUID(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isSubscribed/{uuid}/{eventId}")
    public ResponseEntity<Response> getSubscription(@PathVariable @Valid String uuid, @PathVariable @Valid String eventId) {
        return subscribeService.isSubscribed(uuid, eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
