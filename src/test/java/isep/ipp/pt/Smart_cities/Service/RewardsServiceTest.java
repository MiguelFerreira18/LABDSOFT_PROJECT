package isep.ipp.pt.Smart_cities.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import isep.ipp.pt.Smart_cities.Dto.RewardsDto.RewardResponseDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.Rewards;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.RewardsRepo;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;

@ExtendWith(MockitoExtension.class)
class RewardsServiceTest {
    @Mock
    private RewardsRepo rewardsRepo;
    @Mock
    private SubscribeRepo subscribeRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private EventRepository eventRepo;

    @InjectMocks
    private RewardsService rewardsService;

    private User testUser;
    private Event testEvent;
    private Subscribe testSubscription;
    private Rewards testRewards;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user1");

        testEvent = Event.builder()
                .id("event1")
                .endDate(LocalDate.now().minusDays(1))
                .build();

        testSubscription = Subscribe.builder()
                .id(1L)
                .user(testUser)
                .event(testEvent)
                .subscriptionStatus(SubscriptionStatus.SUBSCRIBED)
                .build();

        testRewards = Rewards.builder()
                .points(10)
                .user(testUser)
                .event(testEvent)
                .build();
    }

    @Test
    void givePointsByAttendingAnEvent_Success() {
        when(subscribeRepo.findByEventIdAndUserId("event1", "user1"))
                .thenReturn(Optional.of(testSubscription));
        when(userRepo.findById("user1")).thenReturn(Optional.of(testUser));
        when(eventRepo.findById("event1")).thenReturn(Optional.of(testEvent));
        when(subscribeRepo.save(any(Subscribe.class))).thenReturn(testSubscription);
        when(rewardsRepo.save(any(Rewards.class))).thenReturn(testRewards);

        Optional<Response> result = rewardsService.givePointsByAttendingAnEvent("user1", "event1");

        assertTrue(result.isPresent());
        assertEquals("Points given", result.get().message());
        verify(subscribeRepo).save(any(Subscribe.class));
        verify(rewardsRepo).save(any(Rewards.class));
    }

    @Test
    void givePointsByAttendingAnEvent_EventNotFinished() {
        testEvent.setEndDate(LocalDate.now().plusDays(1));
        when(subscribeRepo.findByEventIdAndUserId("event1", "user1"))
                .thenReturn(Optional.of(testSubscription));

        Optional<Response> result = rewardsService.givePointsByAttendingAnEvent("user1", "event1");

        assertFalse(result.isPresent());
        verify(rewardsRepo, never()).save(any(Rewards.class));
    }

    @Test
    void givePointsByAttendingAnEvent_UserNotSubscribed() {
        testSubscription.setSubscriptionStatus(SubscriptionStatus.UNSUBSCRIBED);
        when(subscribeRepo.findByEventIdAndUserId("event1", "user1"))
                .thenReturn(Optional.of(testSubscription));

        Optional<Response> result = rewardsService.givePointsByAttendingAnEvent("user1", "event1");

        assertFalse(result.isPresent());
        verify(rewardsRepo, never()).save(any(Rewards.class));
    }

    @Test
    void calculateTotalRewardsFromUserId_Success() {
        Rewards rewards1 = Rewards.builder().points(10).build();
        Rewards rewards2 = Rewards.builder().points(15).build();
        when(rewardsRepo.findAllByUserId("user1"))
                .thenReturn(Arrays.asList(rewards1, rewards2));

        Optional<Response> result = rewardsService.calculateTotalRewardsFromUserId("user1");

        assertTrue(result.isPresent());
        assertEquals(25, result.get().data());
    }

    @Test
    void givePointsBonusForFrequentAttendance() {
        Subscribe subscription1 = Subscribe.builder()
                .user(testUser)
                .event(Event.builder().endDate(LocalDate.now()).build())
                .subscriptionStatus(SubscriptionStatus.SUBSCRIBED)
                .build();
        Subscribe subscription2 = Subscribe.builder()
                .user(testUser)
                .event(Event.builder().endDate(LocalDate.now()).build())
                .subscriptionStatus(SubscriptionStatus.SUBSCRIBED)
                .build();
        Subscribe subscription3 = Subscribe.builder()
                .user(testUser)
                .event(Event.builder().endDate(LocalDate.now()).build())
                .subscriptionStatus(SubscriptionStatus.SUBSCRIBED)
                .build();

        when(subscribeRepo.findByEventIdAndUserId("event1", "user1"))
                .thenReturn(Optional.of(testSubscription));
        when(subscribeRepo.findAllByUserId("user1"))
                .thenReturn(Arrays.asList(subscription1, subscription2, subscription3));
        when(userRepo.findById("user1")).thenReturn(Optional.of(testUser));
        when(eventRepo.findById("event1")).thenReturn(Optional.of(testEvent));
        when(subscribeRepo.save(any(Subscribe.class))).thenReturn(testSubscription);
        when(rewardsRepo.save(any(Rewards.class))).thenReturn(testRewards);

        Optional<Response> result = rewardsService.givePointsByAttendingAnEvent("user1", "event1");

        assertTrue(result.isPresent());
        verify(rewardsRepo).save(argThat(rewards ->
                rewards.getPoints() == 15
        ));
    }

    @Test
    void whenFirstLogin_shouldCreateNewRewardWithBasePoints() {
        when(rewardsRepo.findLoginPointsByUser(testUser.getId())).thenReturn(Optional.empty());
        when(rewardsRepo.save(any(Rewards.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<RewardResponseDTO> result = rewardsService.givePointsByStreakLogin(testUser);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getPointsEarned());
        assertEquals(1, result.get().getDailyStreakDays());
        verify(rewardsRepo).save(any(Rewards.class));
    }

    @Test
    void whenAlreadyLoggedInToday_shouldNotGivePoints() {
        Rewards existingRewards = new Rewards();
        existingRewards.setPoints(10);
        existingRewards.setDailyStreakDays(1);
        existingRewards.setLastLoginAt(LocalDateTime.now());
        existingRewards.setUser(testUser);

        testUser.setLastLoginAt(LocalDateTime.now());

        when(rewardsRepo.findLoginPointsByUser(testUser.getId())).thenReturn(Optional.of(existingRewards));

        Optional<RewardResponseDTO> result = rewardsService.givePointsByStreakLogin(testUser);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getPointsEarned());
        assertEquals(1, result.get().getDailyStreakDays());
        verify(rewardsRepo, never()).save(any(Rewards.class));
    }

    @Test
    void whenMediumStreak_shouldGiveMediumPoints() {

        LocalDateTime previousLogin = LocalDateTime.now().minusDays(1);
        Rewards existingRewards = new Rewards();
        existingRewards.setPoints(15);
        existingRewards.setDailyStreakDays(3);
        existingRewards.setLastLoginAt(previousLogin);
        existingRewards.setUser(testUser);

        testUser.setLastLoginAt(LocalDateTime.now());

        when(rewardsRepo.findLoginPointsByUser(testUser.getId())).thenReturn(Optional.of(existingRewards));
        when(rewardsRepo.save(any(Rewards.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<RewardResponseDTO> result = rewardsService.givePointsByStreakLogin(testUser);

        assertTrue(result.isPresent());
        assertEquals(10, result.get().getPointsEarned());
        assertEquals(4, result.get().getDailyStreakDays());
        verify(rewardsRepo).save(any(Rewards.class));
    }

    @Test
    void whenWeeklyStreak_shouldGiveWeeklyPoints() {
        LocalDateTime previousLogin = LocalDateTime.now().minusDays(1);
        Rewards existingRewards = new Rewards();
        existingRewards.setPoints(50);
        existingRewards.setDailyStreakDays(6);
        existingRewards.setLastLoginAt(previousLogin);
        existingRewards.setUser(testUser);

        testUser.setLastLoginAt(LocalDateTime.now());

        when(rewardsRepo.findLoginPointsByUser(testUser.getId())).thenReturn(Optional.of(existingRewards));
        when(rewardsRepo.save(any(Rewards.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<RewardResponseDTO> result = rewardsService.givePointsByStreakLogin(testUser);

        assertTrue(result.isPresent());
        assertEquals(15, result.get().getPointsEarned());
        assertEquals(7, result.get().getDailyStreakDays());
        verify(rewardsRepo).save(any(Rewards.class));
    }

    @Test
    void whenStreakBroken_shouldResetToBasePoints() {
        LocalDateTime previousLogin = LocalDateTime.now().minusDays(2);
        Rewards existingRewards = new Rewards();
        existingRewards.setPoints(50);
        existingRewards.setDailyStreakDays(5);
        existingRewards.setLastLoginAt(previousLogin);
        existingRewards.setUser(testUser);

        testUser.setLastLoginAt(previousLogin);

        when(rewardsRepo.findLoginPointsByUser(testUser.getId())).thenReturn(Optional.of(existingRewards));
        when(rewardsRepo.save(any(Rewards.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<RewardResponseDTO> result = rewardsService.givePointsByStreakLogin(testUser);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getPointsEarned());
        assertEquals(1, result.get().getDailyStreakDays());
        verify(rewardsRepo).save(any(Rewards.class));
    }
}