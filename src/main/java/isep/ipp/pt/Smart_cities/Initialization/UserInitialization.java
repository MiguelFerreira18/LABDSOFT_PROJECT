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
            User admin = new User("admin@smartcity.com", passwordEncoder.encode("AdminPassword$123"));
            admin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(admin);
        }
        if (userRepository.findByEmail("dev@smartcity.com").isEmpty()) {
            User admin = new User("dev@smartcity.com", passwordEncoder.encode("DevPassword$123"));
            admin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(admin);
        }
        if (institutionRepo.findByEmail("devInstitution@smartcity.com").isEmpty()) {
            Institution institution = new Institution("devInstitution@smartcity.com",  passwordEncoder.encode("DevInstiPassword$123"));
            institution.addAuthority(new Role(Role.INSTITUTION));
            institutionRepo.save(institution);
        }

    }
}
