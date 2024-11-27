package isep.ipp.pt.Smart_cities.Android.Notification;

import isep.ipp.pt.Smart_cities.Android.FCMService;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class EventNotifier {

    @Autowired
    private FCMService fcmService;
    @Autowired
    private SubscribeRepo subscribeRepo;

    private Logger logger = LoggerFactory.getLogger(EventNotifier.class);

    @Scheduled(fixedRate = 60000)
    public void notifyUsersOfEventSubscribedEvents() {
        logger.info("Checking for events starting soon");

       StreamSupport.stream(subscribeRepo.findAll().spliterator(), false)
                .filter(this::filterSubscriptions)
                .map(subscribe -> {
                    Optional<Response> response = fcmService.sendMessageToToken(createNotificationRequest(subscribe.getUser(), subscribe.getEvent()));
                    if (response.isPresent()) {
                        subscribe.setNotified(true);
                        subscribeRepo.save(subscribe);
                        logger.info("Message sent to token");

                    } else {
                        logger.error("Error sending message to token. ");
                    }
                    return response.orElse(Response.badRequest("Error sending message to token."));
                }).toList();
        logger.info("Finished checking for events starting soon");
    }
    public boolean filterSubscriptions(Subscribe subscribe){
        return !subscribe.isNotified() && isEventWithinAnHour(subscribe.getEvent()) && subscribe.getUser().getPushTokenMobile() != null;
    }

    public boolean isEventWithinAnHour(Event event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourFromNow = now.plus(1, ChronoUnit.HOURS);

        // Check if the event's start date is between now and one hour from now
        return event.getStartDate().isAfter(now) && event.getStartDate().isBefore(oneHourFromNow);
    }

    public NotificationRequest createNotificationRequest(User user, Event event) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .title("Event starting soon")
                .body("Event " + event.getTitle() + " is starting soon")
                .token(user.getPushTokenMobile())
                .build();
        return notificationRequest;
    }
}
