package isep.ipp.pt.Smart_cities.Model.EventModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class EventSummary {

    private String id;
    private String title;
    private LocalDate date;
    private String location;
    private String category;
    private int totalAttendees;

    // Constructor to initialize from an Event object
    public EventSummary(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.date = event.getStartDate();  // Assuming date represents the start date
        this.location = event.getLocation();
        this.category = event.getCategory();  // Assuming Event has a getCategory() method
        this.totalAttendees =  event.getAttendees(); // Assuming Event has a list of attendees
    }

    // Constructor with all fields for custom initialization
    public EventSummary(String id, String title, LocalDate date, String location, String category, int totalAttendees) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.category = category;
        this.totalAttendees = totalAttendees;
    }

    // Default constructor (required by JPA)
    public EventSummary() {}
}
