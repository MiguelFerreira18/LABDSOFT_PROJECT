package isep.ipp.pt.Smart_cities.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import isep.ipp.pt.Smart_cities.Controller.EventController;
import isep.ipp.pt.Smart_cities.Dto.EventsDto.EventRequestDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Service.EventService;
import isep.ipp.pt.Smart_cities.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EventController eventController;

    private ObjectMapper objectMapper;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        // Initialize test data
        testEvent = new Event();
        testEvent.setId("event-1");
        testEvent.setTitle("Test Event");
        testEvent.setLocation("Test Location");
        testEvent.setStartDate(LocalDate.now().plusDays(1));
        testEvent.setEndDate(LocalDate.now().plusDays(2));
        testEvent.setCategory("Art");
    }

    @Test
    void testCreateEvent() throws Exception {
        EventRequestDTO requestDTO = EventRequestDTO.builder()
                .creatorID("user-1")
                .title("Test Event")
                .location("Test Location")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2))
                .description("Description")
                .category("Art")
                .latitude(12.34f)
                .longitude(56.78f)
                .build();

        when(eventService.createEvent(any(EventRequestDTO.class))).thenReturn(testEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("event-1"))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.category").value("Art"));

        verify(eventService, times(1)).createEvent(any(EventRequestDTO.class));
    }

    @Test
    void testGetEventById() throws Exception {
        when(eventService.getEventById("event-1")).thenReturn(Optional.of(testEvent));

        mockMvc.perform(get("/api/events/{id}", "event-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("event-1"))
                .andExpect(jsonPath("$.title").value("Test Event"));

        verify(eventService, times(1)).getEventById("event-1");
    }

    @Test
    void testGetEventById_NotFound() throws Exception {
        when(eventService.getEventById("invalid-id")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/events/{id}", "invalid-id"))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEventById("invalid-id");
    }

    @Test
    void testGetAllEvents() throws Exception {
        when(eventService.getAllEvents()).thenReturn(Arrays.asList(testEvent));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("event-1"))
                .andExpect(jsonPath("$[0].title").value("Test Event"));

        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    void testDeleteEvent() throws Exception {
        doNothing().when(eventService).deleteEvent("event-1");

        mockMvc.perform(delete("/api/events/{id}", "event-1"))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEvent("event-1");
    }

    @Test
    void testPromoteEvent() throws Exception {
        when(eventService.promoteEvent("event-1", "user-1")).thenReturn(testEvent);

        mockMvc.perform(post("/api/events/{id}/promote", "event-1")
                        .param("userId", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("event-1"))
                .andExpect(jsonPath("$.title").value("Test Event"));

        verify(eventService, times(1)).promoteEvent("event-1", "user-1");
    }

    @Test
    void testGetPromotedEvents() throws Exception {
        when(eventService.getPromotedEvents()).thenReturn(Arrays.asList(testEvent));

        mockMvc.perform(get("/api/events/promoted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("event-1"));

        verify(eventService, times(1)).getPromotedEvents();
    }
}
