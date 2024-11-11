package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Model.Event;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubscribeServiceTest {
    private static SubscribeService staticSubscribeService;
    private static SubscribeRepo staticSubscribeRepo;

    @Autowired
    private SubscribeService subscribeService;

    //In the future the event should be created in the test
    @BeforeAll
    static void init(@Autowired SubscribeService subscribeService,@Autowired SubscribeRepo subscribeRepo) {
        User user = new User();
        user.setId("478413fc-37fd-4ded-bcbf-1616be5cc647");
        user.setEmail("admin@smartcity.com");

        staticSubscribeService = subscribeService;

        Subscribe sub1 = new Subscribe(10L, user,new Event(1L) ,1, SubscriptionStatus.SUBSCRIBED);
        Subscribe sub2 = new Subscribe(11L, user,new Event(2L) ,1, SubscriptionStatus.SUBSCRIBED);
        Subscribe sub3 = new Subscribe(12L, user,new Event(3L) ,1, SubscriptionStatus.SUBSCRIBED);
        staticSubscribeRepo = subscribeRepo;
        staticSubscribeRepo.save(sub1);
        staticSubscribeRepo.save(sub2);
        staticSubscribeRepo.save(sub3);


    }

    //Create Subscription
    @ParameterizedTest
    @CsvSource({
    "'478413fc-37fd-4ded-bcbf-1616be5cc647',1",
    "'478413fc-37fd-4ded-bcbf-1616be5cc647',2",
    "'478413fc-37fd-4ded-bcbf-1616be5cc647',3",
    })
    void testSubscribeToAnEvent(String userId, Long eventId) {
        Optional<Response> response = subscribeService.subscribe(userId, eventId);
        assertTrue(response.isPresent());
        assertTrue(response.get().success());
    }
    @ParameterizedTest
    @CsvSource({
            "'87518d5a-ed00-4a52-8040-3ee883b98asd',1",
            "'87518d5a-ed00-8040-4a52-3ee883b98acb',2",
            "'87518d5a-8040-4a52-ed008040-3ee883b98acb',3",
    })
    void testSubscribeToAnEventWithUnkownUser(String userId, Long eventId) {
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
    void testSubscribeToAnEventWithUnkownEventId(String userId, Long eventId) {
        Optional<Response> response = subscribeService.subscribe(userId, eventId);
        assertTrue(response.isPresent());
        assertFalse(response.get().success());
    }
    //Unsub
    @Test
    void testUnsubscribeToEvent() {
       List<Response> subscribes = StreamSupport.stream(staticSubscribeRepo.findAllSubscribedEventsFromUser("478413fc-37fd-4ded-bcbf-1616be5cc647").spliterator(),false)
               .map(subscribe -> subscribeService.unsubscribe(subscribe.getId()).get()).toList();
       assertTrue(subscribes.stream().allMatch(Response::success));
       StreamSupport.stream(staticSubscribeRepo.findAllSubscribedEventsFromUser("478413fc-37fd-4ded-bcbf-1616be5cc647").spliterator(),false)
               .forEach(subscribe -> assertEquals(SubscriptionStatus.UNSUBSCRIBED,subscribe.getSubscriptionStatus()));
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
    void tearDown() {
        staticSubscribeService.deleteAll();
    }
}