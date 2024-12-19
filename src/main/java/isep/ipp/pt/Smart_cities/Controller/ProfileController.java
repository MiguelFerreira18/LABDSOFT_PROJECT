package isep.ipp.pt.Smart_cities.Controller;


import isep.ipp.pt.Smart_cities.Model.Badge;
import isep.ipp.pt.Smart_cities.Model.BadgeCategory;
import isep.ipp.pt.Smart_cities.Model.Milestone;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Service.BadgeService;
import isep.ipp.pt.Smart_cities.Service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private MilestoneService milestoneService;

    // Endpoint to get user badges
    @GetMapping("/badges")
    public List<Badge> getUserBadges(@RequestParam User user) {
        return badgeService.getUserBadges(user);
    }

    // Endpoint to get milestones by category
    @GetMapping("/milestones")
    public List<Milestone> getMilestonesByCategory(@RequestParam BadgeCategory category) {
        return milestoneService.getMilestonesByCategory(category);
    }

    // Endpoint to notify no milestones earned
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
    public Badge assignIconToBadge(@RequestParam Long badgeId, @RequestParam String iconPath) {
        Badge badge = badgeService.getBadgeById(badgeId);
        badgeService.assignIconToBadge(badge, iconPath);
        return badge;
    }
}

