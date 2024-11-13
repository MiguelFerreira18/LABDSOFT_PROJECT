package isep.ipp.pt.Smart_cities.Initialization;

import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.InstitutionRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import isep.ipp.pt.Smart_cities.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitialization implements CommandLineRunner {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private InstitutionRepo institutionRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@smartcity.com").isEmpty()) {
            User admin = User.builder()
                    .id("478413fc-37fd-4ded-bcbf-1616be5cc647")
                    .email("admin@smartcity.com")
                    .name("NormalAdmin")
                    .password(passwordEncoder.encode("AdminPassword$123"))
                    .build();
            admin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(admin);
        }
        if (userRepository.findByEmail("dev@smartcity.com").isEmpty()) {
            User devAdmin = User.builder()
                    .id("6ba33f06-098a-4ddb-ada6-c689532a458f")
                    .email("dev@smartcity.com")
                    .name("devUserAdmin")
                    .password(passwordEncoder.encode("DevPassword$123"))
                    .build();
            devAdmin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(devAdmin);
        }
        if (institutionRepo.findByEmail("devInstitution@smartcity.com").isEmpty()) {
            Institution institution = Institution.builder()
                    .id("2b804dbc-ce7a-4316-a1e4-553445046a12")
                    .email("devInstitution@smartcity.com")
                    .name("devInstitution")
                    .password(passwordEncoder.encode("DevPassword$123"))
                    .rating(0)
                    .build();
            institution.addAuthority(new Role(Role.INSTITUTION));
            institutionRepo.save(institution);
        }

    }
}
