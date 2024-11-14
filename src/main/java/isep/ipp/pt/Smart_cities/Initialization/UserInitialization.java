package isep.ipp.pt.Smart_cities.Initialization;

import isep.ipp.pt.Smart_cities.Model.UserInfoModel.UserInfo;
import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.InstitutionRepo;
import isep.ipp.pt.Smart_cities.Respository.UserInfoRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import isep.ipp.pt.Smart_cities.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserInitialization implements CommandLineRunner {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private UserInfoRepo userInfoRepo;
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

            User user = userRepository.findByEmail("admin@smartcity.com").get();

            if (userInfoRepo.findByUserId(user.getId()).isEmpty()) {
                System.out.println("entrei");
                UserInfo userInfo = new UserInfo(user.getId(), new Date(), "Male", "Rua 1", "Lisboa", "Portugal");
                System.out.println(userInfo.toString());
                userInfoRepo.save(userInfo);
            }

        }
        if (userRepository.findByEmail("dev@smartcity.com").isEmpty()) {
            User admin = new User("dev@smartcity.com", passwordEncoder.encode("DevPassword$123"));
            admin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(admin);

            User user = userRepository.findByEmail("dev@smartcity.com").get();

            if (userInfoRepo.findByUserId(user.getId()).isEmpty()) {
                UserInfo userInfo = new UserInfo(user.getId(), new Date(), "Female", "Rua 2", "Porto", "Portugal");
                userInfoRepo.save(userInfo);
            }

        }
        if (institutionRepo.findByEmail("devInstitution@smartcity.com").isEmpty()) {
            Institution institution = new Institution("devInstitution@smartcity.com",  passwordEncoder.encode("DevInstiPassword$123"));
            institution.addAuthority(new Role(Role.INSTITUTION));
            institutionRepo.save(institution);
        }

        if (userRepository.findByEmail("admin@smartcity.com").isEmpty()) {
            User admin = new User("admin@smartcity.com", passwordEncoder.encode("AdminPassword$123"));
            admin.addAuthority(new Role(Role.ADMIN));
            userRepository.save(admin);
        }

    }
}
