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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubscribeServiceTest {

    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    private SubscribeRepo subscribeRepo;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepo userRepo;

    //In the future the event should be created in the test
    @BeforeAll
    static void init(@Autowired UserRepo userRepo,@Autowired SubscribeRepo subscribeRepo, @Autowired EventService eventService) {

        User user = userRepo.findByEmail("AnyNormalUser@gmail.com").get();

        Event event1 = new Event();
        event1.setTitle("AnyTitle1");
        event1.setLocation("AnyLocation1");
        event1.setStartDate(java.time.LocalDate.now());
        event1.setEndDate(java.time.LocalDate.now().plusDays(1));
        event1.setDescription("AnyDescription1");
        event1.setCreator(user);
        event1.setCategory("Art");

        Event event2 = new Event();
        event2.setTitle("AnyTitle2");
        event2.setLocation("AnyLocation2");
        event2.setStartDate(java.time.LocalDate.now());
        event2.setEndDate(java.time.LocalDate.now().plusDays(2));
        event2.setDescription("AnyDescription2");
        event2.setCreator(user);
        event2.setCategory("Educational");

        Event event3 = new Event();
        event3.setTitle("AnyTitle3");
        event3.setLocation("AnyLocation3");
        event3.setStartDate(java.time.LocalDate.now());
        event3.setEndDate(java.time.LocalDate.now().plusDays(3));
        event3.setDescription("AnyDescription3");
        event3.setCreator(user);
        event3.setCategory("Recreational");

        List<Event> newEvents = List.of(event1, event2, event3);
        for (Event event : newEvents) {
            eventService.createEvent(event);
        }

        List<Event> events = eventService.getAllEvents();
        for (Event event : events) {
            System.out.println(event);
        }

        Subscribe sub1 = Subscribe.builder()
                .id(10L)
                .user(user)
                .event(event1)
                .subscriptionStatus(SubscriptionStatus.SUBSCRIBED)
                .build();
        Subscribe sub2 = Subscribe.builder()
                .id(11L)
                .user(user)
                .event(event2)
                .subscriptionStatus(SubscriptionStatus.SUBSCRIBED)
                .build();
        Subscribe sub3 = Subscribe.builder()
                .id(12L)
                .user(user)
                .event(event3)
                .subscriptionStatus(SubscriptionStatus.SUBSCRIBED)
                .build();
        subscribeRepo.save(sub1);
        subscribeRepo.save(sub2);
        subscribeRepo.save(sub3);


    }

    //Create Subscription
    @ParameterizedTest
    @CsvSource({
            "AnyNormalUser@gmail.com",
            "admin@smartcity.com",
            "dev@smartcity.com",
    })
    void testSubscribeToAnEvent(String userEmail) {
        User u = userRepo.findByEmail(userEmail).get();
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {
            Optional<Response> response = subscribeService.subscribe(u.getId(), event.getId());
            assertTrue(response.isPresent());
            assertEquals(201, response.get().statusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "'87518d5a-ed00-4a52-8040-3ee883b98asd',1",
            "'87518d5a-ed00-8040-4a52-3ee883b98acb',2",
            "'87518d5a-8040-4a52-ed008040-3ee883b98acb',3",
    })
    void testSubscribeToAnEventWithUnkownUser(String userId, String eventId) {
        Optional<Response> response = subscribeService.subscribe(userId, eventId);
        assertTrue(response.isPresent());
        assertEquals(404, response.get().statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "'AnyNormalUser@gmail.com',10",
            "'admin@smartcity.com',11",
            "'dev@smartcity.com',12",
    })
    void testSubscribeToAnEventWithUnkownEventId(String email, String eventId) {
        User u = userRepo.findByEmail(email).get();
        Optional<Response> response = subscribeService.subscribe(u.getId(), eventId);
        assertTrue(response.isPresent());
        assertEquals(404, response.get().statusCode());
    }

    //Unsub
    @Test
    void testUnsubscribeToEvent() {
        List<Response> subscribes = StreamSupport.stream(subscribeRepo.findAllSubscribedEventsFromUser("AnyNormalUser@gmail.com").spliterator(), false)
                .map(subscribe -> subscribeService.unsubscribe(subscribe.getId()).get()).toList();
        assertTrue(subscribes.stream().allMatch(response -> response.statusCode() == 200));
        StreamSupport.stream(subscribeRepo.findAllSubscribedEventsFromUser("AnyNormalUser@gmail.com").spliterator(), false)
                .forEach(subscribe -> assertEquals(SubscriptionStatus.UNSUBSCRIBED, subscribe.getSubscriptionStatus()));
    }

    @ParameterizedTest
    @CsvSource({
            "999",
            "888",
            "777",
    })
    void testUnsubscribeToEventWithUnkownEventId(Long subscriptionId) {
        Optional<Response> response = subscribeService.unsubscribe(subscriptionId);
        assertFalse(response.isPresent());
    }

    //GetAll
    @Test
    void testGetAllSubscriptions() {
        User u = userRepo.findByEmail("AnyNormalUser@gmail.com").get();
        Optional<List<SubscribeResponseDTO>> response = subscribeService.getSubscriptionsByUserUUID(u.getId());
        assertTrue(response.isPresent());
        assertNotNull(response.get());

    }

    @Test
    void testGetAllSubscriptionsWithUnkownUser() {
        Optional<List<SubscribeResponseDTO>> response = subscribeService.getSubscriptionsByUserUUID("87518d5a-ed00-4a52-8040-3ee883b98asd");
        assertFalse(!response.isPresent());
    }

    //GetAttendedEvents
    @Test
    void testGetAttendedEvents() {

        User u = userRepo.findByEmail("AnyNormalUser@gmail.com").get();
        Event event = new Event("AlreadyAttended", "AnyLocation", LocalDate.now(), LocalDate.now().plusDays(1), "AnyDescription", u, null);
        eventRepository.save(event);
        Event pastEvent = eventRepository.findByTitle("AlreadyAttended").get();
        pastEvent.setStartDate(LocalDate.now().minusDays(5));
        pastEvent.setEndDate(LocalDate.now().minusDays(4));

        Subscribe sub = Subscribe.builder()
                .user(u)
                .event(pastEvent)
                .subscriptionStatus(SubscriptionStatus.ATTENDED)
                .build();
        subscribeRepo.save(sub);

        Optional<List<Event>> response = subscribeService.getAttendedEventsByUserUUID(u.getId());
        assertTrue(response.isPresent());
        assertNotNull(response.get());
    }


    @AfterEach
    void tearDown(@Autowired SubscribeService subscribeService, @Autowired EventService eventService) {
        subscribeService.deleteAll();
        eventService.deleteAllEvents();
    }
}