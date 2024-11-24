package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Dto.EventsDto.EventRequestDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.EventModel.EventSummary;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private SubscribeRepo subscribeRepository;

    @Mock
    private UserService userService;

    @Mock
    private SubscribeService subscribeService;

    @InjectMocks
    private EventService eventService;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        testUser = new User();
        testUser.setId("user-1");
        testUser.setEmail("testuser@example.com");

        testEvent = new Event();
        testEvent.setId("event-1");
        testEvent.setTitle("Test Event");
        testEvent.setCreator(testUser);
        testEvent.setStartDate(LocalDate.now().plusDays(1));
        testEvent.setEndDate(LocalDate.now().plusDays(2));
        testEvent.setCategory("Art");
    }

    @Test
    void testCreateEvent() {
        EventRequestDTO requestDTO = EventRequestDTO.builder()
                .creatorID("user-1")
                .title("Test Event")
                .location("Test Location")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2))
                .description("Test Description")
                .category("Art")
                .latitude(12.34f)
                .longitude(56.78f)
                .build();

        when(userService.findById("user-1")).thenReturn(testUser);
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event createdEvent = eventService.createEvent(requestDTO);

        assertNotNull(createdEvent);
        assertEquals("Test Event", createdEvent.getTitle());
        assertEquals("Art", createdEvent.getCategory());
        assertEquals(testUser, createdEvent.getCreator());

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testGetEventById() {
        when(eventRepository.findById("event-1")).thenReturn(Optional.of(testEvent));

        Optional<Event> event = eventService.getEventById("event-1");

        assertTrue(event.isPresent());
        assertEquals("Test Event", event.get().getTitle());
        verify(eventRepository, times(1)).findById("event-1");
    }

    @Test
    void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(testEvent));

        List<Event> events = eventService.getAllEvents();

        assertEquals(1, events.size());
        assertEquals("Test Event", events.get(0).getTitle());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testPromoteEvent() {
        when(eventRepository.findById("event-1")).thenReturn(Optional.of(testEvent));
        when(userService.findById("user-1")).thenReturn(testUser);

        Event promotedEvent = eventService.promoteEvent("event-1", "user-1");

        assertNotNull(promotedEvent.getPromotedUntil());
        assertTrue(promotedEvent.getPromotedUntil().isAfter(LocalDateTime.now()));
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    void testDeleteEventWithSubscriptions() {
        Subscribe subscribe1 = new Subscribe();
        Subscribe subscribe2 = new Subscribe();
        when(subscribeRepository.findAllSubscribedEventsFromUser("event-1"))
                .thenReturn(Arrays.asList(subscribe1, subscribe2));

        eventService.deleteEventWithSubscriptions("event-1");

        verify(subscribeRepository, times(2)).delete(any(Subscribe.class));
        verify(eventRepository, times(1)).deleteById("event-1");
    }

    @Test
    void testGenerateCurrentEventSummaries() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(testEvent));
        when(subscribeService.getCountOfSubscriptions("event-1")).thenReturn(5);

        List<EventSummary> summaries = eventService.generateCurrentEventSummaries();

        assertEquals(1, summaries.size());
        assertEquals("Test Event", summaries.get(0).getTitle());
        assertEquals(5, summaries.get(0).getTotalAttendees());
    }

    @Test
    void testGetEventDetails() {
        when(eventRepository.findById("event-1")).thenReturn(Optional.of(testEvent));

        Event event = eventService.getEventDetails("event-1");

        assertNotNull(event);
        assertEquals("Test Event", event.getTitle());
    }
}
