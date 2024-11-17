package isep.ipp.pt.Smart_cities.Model.EventModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
public class EventSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private LocalDate date;
    private String location;
    private int totalAttendees;

    // Constructor to initialize from an Event object
    public EventSummary(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.date = event.getStartDate();  // Assuming date represents the start date
        this.location = event.getLocation();
        this.totalAttendees = event.getAttendees().size(); // Directly retrieve the size of the attendees set
    }

    // Existing constructor (optional but useful for flexibility)
    public EventSummary(String id, String title, LocalDate date, String location, int totalAttendees) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.totalAttendees = totalAttendees;
    }
    
    // Default constructor (required by JPA)
    public EventSummary() {}
}
