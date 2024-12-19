package isep.ipp.pt.Smart_cities.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isep.ipp.pt.Smart_cities.Model.Badge;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    private final BadgeService badgeService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Badge>> getUserBadges(@PathVariable String userId) {
        return ResponseEntity.ok(badgeService.getUserBadges(userId));
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<Badge> createBadge(
            @PathVariable String userId,
            @RequestBody BadgeRequest badgeRequest) {
        var badge = badgeService.createBadge(
                userId,
                badgeRequest.getName(),
                badgeRequest.getDescription(),
                badgeRequest.getCategory()
        );
        return ResponseEntity.ok(badge);
    }

    @PostMapping("/{badgeId}/complete")
    public ResponseEntity<Badge> markBadgeAsCompleted(@PathVariable Long badgeId) {
        return ResponseEntity.ok(badgeService.markBadgeAsCompleted(badgeId));
    }
}

class BadgeRequest {
    private String name;
    private String description;
    private BadgeCategory category;

    // Getters and setters
}

