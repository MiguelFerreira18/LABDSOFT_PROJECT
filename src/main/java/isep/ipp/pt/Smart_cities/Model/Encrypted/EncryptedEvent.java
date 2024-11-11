package isep.ipp.pt.Smart_cities.Model.Encrypted;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import isep.ipp.pt.Smart_cities.Model.Event;
import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EncryptedEvent {
    private String id;

    public EncryptedEvent() {
    }

    public EncryptedEvent(EncryptionUtil util, Event event){
        this.id = util.encrypt(String.valueOf(event.getId())).get();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
