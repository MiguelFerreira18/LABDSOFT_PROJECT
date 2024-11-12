package isep.ipp.pt.Smart_cities.Model;

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

}
