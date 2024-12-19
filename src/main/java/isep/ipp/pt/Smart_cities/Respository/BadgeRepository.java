package isep.ipp.pt.Smart_cities.Respository;

import org.springframework.data.jpa.repository.JpaRepository;

import isep.ipp.pt.Smart_cities.Model.Badge;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findAllByUserId(String userId);
}

