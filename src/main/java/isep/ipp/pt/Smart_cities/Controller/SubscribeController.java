package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeRequestDTO;
import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
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
    
    @GetMapping("/subscriptions/user/{uuid}")
    public ResponseEntity<List<SubscribeResponseDTO>> getSubscriptionsByUserUUID(@PathVariable @Valid String uuid) {
        return subscribeService.getSubscriptionsByUserUUID(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/isSubscribed")
    public ResponseEntity<Response> getSubscription(@RequestBody @Valid SubscribeRequestDTO subscribeRequestDTO) {
        return subscribeService.isSubscribed(subscribeRequestDTO.getUuid(), subscribeRequestDTO.getEventId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
