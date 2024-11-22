package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Dto.EventsDto.EventRequestDTO;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.EventModel.EventSummary;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SubscribeRepo subscribeRepository;

    @Autowired
    private UserService userService;



    public Event createEvent(EventRequestDTO eventRequestDTO) {
        User creator = userService.findById(eventRequestDTO.getCreatorID());
        if (creator == null) {
            throw new IllegalArgumentException("Creator not found");
        }
        Event event = Event.builder()
                .creator(creator)
                .title(eventRequestDTO.getTitle())
                .location(eventRequestDTO.getLocation())
                .startDate(eventRequestDTO.getStartDate())
                .endDate(eventRequestDTO.getEndDate())
                .description(eventRequestDTO.getDescription())
                .category(eventRequestDTO.getCategory())
                .latitude(eventRequestDTO.getLatitude())
                .longitude(eventRequestDTO.getLongitude())
                .build();

        return eventRepository.save(event);
    }


    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsByCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    public void deleteAllEvents() {
        eventRepository.deleteAll();
    }

     public void deleteEventWithSubscriptions(String eventId) {
        Iterable<Subscribe> subscriptions = subscribeRepository.findAllSubscribedEventsFromUser(eventId);
        for (Subscribe subscription : subscriptions) {
            subscribeRepository.delete(subscription);
        }
        eventRepository.deleteById(eventId);
    }

    public void deleteAllEventsWithSubscriptions() {
        subscribeRepository.deleteAll();
    }

    // New method to get summaries of events created by a specific promoter
    public List<EventSummary> getEventSummariesByPromoter(User promoter) {
        List<Event> events = eventRepository.findByCreatorEmail(promoter.getEmail());
        return events.stream().map(EventSummary::new).collect(Collectors.toList());
    }

    public Event getEventDetails(String eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }


    public void delteAllEvents() {
        eventRepository.deleteAll();
    }

    public Event promoteEvent(String eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User user = userService.findById(userId);

        if (user.hasPromotedEvent()) {
            throw new RuntimeException("User already promoted a event");
        }

        event.setPromotedUntil(LocalDateTime.now().plusDays(7));

        user.promoteEvent();

        eventRepository.save(event);
        userService.saveUser(user);

        return event;
    }
    public List<Event> getPromotedEvents() {
        return eventRepository.findPromotedEvents(LocalDateTime.now());
    }

    public List<Event> getNonPromotedEvents() {
        return eventRepository.findNonPromotedEvents(LocalDateTime.now());
    }




    public List<EventSummary> generateCurrentEventSummaries(String userId) {
            List<Event> currentEvents = eventRepository.findAll().stream().filter(event -> event.getCreator().getId().equals(userId)).toList(); // Fetch all events
        System.out.println(currentEvents);
            return currentEvents.stream()
                    .map(EventSummary::new)
                    .toList();
    }



}
