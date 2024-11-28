

package isep.ipp.pt.Smart_cities.Mapper.Initialization;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class EventInitialization implements CommandLineRunner {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Verifica se o usuário já existe, caso contrário, cria um novo
        if (userRepository.findByEmail("AnyNormalUser@gmail.com").isEmpty()) {
            User user = User.builder()
                    .email("AnyNormalUser@gmail.com")
                    .name("NormalUser")
                    .password(passwordEncoder.encode("UserPassword$123"))
                    .build();
            user.addAuthority(new Role(Role.USER));
            userRepository.save(user);
        }

        User user = userRepository.findByEmail("AnyNormalUser@gmail.com").get();

        // Criação de eventos com verificação para evitar duplicados

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e5f")) {
            Event event1 = new Event();
            event1.setId("0b2f589d-b5c2-4616-958c-504eeca80e5f");
            event1.setTitle("Tech Innovators Conference");
            event1.setLocation("Lisbon, Portugal");
            event1.setStartDate(LocalDateTime.of(2024, 12, 10, 9, 0));
            event1.setEndDate(LocalDateTime.of(2024, 12, 15, 17, 0));
            event1.setLimit(10);
            event1.setDescription("A conference on the latest tech innovations.");
            event1.setCreator(user);
            event1.setCategory("Art");
            eventRepository.save(event1);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e60")) {
            Event event2 = new Event();
            event2.setId("0b2f589d-b5c2-4616-958c-504eeca80e60");
            event2.setTitle("Summer Music Festival");
            event2.setLocation("Porto, Portugal");
            event2.setStartDate(LocalDateTime.of(2025, 6, 10, 9, 0));
            event2.setEndDate(LocalDateTime.of(2025, 6, 12, 23, 59));
            event2.setLimit(1);
            event2.setDescription("A 3-day music festival featuring international artists.");
            event2.setCreator(user);
            event2.setCategory("Sports");
            eventRepository.save(event2);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e61")) {
            Event event3 = new Event();
            event3.setId("0b2f589d-b5c2-4616-958c-504eeca80e61");
            event3.setTitle("Modern Art Expo");
            event3.setLocation("Lisbon, Portugal");
            event3.setStartDate(LocalDateTime.of(2025, 11, 20, 9, 0));
            event3.setEndDate(LocalDateTime.of(2025, 11, 25, 17, 0));
            event3.setLimit(5);
            event3.setDescription("An exhibition showcasing modern art.");
            event3.setCreator(user);
            event3.setCategory( "Social");
            eventRepository.save(event3);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e62")) {
            Event event4 = new Event();
            event4.setId("0b2f589d-b5c2-4616-958c-504eeca80e62");
            event4.setTitle("International Football Tournament");
            event4.setLocation("Madrid, Spain");
            event4.setStartDate(LocalDateTime.of(2025, 3, 10, 9, 0));
            event4.setEndDate(LocalDateTime.of(2025, 3, 15, 23, 59));
            event4.setLimit(20);
            event4.setDescription("A football tournament with teams from across the globe.");
            event4.setCreator(user);
            event4.setCategory("Volunteering");
            eventRepository.save(event4);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e63")) {
            Event event5 = new Event();
            event5.setId("0b2f589d-b5c2-4616-958c-504eeca80e63");
            event5.setTitle("Java Programming Bootcamp");
            event5.setLocation("Lisbon, Portugal");
            event5.setStartDate(LocalDateTime.of(2026, 8, 10, 9, 0));
            event5.setEndDate(LocalDateTime.of(2026, 8, 15, 17, 0));
            event5.setLimit(20);
            event5.setDescription("A bootcamp for learning Java programming.");
            event5.setCreator(user);
            event5.setCategory("Educational");
            eventRepository.save(event5);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e64")) {
            Event event6 = new Event();
            event6.setId("0b2f589d-b5c2-4616-958c-504eeca80e64");
            event6.setTitle("Startup Networking Event");
            event6.setLocation("Porto, Portugal");
            event6.setStartDate(LocalDateTime.of(2024, 12, 10, 9, 0));
            event6.setEndDate(LocalDateTime.of(2024, 12, 10, 17, 0));
            event6.setLimit(20);
            event6.setDescription("A networking event for entrepreneurs and investors.");
            event6.setCreator(user);
            event6.setCategory( "Recreational");
            eventRepository.save(event6);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e65")) {
            Event event7 = new Event();
            event7.setId("0b2f589d-b5c2-4616-958c-504eeca80e65");
            event7.setTitle("Health and Wellness Seminar");
            event7.setLocation("Braga, Portugal");
            event7.setStartDate(LocalDateTime.of(2024, 12, 15, 9, 0));
            event7.setEndDate(LocalDateTime.of(2024, 12, 15, 17, 0));
            event7.setLimit(20);
            event7.setDescription("A seminar on mental and physical health improvement.");
            event7.setCreator(user);
            event7.setCategory("Political");
            eventRepository.save(event7);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e66")) {
            Event event8 = new Event();
            event8.setId("0b2f589d-b5c2-4616-958c-504eeca80e66");
            event8.setTitle("Gastronomy Festival");
            event8.setLocation("Funchal, Madeira");
            event8.setStartDate(LocalDateTime.of(2025, 2, 10, 8, 0));
            event8.setEndDate(LocalDateTime.of(2025, 2, 14, 23, 59));
            event8.setLimit(20);
            event8.setDescription("A festival dedicated to the best food and drinks.");
            event8.setCreator(user);
            event8.setCategory( "Sports");
            eventRepository.save(event8);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e67")) {
            Event event9 = new Event();
            event9.setId("0b2f589d-b5c2-4616-958c-504eeca80e67");
            event9.setTitle("Charity Run for Children");
            event9.setLocation("Lisbon, Portugal");
            event9.setStartDate(LocalDateTime.of(2024, 12, 22, 8, 0));
            event9.setEndDate(LocalDateTime.of(2024, 12, 22, 12, 0));
            event9.setLimit(20);
            event9.setDescription("A charity run to raise funds for underprivileged children.");
            event9.setCreator(user);
            event9.setCategory("Art");
            eventRepository.save(event9);
        }

        if (!eventRepository.existsById("0b2f589d-b5c2-4616-958c-504eeca80e68")) {
            Event event10 = new Event();
            event10.setId("0b2f589d-b5c2-4616-958c-504eeca80e68");
            event10.setTitle("Innovation and Startup Forum");
            event10.setLocation("Lisbon, Portugal");
            event10.setStartDate(LocalDateTime.of(2025, 11, 25, 9, 0));
            event10.setEndDate(LocalDateTime.of(2025, 11, 25, 17, 0));
            event10.setLimit(20);
            event10.setDescription("A forum for discussing the latest innovations and startup trends.");
            event10.setCreator(user);
            event10.setCategory("Educational");
            eventRepository.save(event10);
        }
    }
}
