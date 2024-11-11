package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Mapper.SubscribeMapper.SubscribeMapperImpl;
import isep.ipp.pt.Smart_cities.Model.Event;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Respository.EventRepo;
import isep.ipp.pt.Smart_cities.Respository.SubscribeRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SubscribeService {

    @Autowired
    private SubscribeRepo subscribeRepo;
    @Autowired
    private UserRepo userService;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private SubscribeMapperImpl subscribeMapper;
    @Autowired
    private EncryptionUtil encryptionUtil;


    public Optional<Response> subscribe(String uuid, long eventId) {


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
                    subscribeRepo.save(subscribeRequest).returnEncryptedSubscribe(encryptionUtil)));
        } catch (Exception e) {
            return Optional.of(Response.error("Error creating Subscribe Request", e));
        }
    }
    public Optional<Response> unsubscribe(long id) {
        return subscribeRepo.findById(id).map(subscribe -> {
            try {
                subscribe.setSubscriptionStatus(SubscriptionStatus.UNSUBSCRIBED);
                return Response.success("Event unsubscribed", subscribeRepo.save(subscribe).returnEncryptedSubscribe(encryptionUtil));
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
                            .map(subscribe -> subscribeMapper.toSubscribeResponseDTO(subscribe, encryptionUtil))
                            .toList()));

        }catch (Exception e){
            return Optional.of(Response.error("Error getting Subscriptions", e));
        }
    }

}
