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

import java.util.Date;
import java.util.Optional;

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
                    .birthDate(new Date())
                    .gender("Male")
                    .address("Rua 1")
                    .city("Lisboa")
                    .country("Portugal")
                    .build();
            user.addAuthority(new Role(Role.USER));
            userRepository.save(user);
        } else {
            Optional<User> userOpt = userRepository.findByEmail("AnyNormalUser@gmail.com");
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.getBirthDate() == null) {
                    user.setBirthDate(new Date());
                    user.setGender("Male");
                    user.setAddress("Rua 2");
                    user.setCity("Porto");
                    user.setCountry("Portugal");
                    userRepository.save(user);
                }
            }
        }

        if (userRepository.findByEmail("admin@smartcity.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@smartcity.com")
                    .name("NormalAdmin")
                    .password(passwordEncoder.encode("AdminPassword$123"))
                    .birthDate(new Date())
                    .gender("Male")
                    .address("Rua 3")
                    .city("Faro")
                    .country("Portugal")
                    .build();
            admin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(admin);
        } else {
            Optional<User> adminOpt = userRepository.findByEmail("admin@smartcity.com");
            if (adminOpt.isPresent()) {
                User admin = adminOpt.get();
                if (admin.getBirthDate() == null) {
                    admin.setBirthDate(new Date());
                    admin.setGender("Male");
                    admin.setAddress("Rua 4");
                    admin.setCity("Viseu");
                    admin.setCountry("Portugal");
                    userRepository.save(admin);
                }
            }
        }

        if (userRepository.findByEmail("dev@smartcity.com").isEmpty()) {
            User devAdmin = User.builder()
                    .email("dev@smartcity.com")
                    .name("devUserAdmin")
                    .password(passwordEncoder.encode("DevPassword$123"))
                    .birthDate(new Date())
                    .gender("Male")
                    .address("Rua 5")
                    .city("Viana do Castelo")
                    .country("Portugal")
                    .build();
            devAdmin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(devAdmin);
        } else {
            Optional<User> devAdminOpt = userRepository.findByEmail("dev@smartcity.com");
            if (devAdminOpt.isPresent()) {
                User devAdmin = devAdminOpt.get();
                if (devAdmin.getBirthDate() == null) {
                    devAdmin.setBirthDate(new Date());
                    devAdmin.setGender("Male");
                    devAdmin.setAddress("Rua 6");
                    devAdmin.setCity("Braga");
                    devAdmin.setCountry("Portugal");
                    userRepository.save(devAdmin);
                }
            }
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