package isep.ipp.pt.Smart_cities.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import isep.ipp.pt.Smart_cities.Model.Badge;
import isep.ipp.pt.Smart_cities.Model.BadgeCategory;
import isep.ipp.pt.Smart_cities.Respository.BadgeRepository;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final UserRepo userRepository;

    public List<Badge> getUserBadges(String userId) {
        return badgeRepository.findAllByUserId(userId);
    }

    public Badge createBadge(String userId, String name, String description, BadgeCategory category) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Badge badge = new Badge();
        badge.setName(name);
        badge.setDescription(description);
        badge.setCategory(category);
        badge.setUser(user);
        return badgeRepository.save(badge);
    }

    public Badge markBadgeAsCompleted(Long badgeId) {
        var badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new RuntimeException("Badge not found"));

        badge.setCompletionDate(LocalDateTime.now());
        return badgeRepository.save(badge);
    }
}