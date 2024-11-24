package isep.ipp.pt.Smart_cities.Events.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private User testUser;
    private Validator validator;

    @BeforeEach
    void setup() {
        // Create a test user
        testUser = new User();
        testUser.setId("12345");
        testUser.setName("Test User");

        // Set up Jakarta Bean Validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testEventInitialization() {
        Event event = new Event("Title", "Location", LocalDate.now(), LocalDate.now().plusDays(1), "Description", testUser);

        assertNotNull(event);
        assertEquals("Title", event.getTitle());
        assertEquals("Location", event.getLocation());
        assertEquals("Description", event.getDescription());
        assertEquals(testUser, event.getCreator());
        assertEquals(0, event.getAttendees());
        assertNull(event.getPromotedUntil());
    }

    @Test
    void testIsInCurrentMonth() {
        Event event = new Event("Title", "Location", LocalDate.now(), LocalDate.now().plusDays(5), "Description", testUser);
        assertTrue(event.isInCurrentMonth());
    }

    @Test
    void testIsInCurrentMonth_False() {
        Event event = new Event("Title", "Location", LocalDate.now(), LocalDate.now().plusMonths(1), "Description", testUser);
        assertFalse(event.isInCurrentMonth());
    }

    @Test
    void testIsPromoted() {
        Event event = new Event("Title", "Location", LocalDate.now(), LocalDate.now().plusDays(1), "Description", testUser);
        event.setPromotedUntil(LocalDateTime.now().plusDays(1));
        assertTrue(event.isPromoted());
    }

    @Test
    void testIsPromoted_False() {
        Event event = new Event("Title", "Location", LocalDate.now(), LocalDate.now().plusDays(1), "Description", testUser);
        event.setPromotedUntil(LocalDateTime.now().minusDays(1));
        assertFalse(event.isPromoted());
    }

    @Test
    void testIncrementAndDecrementAttendees() {
        Event event = new Event("Title", "Location", LocalDate.now(), LocalDate.now().plusDays(1), "Description", testUser);
        assertEquals(0, event.getAttendees());

        event.incrementAttendees();
        assertEquals(1, event.getAttendees());

        event.decrementAttendees();
        assertEquals(0, event.getAttendees());

        event.decrementAttendees(); // Should not go below 0
        assertEquals(0, event.getAttendees());
    }

    @Test
    void testToEventRequestDTO() {
        Event event = new Event("Title", "Location", LocalDate.now(), LocalDate.now().plusDays(1), "Description", testUser);
        event.setCategory("Art");
        event.setLatitude(40.7128f);
        event.setLongitude(-74.0060f);

        var dto = event.toEventRequestDTO();

        assertEquals("Title", dto.getTitle());
        assertEquals("Location", dto.getLocation());
        assertEquals("Art", dto.getCategory());
        assertEquals(testUser.getId(), dto.getCreatorID());
        assertEquals(40.7128f, dto.getLatitude());
        assertEquals(-74.0060f, dto.getLongitude());
    }

    @Test
    void testValidationConstraints() {
        // Test invalid data
        Event invalidEvent = new Event();
        invalidEvent.setTitle(""); // Invalid: blank
        invalidEvent.setLocation(""); // Invalid: blank
        invalidEvent.setStartDate(LocalDate.now().minusDays(1)); // Invalid: past date
        invalidEvent.setEndDate(LocalDate.now().minusDays(1)); // Invalid: past date
        invalidEvent.setCategory("InvalidCategory"); // Invalid: not in enum
        invalidEvent.setDescription(""); // Invalid: blank

        var violations = validator.validate(invalidEvent);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Title is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Location is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Start date must not be in the past")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid category")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Description is required")));
    }
}
