package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Respository.InstitutionRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstitutionService {
    @Autowired
    private InstitutionRepo institutionRepo;
    @Autowired
    private PasswordEncoder encoder;

    public Optional<Institution> saveInstitution(Institution institution) {
        Optional<Institution> institutionOptional;
        try {
            institution.setPassword(institution.getPassword(), encoder);
            institution.addAuthority(new Role(Role.INSTITUTION));
            institutionOptional = Optional.of(institutionRepo.save(institution));
        } catch (Exception e) {
            return Optional.empty();
        }
        return institutionOptional;
    }
}
