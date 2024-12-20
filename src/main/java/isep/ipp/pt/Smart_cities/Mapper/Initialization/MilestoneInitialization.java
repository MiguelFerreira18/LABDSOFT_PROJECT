package isep.ipp.pt.Smart_cities.Mapper.Initialization;
/*
import isep.ipp.pt.Smart_cities.Model.Milestone;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.MilestoneRepository;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        // Example milestones, add more as needed
        createMilestone("First Event Attended", "Attend your first event through the app.");
        createMilestone("Social Butterfly", "Attend 3 events in a month.");
        createMilestone("Event Explorer", "Attend 3 different types of events.");
        createMilestone("Crowd Pleaser", "Check in with at least 5 people at an event.");
        createMilestone("Group Organizer", "Create and organize your own event.");
        createMilestone("Social Star", "Receive 10 or more likes/reactions for checking in.");
        createMilestone("Weekend Warrior", "Attend 4 events over a month, with at least 1 event each weekend.");
        createMilestone("Monthly Marathoner", "Attend at least 1 event every weekend for a month.");
        createMilestone("Event Regular", "Attend 10 events over a period of 3 months.");
    }

    private void createMilestone(String name, String description) {
        // Check if the milestone already exists before creating it
        if (milestoneRepository.findByName(name).isEmpty()) {
            Milestone milestone = new Milestone();
            milestone.setName(name);
            milestone.setDescription(description);
            milestoneRepository.save(milestone);
        }
    }
}*/