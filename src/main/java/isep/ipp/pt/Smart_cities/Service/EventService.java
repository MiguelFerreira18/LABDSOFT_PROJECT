package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Dto.EventsDto.EventRequestDTO;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.EventModel.EventSummary;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;

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
    @Autowired
    private SubscribeService subscribeService;

    
    


    public Event createEvent(EventRequestDTO createEventRequestDto) {

        User creator = userService.findById(createEventRequestDto.getCreatorID());
        
        if (creator == null) {
            throw new IllegalArgumentException("Invalid creator ID");
        }

        Event event = new Event();
        event.setTitle(createEventRequestDto.getTitle());
        event.setLocation(createEventRequestDto.getLocation());
        event.setStartDate(createEventRequestDto.getStartDate());
        event.setEndDate(createEventRequestDto.getEndDate());
        event.setDescription(createEventRequestDto.getDescription());
        event.setCategory(createEventRequestDto.getCategory());
        event.setCreator(creator);
        event.setLatitude(createEventRequestDto.getLatitude());
        event.setLongitude(createEventRequestDto.getLongitude());

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

    /*public List<EventSummary> generateCurrentEventSummaries() {
        List<Event> currentEvents = eventRepository.findAll()
                .stream()                
                .toList();
    
        return currentEvents.stream()
                .map(event -> {
                    int subscriptionCount = subscribeService.getCountOfSubscriptions(event.getId());
                    EventSummary summary = new EventSummary(event);
                    summary.setTotalAttendees(subscriptionCount); // Update attendees dynamically
                    return summary;
                })
                .toList();
    }*/


    public List<EventSummary> generateCurrentEventSummaries() {
        // Fetch all events (add more filters if needed)
        List<Event> currentEvents = eventRepository.findAll();
        
        // For each event, get subscription count and generate an EventSummary
        return currentEvents.stream()
                .map(event -> {
                    int subscriptionCount = this.subscribeService.getCountOfSubscriptions(event.getId());
                    EventSummary summary = new EventSummary(event);
                    summary.setTotalAttendees(subscriptionCount); // Update attendees dynamically
                    return summary;
                })
                .collect(Collectors.toList());
    }
    
    public List<EventSummary> getEventSummariesWithDetails() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(EventSummary::new) // Map all events to summaries
                .collect(Collectors.toList());
    }

    public List<Event> getEventsByCreator(String userId) {
        return eventRepository.findByCreatorId(userId);
    }

      



}
