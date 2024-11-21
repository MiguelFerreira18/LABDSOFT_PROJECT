package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Responses.Response;
import isep.ipp.pt.Smart_cities.Service.RewardsService;
import isep.ipp.pt.Smart_cities.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @Autowired
    private UserService userService;

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

    @PostMapping("/{userId}/daily")
    public ResponseEntity<?> dailyStreakLogin(@PathVariable String userId){

        var user = userService.findById(userId);

        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.badRequest("User not found."));
        }

        /*
        if(user.hasLoggedInToday(user.getLastLoginAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.badRequest("User already received the points."));
        }*/

        var rewardsDto = rewardsService.givePointsByStreakLogin(user);

        return ResponseEntity.ok(rewardsDto);
    }
}
