package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private EventService eventService;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user1");

        testEvent = Event.builder()
                .id("event1")
                .endDate(LocalDate.now().minusDays(1))
                .build();


    }

    @Test
    void testPromoteEvent_Success() {
        String eventID = "event1";
        String userID = "user1";
        when(eventRepository.findById(eventID)).thenReturn(Optional.of(testEvent));
        when(userService.findById(userID)).thenReturn(testUser);
        when(eventRepository.save(testEvent)).thenReturn(testEvent);
        when(userService.saveUser(testUser)).thenReturn(Optional.of(testUser));

        Event promotedEvent = eventService.promoteEvent(eventID, userID);

        assertEquals(promotedEvent.getPromotedUntil(), LocalDateTime.now().plusDays(7));
        assertTrue(testUser.hasPromotedEvent());
    }

    @Test
    void testPromoteEvent_ShouldThrowException_WhenUserAlreadyPromoted() {
        String eventID = "event1";
        String userID = "user1";
        testUser.setHasPromotedEvent(true);

        when(eventRepository.findById(eventID)).thenReturn(Optional.of(testEvent));
        when(userService.findById(userID)).thenReturn(testUser);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.promoteEvent(eventID, userID));

        assertEquals("User already promoted a event", exception.getMessage());
        verify(eventRepository, never()).save(any());
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testPromoteEvent_ShouldThrowException_WhenEventNotFound() {
        String eventID = "event1";
        String userID = "user1";

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.promoteEvent(eventID, userID));

        assertEquals("Event not found", exception.getMessage());
        verify(eventRepository, never()).save(any());
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testGetPromotedEvents() {
        String eventID = testEvent.getId();
        String userID = "user1";
        LocalDateTime local = LocalDateTime.now();
        testEvent.setPromotedUntil(local);

        when(eventRepository.findById(eventID)).thenReturn(Optional.of(testEvent));
        when(userService.findById(userID)).thenReturn(testUser);
        when(eventRepository.save(testEvent)).thenReturn(testEvent);
        when(userService.saveUser(testUser)).thenReturn(Optional.of(testUser));
        when(eventRepository.findPromotedEvents(any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(testEvent));

        eventService.promoteEvent(testEvent.getId(), "user1");

        List<Event> promotedEvents = eventService.getPromotedEvents();

        assertTrue(promotedEvents.size() == 1);
        assertEquals(testEvent, promotedEvents.get(0));
    }

    @Test
    void testGetNonPromotedEvents() {
        String eventID = testEvent.getId();
        String userID = "user1";
        LocalDateTime local = LocalDateTime.now();
        testEvent.setPromotedUntil(local);

        when(eventRepository.findById(eventID)).thenReturn(Optional.of(testEvent));
        when(userService.findById(userID)).thenReturn(testUser);
        when(eventRepository.save(testEvent)).thenReturn(testEvent);
        when(userService.saveUser(testUser)).thenReturn(Optional.of(testUser));
        when(eventRepository.findNonPromotedEvents(any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(testEvent));

        eventService.promoteEvent(testEvent.getId(), "user1");

        List<Event> nonPromotedEvents = eventService.getNonPromotedEvents();

        assertTrue(nonPromotedEvents.size() == 1);
        assertEquals(testEvent, nonPromotedEvents.get(0));
    }


}