package isep.ipp.pt.Smart_cities.Model.EventModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class EventSummary {


    private String id;

    private String title;
    private LocalDateTime date;
    private String location;
    private int totalAttendees;

    public EventSummary(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.date = event.getStartDate();
        this.location = event.getLocation();
    }

    public EventSummary(String id, String title, LocalDateTime date, String location, int totalAttendees) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
    }
    
    public EventSummary() {}
}
