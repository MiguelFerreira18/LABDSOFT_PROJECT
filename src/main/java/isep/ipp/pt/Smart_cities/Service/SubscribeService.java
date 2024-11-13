package isep.ipp.pt.Smart_cities.Service;

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
            return Optional.of(Response.error("User already subscribed to event", null, eventId));
        }

        Optional<User> user= userService.findById(uuid);
        if(user.isEmpty()){
            return Optional.of(Response.error("User not found", null,uuid));
        }

        Optional<Event> event = eventRepo.findById(eventId);
        if(event.isEmpty()){
            return Optional.of(Response.error("Event not found", null, eventId));
        }

        Subscribe subscribeRequest = new Subscribe(user.get(), event.get());
        try {
            subscribeRequest.setCode((int) (Math.random() * 10000));
            subscribeRequest.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);
            return Optional.of(Response.success("Subscribe Request created",
                    subscribeRepo.save(subscribeRequest)));
        } catch (Exception e) {
            return Optional.of(Response.error("Error creating Subscribe Request", e));
        }
    }
    public Optional<Response> reSubscribeAnEvent(long id) {
        return subscribeRepo.findById(id).map(subscribe -> {
            try {
                subscribe.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);
                return Response.success("Event resubscribed", subscribeRepo.save(subscribe));
            } catch (Exception e) {
                return Response.error("Error resubscribing event", e, subscribe);
            }
        });
    }
    public Optional<Response> unsubscribe(long id) {
        return subscribeRepo.findById(id).map(subscribe -> {
            try {
                subscribe.setSubscriptionStatus(SubscriptionStatus.UNSUBSCRIBED);
                return Response.success("Event unsubscribed", subscribeRepo.save(subscribe));
            } catch (Exception e) {
                return Response.error("Error unsubscribing event", e, subscribe);
            }
        });
    }

    public Optional<Response> getSubscriptions(String uuid) {
        try{
            return Optional.of(Response.success("Subscriptions retrieved",
                    StreamSupport
                            .stream(subscribeRepo.findAllSubscribedEventsFromUser(uuid)
                                    .spliterator(), false)
                            .map(subscribe -> subscribeMapper.toSubscribeResponseDTO(subscribe))
                            .toList()));

        }catch (Exception e){
            return Optional.of(Response.error("Error getting Subscriptions", e));
        }
    }

    public Optional<Response> isSubscribed(String uuid, String eventId) {
        Optional<Subscribe> subscribe = subscribeRepo.findByEventIdAndUserId(eventId, uuid);
        if (subscribe.isEmpty()) {
            return Optional.of(Response.error("User not subscribed to event", null, eventId));
        }else if(subscribe.get().getSubscriptionStatus().equals(SubscriptionStatus.UNSUBSCRIBED)){
            return Optional.of(Response.success("User unsubscribed to event",  eventId));
        }
        return Optional.of(Response.success("User subscribed to event", eventId));
    }

    public void deleteAll(){
        subscribeRepo.deleteAll();
    }

}
