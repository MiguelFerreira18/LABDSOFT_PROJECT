package isep.ipp.pt.Smart_cities.Events.model;



import org.junit.jupiter.api.Test;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.EventModel.EventSummary;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EventSummaryTest {

    @Test
    void testEventSummaryConstructorFromEvent() {
        // Arrange: Create an Event object with sample data
        Event event = new Event();
        event.setId("1");
        event.setTitle("Test Event");
        event.setStartDate(LocalDate.of(2024, 12, 1));
        event.setLocation("Test Location");
        event.setCategory("Art");

        // Act: Create an EventSummary from the Event object
        EventSummary summary = new EventSummary(event);

        // Assert: Verify the fields are correctly set
        assertEquals("1", summary.getId());
        assertEquals("Test Event", summary.getTitle());
        assertEquals(LocalDate.of(2024, 12, 1), summary.getDate());
        assertEquals("Test Location", summary.getLocation());
        assertEquals("Art", summary.getCategory());
        assertEquals(0, summary.getTotalAttendees());
    }

    @Test
    void testEventSummaryConstructorWithAllFields() {
        // Arrange: Provide sample data
        String id = "1";
        String title = "Sample Event";
        LocalDate date = LocalDate.of(2024, 12, 5);
        String location = "Sample Location";
        String category = "Sports";
        int totalAttendees = 50;

        // Act: Create an EventSummary using the full constructor
        EventSummary summary = new EventSummary(id, title, date, location, category, totalAttendees);

        // Assert: Verify all fields are correctly initialized
        assertEquals("1", summary.getId());
        assertEquals("Sample Event", summary.getTitle());
        assertEquals(LocalDate.of(2024, 12, 5), summary.getDate());
        assertEquals("Sample Location", summary.getLocation());
        assertEquals("Sports", summary.getCategory());
        assertEquals(50, summary.getTotalAttendees());
    }

    @Test
    void testDefaultConstructorAndSetters() {
        // Act: Create an EventSummary with the default constructor
        EventSummary summary = new EventSummary();

        // Set values using setters
        summary.setId("2");
        summary.setTitle("Default Constructor Event");
        summary.setDate(LocalDate.of(2024, 11, 15));
        summary.setLocation("Default Location");
        summary.setCategory("Recreational");
        summary.setTotalAttendees(20);

        // Assert: Verify the fields are correctly updated
        assertEquals("2", summary.getId());
        assertEquals("Default Constructor Event", summary.getTitle());
        assertEquals(LocalDate.of(2024, 11, 15), summary.getDate());
        assertEquals("Default Location", summary.getLocation());
        assertEquals("Recreational", summary.getCategory());
        assertEquals(20, summary.getTotalAttendees());
    }

    @Test
    void testToStringMethod() {
        // Arrange: Create an EventSummary object
        EventSummary summary = new EventSummary("3", "Event with ToString", LocalDate.of(2024, 10, 20), "Somewhere", "Educational", 15);

        // Act: Call toString
        String summaryString = summary.toString();

        // Assert: Ensure the string contains the expected field values
        assertTrue(summaryString.contains("3"));
        assertTrue(summaryString.contains("Event with ToString"));
        assertTrue(summaryString.contains("2024-10-20"));
        assertTrue(summaryString.contains("Somewhere"));
        assertTrue(summaryString.contains("Educational"));
        assertTrue(summaryString.contains("15"));
    }
}
