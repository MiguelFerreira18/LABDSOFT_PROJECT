package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Model.Badge;
import isep.ipp.pt.Smart_cities.Model.Milestone;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.BadgeRepository;
import isep.ipp.pt.Smart_cities.Respository.MilestoneRepository;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private UserRepo userRepository;

    // Get all badges for a specific user
    public List<Badge> getUserBadges(User user) {
        return badgeRepository.findByUser(user);
    }

    // Get badges for a user by user ID
    public List<Badge> getBadgesForUser(String userId) {
        return badgeRepository.findByUserId(userId);
    }

    // Get all badges (for public/admin use)
    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    // Assign a badge to the user when a milestone is achieved
    public void assignBadgeToUser(String userId, String milestoneId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Milestone> milestoneOpt = milestoneRepository.findById(milestoneId);

        if (userOpt.isEmpty() || milestoneOpt.isEmpty()) {
            throw new IllegalArgumentException("User or Milestone not found");
        }

        User user = userOpt.get();
        Milestone milestone = milestoneOpt.get();

        // Check if the user has already earned a badge for this milestone
        Optional<Badge> existingBadge = badgeRepository.findByUserAndMilestone(user, milestone);
        if (existingBadge.isPresent()) {
            return; // Badge already exists
        }

        Badge userBadge = new Badge();
        userBadge.setUser(user);
        userBadge.setMilestone(milestone);
        userBadge.setCompletionDate(LocalDate.now());
        userBadge.setIconPath("/path/to/icon"); // Placeholder for badge icon

        badgeRepository.save(userBadge);
    }

    // Method to assign an icon to an existing badge
    public Badge assignIconToBadge(Badge badge, String iconPath) {
        badge.setIconPath(iconPath);
        return badgeRepository.save(badge);
    }

    // Create a new badge
    public Badge createBadge(Badge badge) {
        return badgeRepository.save(badge);
    }

    // Update an existing badge
    public Badge updateBadge(String badgeId, Badge badge) {
        badge.setId(badgeId);
        return badgeRepository.save(badge);
    }

    // Delete a badge by ID
    public boolean deleteBadge(String badgeId) {
        if (badgeRepository.existsById(badgeId)) {
            badgeRepository.deleteById(badgeId);
            return true;
        }
        return false;
    }

}