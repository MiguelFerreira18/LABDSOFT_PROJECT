package isep.ipp.pt.Smart_cities.Service;



import isep.ipp.pt.Smart_cities.Model.Milestone;
import isep.ipp.pt.Smart_cities.Model.BadgeCategory;
import isep.ipp.pt.Smart_cities.Respository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilestoneService {

    @Autowired
    private MilestoneRepository milestoneRepository;

    public List<Milestone> getMilestonesByCategory(BadgeCategory category) {
        return milestoneRepository.findByCategory(category);
    }

    public Milestone saveMilestone(Milestone milestone) {
        return milestoneRepository.save(milestone);
    }
}