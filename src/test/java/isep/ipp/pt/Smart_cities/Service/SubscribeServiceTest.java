package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    //In the future the event should be created in the test
    @BeforeAll
    static void init(@Autowired SubscribeRepo subscribeRepo, @Autowired EventService eventService) {

        User user = new User();
        user.setId("b4f90eca-c844-4605-9a9c-20b098100fbe");
        user.setEmail("admin@smartcity.com");

        Event event1 = new Event();
        event1.setTitle("AnyTitle1");
        event1.setLocation("AnyLocation1");
        event1.setStartDate(java.time.LocalDate.now());
        event1.setEndDate(java.time.LocalDate.now().plusDays(1));
        event1.setDescription("AnyDescription1");
        event1.setCreator(user);
        event1.setCategories(Set.of("AnyCategory1"));

        Event event2 = new Event();
        event2.setTitle("AnyTitle2");
        event2.setLocation("AnyLocation2");
        event2.setStartDate(java.time.LocalDate.now());
        event2.setEndDate(java.time.LocalDate.now().plusDays(2));
        event2.setDescription("AnyDescription2");
        event2.setCreator(user);
        event2.setCategories(Set.of("AnyCategory2"));

        Event event3 = new Event();
        event3.setTitle("AnyTitle3");
        event3.setLocation("AnyLocation3");
        event3.setStartDate(java.time.LocalDate.now());
        event3.setEndDate(java.time.LocalDate.now().plusDays(3));
        event3.setDescription("AnyDescription3");
        event3.setCreator(user);
        event3.setCategories(Set.of("AnyCategory3"));

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
            "'478413fc-37fd-4ded-bcbf-1616be5cc647'",
            "'478413fc-37fd-4ded-bcbf-1616be5cc647'",
            "'478413fc-37fd-4ded-bcbf-1616be5cc647'",
    })
    void testSubscribeToAnEvent(String userId) {
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {
            Optional<Response> response = subscribeService.subscribe(userId, event.getId());
            assertTrue(response.isPresent());
            assertTrue(response.get().success());
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
        assertFalse(response.get().success());
    }

    @ParameterizedTest
    @CsvSource({
            "'478413fc-37fd-4ded-bcbf-1616be5cc647',10",
            "'478413fc-37fd-4ded-bcbf-1616be5cc647',11",
            "'478413fc-37fd-4ded-bcbf-1616be5cc647',12",
    })
    void testSubscribeToAnEventWithUnkownEventId(String userId, String eventId) {
        Optional<Response> response = subscribeService.subscribe(userId, eventId);
        assertTrue(response.isPresent());
        assertFalse(response.get().success());
    }

    //Unsub
    @Test
    void testUnsubscribeToEvent() {
        List<Response> subscribes = StreamSupport.stream(subscribeRepo.findAllSubscribedEventsFromUser("478413fc-37fd-4ded-bcbf-1616be5cc647").spliterator(), false)
                .map(subscribe -> subscribeService.unsubscribe(subscribe.getId()).get()).toList();
        assertTrue(subscribes.stream().allMatch(Response::success));
        StreamSupport.stream(subscribeRepo.findAllSubscribedEventsFromUser("478413fc-37fd-4ded-bcbf-1616be5cc647").spliterator(), false)
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
        Optional<Response> response = subscribeService.getSubscriptions("478413fc-37fd-4ded-bcbf-1616be5cc647");
        assertTrue(response.isPresent());
        assertTrue(response.get().success());

    }

    @Test
    void testGetAllSubscriptionsWithUnkownUser() {
        Optional<Response> response = subscribeService.getSubscriptions("87518d5a-ed00-4a52-8040-3ee883b98asd");
        assertTrue(response.isPresent());
        assertTrue(response.get().success());
    }

    @AfterEach
    void tearDown(@Autowired SubscribeService subscribeService, @Autowired EventService eventService) {
        subscribeService.deleteAll();
        eventService.delteAllEvents();
    }
}