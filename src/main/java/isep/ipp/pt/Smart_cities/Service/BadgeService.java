package isep.ipp.pt.Smart_cities.Service;


import isep.ipp.pt.Smart_cities.Model.Badge;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    public List<Badge> getUserBadges(User user) {
        return badgeRepository.findByUser(user);
    }

    public Badge saveBadge(Badge badge) {
        return badgeRepository.save(badge);
    }

    public void assignIconToBadge(Badge badge, String iconPath) {
        badge.setIconPath(iconPath);
        badgeRepository.save(badge);
    }

    public Badge getBadgeById(Long badgeId) {
        return badgeRepository.findById(badgeId).orElseThrow(() -> new IllegalArgumentException("Badge not found"));
    }
}