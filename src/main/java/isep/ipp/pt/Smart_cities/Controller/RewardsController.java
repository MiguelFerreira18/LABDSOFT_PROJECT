package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;


    @PostMapping("/claim/{userId}/{eventId}")
    public ResponseEntity<Response> claimRewardsForAttendingAnEvent(@PathVariable String userId,@PathVariable String eventId) {
        Response response = rewardsService.givePointsByAttendingAnEvent(userId, eventId)
                .orElse(Response.internalError("An error occurred while processing the request."));
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/points/{userId}")
    public ResponseEntity<Response> getPointsByUserId(@PathVariable String userId) {
        Response response = rewardsService.calculateTotalRewardsFromUserId(userId)
                .orElse(Response.notFound("User not found"));
        return ResponseEntity.status(response.statusCode()).body(response);
    }

}
