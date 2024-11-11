package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeRequestDTO;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Service.SubscribeService;
import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
public class SubscribeController {
    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private EncryptionUtil encryptionUtil;

    @PostMapping("/subscribe")
    public ResponseEntity<Response> subscribe(@RequestBody @Valid SubscribeRequestDTO subscribeRequestDTO) {
        String uuidDecrypted = subscribeRequestDTO.decryptUuid(encryptionUtil);
        String eventIdDecrypted = subscribeRequestDTO.decryptEventId(encryptionUtil);
        Long eventId = isLong(eventIdDecrypted);
        if (uuidDecrypted == null || eventId == null) {
            return ResponseEntity.badRequest().build();
        }

        return subscribeService.subscribe(uuidDecrypted, eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @PostMapping("/unsubscribe/{subscribeIdEncrypted}")
    public ResponseEntity<Response> unsubscribe(@PathVariable @Valid String subscribeIdEncrypted) {
        String subscribeIdDecrypted = encryptionUtil.decrypt(subscribeIdEncrypted).orElse(null);
        if (subscribeIdDecrypted == null) {
            return ResponseEntity.badRequest().build();
        }

        Long subscribeId = isLong(subscribeIdDecrypted);
        if (subscribeId == null) {
            return ResponseEntity.badRequest().build();
        }

        return subscribeService.unsubscribe(subscribeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/subscriptions/{uuidEncrypted}")
    public ResponseEntity<Response> getSubscriptions(@PathVariable @Valid String uuidEncrypted) {
        String uuidDecrypted = encryptionUtil.decrypt(uuidEncrypted).orElse(null);
        if (uuidDecrypted == null) {
            return ResponseEntity.badRequest().build();
        }

        return subscribeService.getSubscriptions(uuidDecrypted)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/subscribe")
    public ResponseEntity<Response> getSubscription(@RequestBody @Valid SubscribeRequestDTO subscribeRequestDTO) {
        String uuidDecrypted = subscribeRequestDTO.decryptUuid(encryptionUtil);
        String eventIdDecrypted = subscribeRequestDTO.decryptEventId(encryptionUtil);
        Long eventId = isLong(eventIdDecrypted);

        if (uuidDecrypted == null || eventId == null) {
            return ResponseEntity.badRequest().build();
        }

        return subscribeService.isSubscribed(uuidDecrypted, eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @PutMapping("/reSubscribe/{subscribeIdEncrypted}")
    public ResponseEntity<Response> reSubscribe(@PathVariable @Valid String subscribeIdEncrypted) {
        String subscribeIdDecrypted = encryptionUtil.decrypt(subscribeIdEncrypted).orElse(null);
        if (subscribeIdDecrypted == null) {
            return ResponseEntity.badRequest().build();
        }

        Long subscribeId = isLong(subscribeIdDecrypted);
        if (subscribeId == null) {
            return ResponseEntity.badRequest().build();
        }

        return subscribeService.reSubscribeAnEvent(subscribeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }


    private Long isLong(String eventId) {
        try {
            return Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
