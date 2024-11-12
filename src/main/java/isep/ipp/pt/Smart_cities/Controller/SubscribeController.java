package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeRequestDTO;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Service.SubscribeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
public class SubscribeController {
    @Autowired
    private SubscribeService subscribeService;


    @PostMapping("/subscribe")
    public ResponseEntity<Response> subscribe(@RequestBody @Valid SubscribeRequestDTO subscribeRequestDTO) {
        return subscribeService.subscribe(subscribeRequestDTO.getUuid(), subscribeRequestDTO.getEventId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @PostMapping("/unsubscribe/{subscribeId}")
    public ResponseEntity<Response> unsubscribe(@PathVariable @Valid Long subscribeId) {

        return subscribeService.unsubscribe(subscribeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/subscriptions/{uuid}")
    public ResponseEntity<Response> getSubscriptions(@PathVariable @Valid String uuid) {
        return subscribeService.getSubscriptions(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/subscribe")
    public ResponseEntity<Response> getSubscription(@RequestBody @Valid SubscribeRequestDTO subscribeRequestDTO) {
        return subscribeService.isSubscribed(subscribeRequestDTO.getUuid(), subscribeRequestDTO.getEventId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @PutMapping("/reSubscribe/{subscribeId}")
    public ResponseEntity<Response> reSubscribe(@PathVariable @Valid Long subscribeId) {
        return subscribeService.reSubscribeAnEvent(subscribeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }
}
