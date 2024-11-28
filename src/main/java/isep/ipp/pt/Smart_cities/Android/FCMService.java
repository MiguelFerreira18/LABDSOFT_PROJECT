package isep.ipp.pt.Smart_cities.Android;

import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import isep.ipp.pt.Smart_cities.Android.Notification.NotificationRequest;
import isep.ipp.pt.Smart_cities.Responses.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@Service
public class FCMService {
    private Logger logger = LoggerFactory.getLogger(FCMService.class.getName());


    public Optional<Response> sendMessageToToken(NotificationRequest request) {
        Message message = getPreconfiguredMessageToToken(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        Optional<String> response = sendAndGetResponse(message);
        if (response.isPresent()) {
            logger.info("Sent message to token. Device token: " + request.getToken() + ", " + jsonOutput);
            return Optional.of(Response.created("Notification has been sent.", jsonOutput));
        } else {
            logger.error("Error sending message to token. " + jsonOutput);
            return Optional.of(Response.badRequest("Error sending message to token."));
        }
    }

    private Optional<String> sendAndGetResponse(Message message)  {
        try {
            return Optional.of(FirebaseMessaging.getInstance().sendAsync(message).get());
        }catch (InterruptedException | ExecutionException e){
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }


    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setTag(topic).build()).build();
    }
    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }
    private Message getPreconfiguredMessageToToken(NotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(NotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(notification);
    }


}
