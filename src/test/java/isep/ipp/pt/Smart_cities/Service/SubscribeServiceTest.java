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
    static void init(@Autowired UserRepo userRepo,@Autowired SubscribeRepo subscribeRepo, @Autowired EventRepository eventRepository) {

        User user = userRepo.findByEmail("AnyNormalUser@gmail.com").orElseThrow();

        Event event1 = eventRepository.save(new Event("AnyTitle1", "AnyLocation1", LocalDate.now(), LocalDate.now().plusDays(1), "AnyDescription1", user ));
        Event event2 = eventRepository.save(new Event("AnyTitle2", "AnyLocation2", LocalDate.now(), LocalDate.now().plusDays(2), "AnyDescription2", user));
        Event event3 = eventRepository.save(new Event("AnyTitle3", "AnyLocation3", LocalDate.now(), LocalDate.now().plusDays(3), "AnyDescription3", user));

        subscribeRepo.save(Subscribe.builder().id(10L).user(user).event(event1).subscriptionStatus(SubscriptionStatus.SUBSCRIBED).build());
        subscribeRepo.save(Subscribe.builder().id(11L).user(user).event(event2).subscriptionStatus(SubscriptionStatus.SUBSCRIBED).build());
        subscribeRepo.save(Subscribe.builder().id(12L).user(user).event(event3).subscriptionStatus(SubscriptionStatus.SUBSCRIBED).build());


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

    @AfterEach
    void tearDown(@Autowired SubscribeService subscribeService, @Autowired EventService eventService) {
        subscribeService.deleteAll();
        eventService.deleteAllEvents();
    }
}