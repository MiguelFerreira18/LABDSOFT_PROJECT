package isep.ipp.pt.Smart_cities.Events.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Validator validator;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Initialize the validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Mock a User object
        mockUser = Mockito.mock(User.class);
        Mockito.when(mockUser.getId()).thenReturn("mockUserId");
        Mockito.when(mockUser.getName()).thenReturn("Mock User");
    }

    @Test
    void testEventCreationSuccess() {
        // Create a valid event
        Event event = new Event(
                "Sample Event",
                "Sample Location",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "This is a sample event description.",
                mockUser
        );

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid Event.");
    }

    @Test
    void testTitleValidation() {
        // Invalid title (blank)
        Event event = new Event(
                "",
                "Sample Location",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "Valid description",
                mockUser
        );

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Title is required")));
    }

    @Test
    void testLocationValidation() {
        // Location exceeds max length
        Event event = new Event(
                "Valid Title",
                "L".repeat(256), // Exceeds max length
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "Valid description",
                mockUser
        );

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Location cannot exceed 255 characters")));
    }

    @Test
    void testStartDateValidation() {
        // Start date in the past
        Event event = new Event(
                "Valid Title",
                "Valid Location",
                LocalDate.now().minusDays(1), // Invalid start date
                LocalDate.now().plusDays(1),
                "Valid description",
                mockUser
        );

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Start date must not be in the past")));
    }

    @Test
    void testEndDateValidation() {
        // End date in the past
        Event event = new Event(
                "Valid Title",
                "Valid Location",
                LocalDate.now(),
                LocalDate.now().minusDays(1), // Invalid end date
                "Valid description",
                mockUser
        );

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("End date must be in the future")));
    }

    @Test
    void testAddCategory() {
        Event event = new Event(
                "Valid Title",
                "Valid Location",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "Valid description",
                mockUser
        );

        event.addCategory("Workshop");
        assertTrue(event.getCategories().contains("Workshop"));
    }

    @Test
    void testRemoveCategory() {
        Event event = new Event(
                "Valid Title",
                "Valid Location",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "Valid description",
                mockUser
        );

        event.addCategory("Workshop");
        event.removeCategory("Workshop");
        assertFalse(event.getCategories().contains("Workshop"));
    }

    @Test
    void testAddAttendee() {
        Event event = new Event(
                "Valid Title",
                "Valid Location",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "Valid description",
                mockUser
        );

        User attendee = new User();
        attendee.setName("Attendee 1");

        event.addAttendee(attendee);
        assertTrue(event.getAttendees().contains(attendee));
    }

    @Test
    void testRemoveAttendee() {
        Event event = new Event(
                "Valid Title",
                "Valid Location",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "Valid description",
                mockUser
        );

        User attendee = new User();
        attendee.setName("Attendee 1");

        event.addAttendee(attendee);
        event.removeAttendee(attendee);
        assertFalse(event.getAttendees().contains(attendee));
    }

    @Test
    void testImagePath() {
        Event event = new Event(
                "Valid Title",
                "Valid Location",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "Valid description",
                mockUser
        );

        event.setImagePath("/images/event.jpg");
        assertEquals("/images/event.jpg", event.getImagePath());
    }
}