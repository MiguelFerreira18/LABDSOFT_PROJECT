package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Mapper.SubscribeMapper.SubscribeMapperImpl;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class SubscribeService {

    @Autowired
    private SubscribeRepo subscribeRepo;
    @Autowired
    private UserRepo userService;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private SubscribeMapperImpl subscribeMapper;


    public Optional<Response> subscribe(String uuid, String eventId) {
        Optional<Subscribe> isAlreadySubscribed = subscribeRepo.findByEventIdAndUserId(eventId, uuid);
        if (isAlreadySubscribed.isPresent() && isAlreadySubscribed.get().getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIBED)) {
            return Optional.of(Response.forbidden("User already subscribed to event"));
        }else if (isAlreadySubscribed.isPresent() && isAlreadySubscribed.get().getSubscriptionStatus().equals(SubscriptionStatus.UNSUBSCRIBED)){
            return reSubscribeAnEvent(isAlreadySubscribed.get());
        }

        Optional<User> user= userService.findById(uuid);
        if(user.isEmpty()){
            return Optional.of(Response.notFound("User not found"));
        }

        Optional<Event> event = eventRepo.findById(eventId);
        if(event.isEmpty()){
            return Optional.of(Response.notFound("Event not found"));
        }

        Subscribe subscribeRequest = new Subscribe(user.get(), event.get());
        try {
            subscribeRequest.setCode((int) (Math.random() * 10000));
            subscribeRequest.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);
            return Optional.of(Response.created("Subscribe Request created",
                    subscribeRepo.save(subscribeRequest).toDTO()));
        } catch (Exception e) {
            return Optional.of(Response.internalError("Error creating Subscribe Request"));
        }
    }

    public Optional<Response> reSubscribeAnEvent(Subscribe subscription) {
        try {
            subscription.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);
            return Optional.of(Response.ok("Event resubscribed", subscribeRepo.save(subscription).toDTO()));
        } catch (Exception e) {
            return Optional.of(Response.internalError("Error resubscribing event"));
        }
    }
    public Optional<Response> unsubscribe(long id) {
        return subscribeRepo.findById(id).map(subscribe -> {
            try {
                subscribe.setSubscriptionStatus(SubscriptionStatus.UNSUBSCRIBED);
                return Response.ok("Event unsubscribed", subscribeRepo.save(subscribe).toDTO());
            } catch (Exception e) {
                return Response.internalError("Error unsubscribing event");
            }
        });
    }

    public Optional<Response> countAllSubscriptionsToAnEvent(String eventId){
        return getCountOfSubscriptions(eventId) == 0 ? Optional.of(Response.notFound("No subscriptions found")) :
                Optional.of(Response.ok("Count of subscriptions", getCountOfSubscriptions(eventId)));
    }

    private int getCountOfSubscriptions(String eventId) {
        return (int) StreamSupport.stream(subscribeRepo.findAll().spliterator(), false)
                .filter(subscribe -> subscribe.getEvent().getId().equals(eventId)
                        && subscribe.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIBED))
                .count();
    }

    public Optional<List<SubscribeResponseDTO>> getSubscriptionsByUserUUID(String uuid) {
        return Optional.of(StreamSupport.stream(subscribeRepo.findAll().spliterator(), false)
                .filter(subscribe -> subscribe.getUser().getId().equals(uuid)
                        && subscribe.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIBED)
                        && subscribe.getEvent().getEndDate().isAfter(LocalDate.now()))
                .map(subscribeMapper::toSubscribeResponseDTO)
                .toList());
    }

    public Optional<List<Event>> getAttendedEventsByUserUUID(String uuid) {
        return Optional.of(StreamSupport.stream(subscribeRepo.findAll().spliterator(), false)
                .filter(subscribe -> subscribe.getUser().getId().equals(uuid)
                        && subscribe.getSubscriptionStatus().equals(SubscriptionStatus.ATTENDED)
                        && subscribe.getEvent().getEndDate().isBefore(LocalDate.now()))
                .map(Subscribe::getEvent)
                .toList());
    }
    public Optional<List<Event>> getSubscribedEventsByUserUUID(String uuid) {
        return Optional.of(StreamSupport.stream(subscribeRepo.findAll().spliterator(), false)
                .filter(subscribe -> subscribe.getUser().getId().equals(uuid)
                        && subscribe.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIBED))
                .map(Subscribe::getEvent)
                .toList());
    }

    public Optional<Response> isSubscribed(String uuid, String eventId) {
        Optional<Subscribe> subscribe = subscribeRepo.findByEventIdAndUserId(eventId, uuid);
        if (subscribe.isEmpty() || subscribe.get().getSubscriptionStatus().equals(SubscriptionStatus.UNSUBSCRIBED)) {
            return Optional.of(Response.notFound("User not subscribed to event"));
        }
        return Optional.of(Response.created("User subscribed to event", subscribe.get().toDTO()));
    }

    public void deleteAll(){
        subscribeRepo.deleteAll();
    }

}
