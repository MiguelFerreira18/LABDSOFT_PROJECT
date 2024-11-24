package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Model.EventModel.EventSummary;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/events")
public class EventDashboardController {

    @Autowired
    private EventService eventService;

    @GetMapping("/dashboard/{userId}")
    public List<EventSummary> getDashboardSummaries(@PathVariable String userId) {
        return eventService.generateCurrentEventSummaries(userId);
    }
    @GetMapping("/summaries")
public List<EventSummary> getAllEventSummaries() {
    return eventService.getEventSummariesWithDetails();
}

}
