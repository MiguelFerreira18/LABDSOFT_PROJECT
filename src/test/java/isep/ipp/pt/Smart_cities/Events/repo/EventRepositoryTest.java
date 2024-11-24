package isep.ipp.pt.Smart_cities.Model.EventModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event event1;
    private Event event2;
    private Event event3;

  
    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        eventRepository.deleteAll();

        // Create sample events
        User creator = new User();
        creator.setId("creator-1");
        creator.setEmail("testuser@example.com");

        event1 = new Event(
            "Event One",
            "Location One",
            LocalDate.now().plusDays(5),
            LocalDate.now().plusDays(6),
            "Description One",
            creator,
            LocalDateTime.now().plusDays(10)
        );

        event2 = new Event(
            "Event Two",
            "Location Two",
            LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(12),
            "Description Two",
            creator
        );

        event3 = new Event(
            "Event Three",
            "Location Three",
            LocalDate.now().minusDays(5),
            LocalDate.now().minusDays(3),
            "Description Three",
            creator
        );

        // Save events
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
    }

    @Test
    void testFindByTitle() {
        Optional<Event> foundEvent = eventRepository.findByTitle("Event One");
        assertTrue(foundEvent.isPresent());
        assertEquals(event1.getTitle(), foundEvent.get().getTitle());
    }

    @Test
    void testFindUpcomingEvents() {
        List<Event> upcomingEvents = eventRepository.findUpcomingEvents();
        assertEquals(2, upcomingEvents.size());
        assertTrue(upcomingEvents.contains(event1));
        assertTrue(upcomingEvents.contains(event2));
    }

    @Test
    void testFindByCategory() {
        event1.setCategory("Art");
        event2.setCategory("Art");
        event3.setCategory("Sports");

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        List<Event> artEvents = eventRepository.findByCategory("Art");
        assertEquals(2, artEvents.size());
        assertTrue(artEvents.contains(event1));
        assertTrue(artEvents.contains(event2));

        List<Event> sportsEvents = eventRepository.findByCategory("Sports");
        assertEquals(1, sportsEvents.size());
        assertTrue(sportsEvents.contains(event3));
    }

    @Test
    void testFindPromotedEvents() {
        List<Event> promotedEvents = eventRepository.findPromotedEvents(LocalDateTime.now());
        assertEquals(1, promotedEvents.size());
        assertTrue(promotedEvents.contains(event1));
    }

    @Test
    void testFindNonPromotedEvents() {
        List<Event> nonPromotedEvents = eventRepository.findNonPromotedEvents(LocalDateTime.now());
        assertEquals(2, nonPromotedEvents.size());
        assertTrue(nonPromotedEvents.contains(event2));
        assertTrue(nonPromotedEvents.contains(event3));
    }

    @Test
    void testFindByCreatorEmail() {
        List<Event> events = eventRepository.findByCreatorEmail("testuser@example.com");
        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
    }

    @Test
    void testFindByCreatorId() {
        List<Event> events = eventRepository.findByCreatorId("creator-1");
        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
    }

    @Test
    void testFindAll() {
        List<Event> allEvents = eventRepository.findAll();
        assertEquals(3, allEvents.size());
    }
}
