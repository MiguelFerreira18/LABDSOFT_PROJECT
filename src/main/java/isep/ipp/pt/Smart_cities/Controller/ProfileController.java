package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Model.Badge;
import isep.ipp.pt.Smart_cities.Model.BadgeCategory;
import isep.ipp.pt.Smart_cities.Model.Milestone;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Service.BadgeService;
import isep.ipp.pt.Smart_cities.Service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private MilestoneService milestoneService;

  // Endpoint to assign a badge to a user based on milestone name and user id
  @PostMapping("/badge/assign")
  public String assignBadgeToUser(@RequestBody BadgeAssignmentRequest request) {
      try {
          badgeService.assignBadgeToUser(request.getUserId(), request.getMilestoneName());
          return "Badge assigned successfully";
      } catch (IllegalArgumentException e) {
          return "Error: " + e.getMessage();
      }
  }
 
  @PostMapping("/badges")
  public ResponseEntity<List<Badge>> getUserBadges(@RequestBody BadgeAssignmentRequest request) {
      List<Badge> badges = badgeService.getBadgesForUser(request.getUserId());
      if (badges.isEmpty()) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
      }
      return ResponseEntity.ok(badges);
  }

    // Endpoint to get all badges (admin or public use)
    @GetMapping("/badges/all")
    public List<Badge> getAllBadges() {
        return badgeService.getAllBadges();
    }

    // Endpoint to get milestones by category
    @GetMapping("/milestones")
    public List<Milestone> getMilestonesByCategory(@RequestParam BadgeCategory category) {
        return milestoneService.getMilestonesByCategory(category);
    }

    // Endpoint to get all milestones (admin or public use)
    @GetMapping("/milestones/all")
    public List<Milestone> getAllMilestones() {
        return milestoneService.getAllMilestones();
    }

    // Endpoint to check if a user has earned any milestones
    @GetMapping("/milestones/none")
    public String noMilestonesEarned(@RequestParam User user) {
        List<Badge> badges = badgeService.getUserBadges(user);
        if (badges.isEmpty()) {
            return "No milestones have been earned yet.";
        } else {
            return "Milestones earned.";
        }
    }

    // Endpoint to assign an icon to a badge
    @PostMapping("/badges/icon")
    public Badge assignIconToBadge(@RequestParam String badgeId, @RequestParam String iconPath) {
        Badge badge = badgeService.getBadgeById(badgeId);
        return badgeService.assignIconToBadge(badge, iconPath);
    }

    // Endpoint to create a new badge
    @PostMapping("/badge/create")
    public ResponseEntity<Badge> createBadge(@RequestBody Badge badge) {
        Badge createdBadge = badgeService.createBadge(badge);
        return ResponseEntity.ok(createdBadge);
    }

    // Endpoint to update an existing badge
   

    // Endpoint to delete a badge
    @DeleteMapping("/badges/{badgeId}")
    public ResponseEntity<Void> deleteBadge(@PathVariable String badgeId) {
        if (badgeService.deleteBadge(badgeId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to create a new milestone
    @PostMapping("/milestones")
    public ResponseEntity<Milestone> createMilestone(@RequestBody Milestone milestone) {
        Milestone createdMilestone = milestoneService.saveMilestone(milestone);
        return ResponseEntity.ok(createdMilestone);
    }

    // Endpoint to update an existing milestone
    @PutMapping("/milestones/{milestoneId}")
    public ResponseEntity<Milestone> updateMilestone(@PathVariable String milestoneId, @RequestBody Milestone milestone) {
        Optional<Milestone> existingMilestone = milestoneService.getMilestoneByIdOptional(milestoneId);
        if (existingMilestone.isPresent()) {
            Milestone updatedMilestone = milestoneService.updateMilestone(milestoneId, milestone);
            return ResponseEntity.ok(updatedMilestone);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to delete a milestone
    @DeleteMapping("/milestones/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable String milestoneId) {
        if (milestoneService.deleteMilestone(milestoneId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}