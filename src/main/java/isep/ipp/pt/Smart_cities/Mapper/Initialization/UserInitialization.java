package isep.ipp.pt.Smart_cities.Mapper.Initialization;

import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.InstitutionRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
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
        if (userRepository.findByEmail("AnyNormalUser@gmail.com").isEmpty()) {
            User user = User.builder()
                    .email("AnyNormalUser@gmail.com")
                    .name("NormalUser")
                    .password(passwordEncoder.encode("UserPassword$123"))
                    .build();
            user.addAuthority(new Role(Role.USER));
            userRepository.save(user);
        }
        if (userRepository.findByEmail("admin@smartcity.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@smartcity.com")
                    .name("NormalAdmin")
                    .password(passwordEncoder.encode("AdminPassword$123"))
                    .build();
            admin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(admin);
        }
        if (userRepository.findByEmail("dev@smartcity.com").isEmpty()) {
            User devAdmin = User.builder()
                    .email("dev@smartcity.com")
                    .name("devUserAdmin")
                    .password(passwordEncoder.encode("DevPassword$123"))
                    .build();
            devAdmin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(devAdmin);
        }
        if (institutionRepo.findByEmail("devInstitution@smartcity.com").isEmpty()) {
            Institution institution = Institution.builder()
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
