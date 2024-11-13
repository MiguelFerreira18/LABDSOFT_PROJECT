package isep.ipp.pt.Smart_cities.Initialization;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.EventRepository;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
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


        if (eventRepository.findByTitle("AnyTitle1").isEmpty()) {
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
            Event event = new Event();
            event.setId("0b2f589d-b5c2-4616-958c-504eeca80e5f");
            event.setTitle("AnyTitle1");
            event.setLocation("AnyLocation1");
            event.setStartDate(java.time.LocalDate.now());
            event.setEndDate(java.time.LocalDate.now().plusDays(1000));
            event.setDescription("AnyDescription1");
            event.setCreator(user);
            event.setCategories(Set.of("AnyCategory1"));
            eventRepository.save(event);
        }
    }
}
