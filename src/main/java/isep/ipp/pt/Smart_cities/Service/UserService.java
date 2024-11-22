package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.UserModel.UserView;
import isep.ipp.pt.Smart_cities.Respository.InstitutionRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    public PasswordEncoder encoder;

    @Autowired
    public UserRepo userRepo;

    @Autowired
    public InstitutionRepo institutionRepo;


    public Optional<User> saveUser(User user) {
        System.out.println("Saving user1: " + user);

        Optional<User> userOptional;
        try {
            user.setPassword(user.getPassword(), encoder);
            user.addAuthority(new Role(Role.USER));
            userOptional = Optional.of(userRepo.save(user));
        } catch (Exception e) {
            return Optional.empty();
        }
        return userOptional;
    }

    public void DeleteAllUsers() {
        userRepo.deleteAll();
    }

    public void updateUserLastLogin(String userEmail) {
        userRepo.updateUserLastLogin(userEmail, LocalDateTime.now());
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }

        Optional<Institution> institution = institutionRepo.findByEmail(email);
        if (institution.isPresent()) {
            return institution.get();
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    public void deleteUser(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        user.ifPresent(value -> userRepo.delete(value));
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        return user.orElse(null);
    }

    public User findById(String userId) {

        Optional<User> user = userRepo.findById(userId);

        return user.orElse(null);
    }

    public UserView toUserView(User user) {
        if (user == null) {
            return null;
        }

        String id = user.getId();
        String name = user.getName();
        Set<Role> authorities = null;
        Date birth = user.getBirthDate();
        String gender = user.getGender();
        String address = user.getAddress();
        String city = user.getCity();
        String country = user.getCountry();

        return new UserView(id, name, authorities, birth, gender, address, city, country);
    }
}
