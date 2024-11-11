package isep.ipp.pt.Smart_cities.Model;

import isep.ipp.pt.Smart_cities.Model.Encrypted.EncryptedEvent;
import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }

    public EncryptedEvent returnEncryoedEvent(EncryptionUtil util){
        return new EncryptedEvent(util, this);
    }
}
