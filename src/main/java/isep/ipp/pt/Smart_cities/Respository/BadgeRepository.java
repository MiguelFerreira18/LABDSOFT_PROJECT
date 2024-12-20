package isep.ipp.pt.Smart_cities.Respository;

import isep.ipp.pt.Smart_cities.Model.Badge;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, String> {
    List<Badge> findByUser(User user);
    List<Badge> findByUserId(String userId);

     Optional<Badge> findByUserAndMilestone(User user, Milestone milestone);
    }