package isep.ipp.pt.Smart_cities.Respository;


import isep.ipp.pt.Smart_cities.Model.Milestone;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.Badge;
import isep.ipp.pt.Smart_cities.Model.BadgeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

 @Repository
    public interface MilestoneRepository extends JpaRepository<Milestone, String> {
        // Find milestones by category (assuming BadgeCategory is an enum or class).
        List<Milestone> findByCategory(BadgeCategory category);
    
        // Find milestones by name
        Optional<Milestone> findByName(String name);
    }

    //
