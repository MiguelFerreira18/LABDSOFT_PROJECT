package isep.ipp.pt.Smart_cities.Mapper.Initialization;

import isep.ipp.pt.Smart_cities.Model.BadgeCategory;
import isep.ipp.pt.Smart_cities.Model.Milestone;
import isep.ipp.pt.Smart_cities.Respository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import isep.ipp.pt.Smart_cities.Model.BadgeCategory;  // Assuming you imported BadgeCategory

@Component
public class MilestoneInitialization implements CommandLineRunner {

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize milestones if they don't exist already
        if (milestoneRepository.count() == 0) {

            // Create predefined milestones and save them to the database
            createMilestones();
        }
    }

    private void createMilestones() {
        // Example milestones, add more as needed. You can map each milestone to a category.
        createMilestone("First Event Attended", "Attend your first event through the app.", BadgeCategory.SOCIAL_INTERACTION);
        createMilestone("Social Butterfly", "Attend 3 events in a month.", BadgeCategory.FREQUENCY_LOYALTY);
        createMilestone("Event Explorer", "Attend 3 different types of events.", BadgeCategory.SOCIAL_INTERACTION);
        createMilestone("Crowd Pleaser", "Check in with at least 5 people at an event.", BadgeCategory.SOCIAL_INTERACTION);
        createMilestone("Group Organizer", "Create and organize your own event.", BadgeCategory.SOCIAL_INTERACTION);
        createMilestone("Social Star", "Receive 10 or more likes/reactions for checking in.", BadgeCategory.SOCIAL_INTERACTION);
        createMilestone("Weekend Warrior", "Attend 4 events over a month, with at least 1 event each weekend.", BadgeCategory.FREQUENCY_LOYALTY);
        createMilestone("Monthly Marathoner", "Attend at least 1 event every weekend for a month.", BadgeCategory.FREQUENCY_LOYALTY);
        createMilestone("Event Regular", "Attend 10 events over a period of 3 months.", BadgeCategory.FREQUENCY_LOYALTY);
            }
        
           
        
            private void createMilestone(String name, String description,BadgeCategory category) {
        // Check if the milestone already exists before creating it
        if (milestoneRepository.findByName(name).isEmpty()) {
            Milestone milestone = new Milestone();
            milestone.setName(name);
            milestone.setDescription(description);
            milestone.setCategory(category);  // Set the category from BadgeCategory constants
            milestoneRepository.save(milestone);
        }
    }
}

   
