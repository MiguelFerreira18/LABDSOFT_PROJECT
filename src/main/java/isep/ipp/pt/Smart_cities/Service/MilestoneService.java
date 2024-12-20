package isep.ipp.pt.Smart_cities.Service;



import isep.ipp.pt.Smart_cities.Model.Milestone;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.Badge;
import isep.ipp.pt.Smart_cities.Model.BadgeCategory;
import isep.ipp.pt.Smart_cities.Respository.BadgeRepository;
import isep.ipp.pt.Smart_cities.Respository.MilestoneRepository;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
public class MilestoneService {

    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private BadgeRepository badgeRepository;
    @Autowired
    private UserRepo userRepository;

    // Get milestones by category
    public List<Milestone> getMilestonesByCategory(BadgeCategory category) {
        return milestoneRepository.findByCategory(category);
    }

    // Save a new milestone
    public Milestone saveMilestone(Milestone milestone) {
        return milestoneRepository.save(milestone);
    }

    // Complete a milestone for a user (create a badge)
    public void completeMilestone(String userId, String milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user has already received a badge for this milestone
        Optional<Badge> existingBadge = badgeRepository.findByUserAndMilestone(user, milestone);

        if (existingBadge.isEmpty()) {
            Badge userBadge = new Badge();
            userBadge.setUser(user);
            userBadge.setMilestone(milestone);
            userBadge.setCompletionDate(LocalDateTime.now());  // Set completion date

            badgeRepository.save(userBadge);  // Save the badge
        } else {
            System.out.println("Badge already awarded for this milestone.");
        }
    }

    // Get all milestones (for public/admin use)
    public List<Milestone> getAllMilestones() {
        return milestoneRepository.findAll();
    }

    // Optional version to get a milestone by ID
    public Optional<Milestone> getMilestoneByIdOptional(String milestoneId) {
        return milestoneRepository.findById(milestoneId);
    }

    // Update an existing milestone
  

    // Delete a milestone by ID
    public boolean deleteMilestone(String milestoneId) {
        if (milestoneRepository.existsById(milestoneId)) {
            milestoneRepository.deleteById(milestoneId);
            return true;
        }
        return false;
    }
}