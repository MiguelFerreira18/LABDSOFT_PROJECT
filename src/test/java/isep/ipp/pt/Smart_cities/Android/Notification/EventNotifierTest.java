package isep.ipp.pt.Smart_cities.Android.Notification;

import isep.ipp.pt.Smart_cities.Android.FCMService;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventNotifierTest {

    @Mock
    private FCMService fcmService;

    @Mock
    private SubscribeRepo subscribeRepo;

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventNotifier eventNotifier;

    private Subscribe subscribe;
    private User user;
    private Event event;


    @BeforeEach
    void setup() {
        user = mock(User.class);
        event = mock(Event.class);
        subscribe = new Subscribe(user, event);
    }
    @Test
    void testNotifyUsersOfEventSubscribedEvents() {
        User user = new User();
        user.setPushTokenMobile("mockPushToken");

        Event event = new Event();
        event.setTitle("Sample Event");
        event.setStartDate(LocalDateTime.now().plusMinutes(30));

        Subscribe subscribe = new Subscribe();
        subscribe.setUser(user);
        subscribe.setEvent(event);
        subscribe.setNotified(false);

        when(subscribeRepo.findAll()).thenReturn(List.of(subscribe));
        when(fcmService.sendMessageToToken(any())).thenReturn(Optional.of(Response.ok("Message sent", null)));

        eventNotifier.notifyUsersOfEventSubscribedEvents();

        verify(fcmService).sendMessageToToken(any());
        verify(subscribeRepo).save(subscribe);
        assertTrue(subscribe.isNotified());
    }

    @Test
    void testNotifyUsersOfEventSubscribedEvents_NoPushToken() {
        User user = new User();
        user.setPushTokenMobile(null);

        Event event = new Event();
        event.setTitle("Sample Event");
        event.setStartDate(LocalDateTime.now().plusMinutes(30));

        Subscribe subscribe = new Subscribe();
        subscribe.setUser(user);
        subscribe.setEvent(event);
        subscribe.setNotified(false);

        when(subscribeRepo.findAll()).thenReturn(List.of(subscribe));

        eventNotifier.notifyUsersOfEventSubscribedEvents();

        verify(fcmService, never()).sendMessageToToken(any());
        verify(subscribeRepo, never()).save(subscribe);
    }

    @Test
    void testFilterSubscriptions_ShouldReturnTrue_WhenAllConditionsAreMet() {
        subscribe.setNotified(false);
        when(user.getPushTokenMobile()).thenReturn("mockPushToken");
        when(event.getStartDate()).thenReturn(LocalDateTime.now().plusMinutes(30));
        boolean result = eventNotifier.filterSubscriptions(subscribe);
        assertTrue(result);
    }

    @Test
    void testFilterSubscriptions_ShouldReturnFalse_WhenAlreadyNotified() {
        subscribe.setNotified(true);
        boolean result = eventNotifier.filterSubscriptions(subscribe);
        assertFalse(result);
    }

    @Test
    void testFilterSubscriptions_ShouldReturnFalse_WhenEventIsNotWithinAnHour() {
        subscribe.setNotified(false);
        when(event.getStartDate()).thenReturn(LocalDateTime.now().plusHours(2));
        boolean result = eventNotifier.filterSubscriptions(subscribe);
        assertFalse(result);
    }

    @Test
    void testFilterSubscriptions_ShouldReturnFalse_WhenUserHasNoPushToken() {
        subscribe.setNotified(false);
        when(user.getPushTokenMobile()).thenReturn(null);
        when(event.getStartDate()).thenReturn(LocalDateTime.now().plusMinutes(30));
        boolean result = eventNotifier.filterSubscriptions(subscribe);
        assertFalse(result);
    }

    @Test
    void testIsEventWithinAnHour_ShouldReturnTrue_WhenEventIsInNextHour() {
        when(event.getStartDate()).thenReturn(LocalDateTime.now().plusMinutes(30));
        boolean result = eventNotifier.isEventWithinAnHour(event);
        assertTrue(result);
    }

    @Test
    void testIsEventWithinAnHour_ShouldReturnFalse_WhenEventIsInThePast() {
        when(event.getStartDate()).thenReturn(LocalDateTime.now().minusMinutes(10));
        boolean result = eventNotifier.isEventWithinAnHour(event);
        assertFalse(result);
    }

    @Test
    void testIsEventWithinAnHour_ShouldReturnFalse_WhenEventIsBeyondNextHour() {
        when(event.getStartDate()).thenReturn(LocalDateTime.now().plusHours(2));
        boolean result = eventNotifier.isEventWithinAnHour(event);
        assertFalse(result);
    }

    @Test
    void testCreateNotificationRequest_ShouldCreateValidNotificationRequest() {
        when(user.getPushTokenMobile()).thenReturn("mockPushToken");
        when(event.getTitle()).thenReturn("Mock Event");
        NotificationRequest notificationRequest = eventNotifier.createNotificationRequest(user, event);
        assertNotNull(notificationRequest);
        assertEquals("Event starting soon", notificationRequest.getTitle());
        assertEquals("Event Mock Event is starting soon", notificationRequest.getBody());
        assertEquals("mockPushToken", notificationRequest.getToken());
    }



}