package isep.ipp.pt.Smart_cities.Service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class RewardsService {
    private static final int BASE_POINTS = 10;
    private static final int BONUS_POINTS = 5;
    private static final int ATTENDANCE_THRESHOLD = 3;

    @Autowired
    private RewardsRepo rewardsRepo;
    @Autowired
    private SubscribeRepo subscribeRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EventRepository eventRepo;

    public Optional<Response> givePointsByAttendingAnEvent(String userId, String eventId){
        return checkIfThereIsAlreadyRewards(userId,eventId) ?
                changSubscriptionStatusToAttended(userRepo.findById(userId).get(),eventRepo.findById(eventId).get())
                        .map(subscribe -> Response.ok("You have already received points for this event",subscribe.toDTO())) :
                processRewardsIfSubscriptionIsValid(userId, eventId);
    }

    private Optional<Response> processRewardsIfSubscriptionIsValid(String userId, String eventId) {
        return validateSubscription(userId, eventId)
                .map(subscribe -> processRewards(userId, eventId));
    }
    private boolean checkIfThereIsAlreadyRewards(String userId, String eventId){
        return StreamSupport.stream(rewardsRepo.findAllByUserId(userId).spliterator(),false)
                .anyMatch(rewards -> rewards.getEvent().getId().equals(eventId));
    }

    private Optional<Subscribe> validateSubscription(String userId,String eventId){
        return subscribeRepo.findByEventIdAndUserId(eventId,userId)
                .filter(subscribe -> subscribe.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIBED) &&
                        LocalDate.now().isAfter(subscribe.getEvent().getEndDate()));
    }

    private Response processRewards(String userId, String eventId) {
        int pointsToAward = calculatePoints(userId);

        return userRepo.findById(userId)
                .flatMap(user -> createRewards(user, eventId, pointsToAward))
                .orElse(Response.notFound("User or Event not found"));
    }

    private int calculatePoints(String userId) {
        long attendedEvents = countAttendedEventsInTheCurrentMonth(userId);
        return attendedEvents >= ATTENDANCE_THRESHOLD ?
                BASE_POINTS + BONUS_POINTS : BASE_POINTS;
    }

    private long countAttendedEventsInTheCurrentMonth(String userId) {
        return StreamSupport.stream(subscribeRepo.findAllByUserId(userId).spliterator(), false)
                .filter(subscribe -> subscribe.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIBED) &&
                        subscribe.getEvent().isInCurrentMonth())
                .count();
    }


    private Optional<Response> createRewards(User user, String eventId, int points) {
        return eventRepo.findById(eventId)
                .map(event -> {
                    Optional<Subscribe> subcription = changSubscriptionStatusToAttended(user,event);
                    return subcription.map(subscribe -> buildAndSaveRewards(user, event, points))
                            .map(rewards -> Response.ok("Points given", rewards))
                            .orElse(Response.internalError("An error occurred while processing the request."));
                });
    }
    private Optional<Subscribe> changSubscriptionStatusToAttended(User user,Event event){
        return subscribeRepo.findByEventIdAndUserId(event.getId(),user.getId())
                .map(subscribe -> {
                    subscribe.setSubscriptionStatus(SubscriptionStatus.ATTENDED);
                    return subscribeRepo.save(subscribe);
                });
    }

    private Rewards buildAndSaveRewards(User user, Event event, int points) {
        Rewards rewards = Rewards.builder()
                .points(points)
                .event(event)
                .user(user)
                .build();
        return rewardsRepo.save(rewards);
    }

    public Optional<Response> calculateTotalRewardsFromUserId(String userId){
        return StreamSupport.stream(rewardsRepo.findAllByUserId(userId).spliterator(), false)
                .map(Rewards::getPoints)
                .reduce(Integer::sum)
                .map(points -> Response.ok("Total points", points));
    }

}
