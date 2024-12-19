package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Dto.EventsDto.EventRequestDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.EventModel.Image;
import isep.ipp.pt.Smart_cities.Service.EventService;
import isep.ipp.pt.Smart_cities.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    // Endpoint para criar um evento
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventRequestDTO createEventRequestDto) {
        try {
            return ResponseEntity.ok(eventService.createEvent(createEventRequestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        return eventService.getEventById(id)
                .map(event -> {
                    // Corrigir a URL das imagens para o formato correto
                    event.getImages().forEach(image -> {
                        image.setUrl("http://localhost:9091/" + image.getUrl().replace("\\", "/"));
                    });
                    return ResponseEntity.ok(event);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para obter todos os eventos
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Endpoint para atualizar evento
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable String id, @RequestBody Event event) {
        event.setId(id);
        return ResponseEntity.ok(eventService.updateEvent(event));
    }

    // Endpoint para deletar evento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para deletar todos os eventos
    @DeleteMapping
    public ResponseEntity<Void> deleteAllEvents() {
        eventService.deleteAllEvents();
        return ResponseEntity.noContent().build();
    }

    // Endpoint para deletar evento com inscrições
    @DeleteMapping("/{id}/delsub")
    public ResponseEntity<Void> deleteEventWithSubscriptions(@PathVariable String id) {
        eventService.deleteEventWithSubscriptions(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para deletar todos os eventos com inscrições
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllEventsWithSubscriptions() {
        eventService.deleteAllEventsWithSubscriptions();
        return ResponseEntity.noContent().build();
    }

    // Endpoint para promover evento
    @PostMapping("/{id}/promote")
    public ResponseEntity<Event> promoteEvent(@PathVariable String id, @RequestParam String userId) {
        return ResponseEntity.ok(eventService.promoteEvent(id, userId));
    }

    // Endpoint para obter eventos promovidos
    @GetMapping("/promoted")
    public ResponseEntity<List<Event>> getPromotedEvents() {
        return ResponseEntity.ok(eventService.getPromotedEvents());
    }

    // Endpoint para obter eventos não promovidos
    @GetMapping("/non-promoted")
    public ResponseEntity<List<Event>> getNonPromotedEvents() {
        return ResponseEntity.ok(eventService.getNonPromotedEvents());
    }

    @PostMapping("/{eventId}/images")
    public ResponseEntity<Void> addImageToEvent(@PathVariable String eventId,
            @RequestParam("image") MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            Path uploadDir = Paths.get("event_images", eventId);

            try {
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String imageName = image.getOriginalFilename();
                Path imagePath = uploadDir.resolve(imageName);
                image.transferTo(imagePath);

                eventService.addImageToEvent(eventId, imagePath.toString());

                return ResponseEntity.ok().build();
            } catch (IOException e) {
                return ResponseEntity.status(500).body(null);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    // Endpoint para obter imagens de um evento
    @GetMapping("/{eventId}/images")
    public ResponseEntity<List<Image>> getImagesByEventId(@PathVariable String eventId) {
        List<Image> images = eventService.getImagesByEventId(eventId);
        return images.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(images);
    }
}
