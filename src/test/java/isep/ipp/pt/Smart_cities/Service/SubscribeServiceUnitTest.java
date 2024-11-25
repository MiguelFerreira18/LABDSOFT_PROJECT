package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscribeServiceUnitTest {
    @Mock
    private SubscribeRepo subscribeRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private EventRepository eventRepo;

    @InjectMocks
    private SubscribeService subscribeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubscribe_UserNotFound() {
        String uuid = "invalid-uuid";
        String eventId = "event-123";

        when(userRepo.findById(uuid)).thenReturn(Optional.empty());

        Optional<Response> response = subscribeService.subscribe(uuid, eventId);

        assertEquals("User not found", response.get().message());
        assertEquals(404, response.get().statusCode());
    }

    @Test
    void testSubscribe_EventNotFound() {
        String uuid = "user-123";
        String eventId = "invalid-event";

        when(userRepo.findById(uuid)).thenReturn(Optional.of(new User()));
        when(eventRepo.findById(eventId)).thenReturn(Optional.empty());

        Optional<Response> response = subscribeService.subscribe(uuid, eventId);

        assertEquals("Event not found", response.get().message());
        assertEquals(404, response.get().statusCode());
    }

    @Test
    void testSubscribe_UserAlreadySubscribed() {
        String uuid = "user-123";
        String eventId = "event-123";

        User user = new User();
        Event event = new Event();
        Subscribe existingSubscribe = new Subscribe(user, event);
        existingSubscribe.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);

        when(userRepo.findById(uuid)).thenReturn(Optional.of(user));
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));
        when(subscribeRepo.findByEventIdAndUserId(eventId, uuid)).thenReturn(Optional.of(existingSubscribe));

        Optional<Response> response = subscribeService.subscribe(uuid, eventId);

        assertEquals("User already subscribed to event", response.get().message());
        assertEquals(403, response.get().statusCode());
    }

    @Test
    void testSubscribe_EventCapacityReached() {
        String uuid = "user-123";
        String eventId = "event-123";

        User user = new User();
        Event event = new Event();
        event.setId(eventId); // Set the event ID
        event.setLimit(1); // Set the event limit

        // Create a subscription with the user and event
        Subscribe subscribe = new Subscribe(user, event);
        subscribe.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);

        when(userRepo.findById(uuid)).thenReturn(Optional.of(user));
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));
        when(subscribeRepo.findAll()).thenReturn(List.of(subscribe)); // Return a subscription

        Optional<Response> response = subscribeService.subscribe(uuid, eventId);

        assertEquals("Event has reached its maximum capacity", response.get().message());
        assertEquals(403, response.get().statusCode());
    }

    @Test
    void testSubscribe_UserResubscribing() {
        String uuid = "user-123";
        String eventId = "event-123";

        User user = new User();
        Event event = new Event();
        Subscribe existingSubscribe = new Subscribe(user, event);
        existingSubscribe.setSubscriptionStatus(SubscriptionStatus.UNSUBSCRIBED);

        when(userRepo.findById(uuid)).thenReturn(Optional.of(user));
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));
        when(subscribeRepo.findByEventIdAndUserId(eventId, uuid)).thenReturn(Optional.of(existingSubscribe));
        when(subscribeRepo.save(any())).thenReturn(existingSubscribe);

        Optional<Response> response = subscribeService.subscribe(uuid, eventId);

        assertEquals("Event resubscribed", response.get().message());
        assertEquals(200, response.get().statusCode());
    }

    @Test
    void testSubscribe_Successful() {
        String uuid = "user-123";
        String eventId = "event-123";

        User user = new User();
        Event event = new Event();
        event.setLimit(2);

        when(userRepo.findById(uuid)).thenReturn(Optional.of(user));
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));
        when(subscribeRepo.findByEventIdAndUserId(eventId, uuid)).thenReturn(Optional.empty());
        when(subscribeRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Response> response = subscribeService.subscribe(uuid, eventId);

        assertEquals("Subscribe Request created", response.get().message());
        assertEquals(201, response.get().statusCode());
    }

    @Test
    void testSubscribe_ErrorDuringSave() {
        String uuid = "user-123";
        String eventId = "event-123";

        User user = new User();
        Event event = new Event();
        event.setLimit(2);

        when(userRepo.findById(uuid)).thenReturn(Optional.of(user));
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));
        when(subscribeRepo.findByEventIdAndUserId(eventId, uuid)).thenReturn(Optional.empty());
        when(subscribeRepo.save(any())).thenThrow(new RuntimeException("Database error"));

        Optional<Response> response = subscribeService.subscribe(uuid, eventId);

        assertEquals("Error creating Subscribe Request", response.get().message());
        assertEquals(500, response.get().statusCode());
    }

}
