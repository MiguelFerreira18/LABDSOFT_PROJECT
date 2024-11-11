package isep.ipp.pt.Smart_cities.Model.Encrypted;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EncryptedSubscribe {
    private String uuid;
    private EncryptedEvent event;
    private String code;
    private String status;

    public EncryptedSubscribe() {
    }

    public EncryptedSubscribe(EncryptionUtil util, Subscribe subscribe){
        this.uuid = util.encrypt(String.valueOf(subscribe.getId())).get();
        this.event = subscribe.getEvent().returnEncryoedEvent(util);
        this.code = util.encrypt(String.valueOf(subscribe.getCode())).get();
        this.status = util.encrypt(subscribe.getSubscriptionStatus().name()).get();
    }

    public String getUuid() {
        return uuid;
    }

    public EncryptedEvent getEvent() {
        return event;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
