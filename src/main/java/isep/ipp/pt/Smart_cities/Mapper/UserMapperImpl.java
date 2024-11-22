package isep.ipp.pt.Smart_cities.Mapper;

import isep.ipp.pt.Smart_cities.Authentication.SignUpRequest;
import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.UserModel.UserView;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

// ESTAVA FARTO DO MAPPER QUE NÃO FUNCIONAVA POR ISSO FIZ O MEU
@Component
public class UserMapperImpl implements UserMapper {


    @Override
    public UserView toUserView(User user) {
        if (user == null) {
            return null;
        }

        String id = null;
        String name = null;
        Set<Role> authorities = null;
        Date birthDate = null;
        String gender = null;
        String address = null;
        String city = null;
        String country = null;


        id = user.getId();
        name = user.getName();
        authorities = grantedAuthorityCollectionToRoleSet(user.getAuthorities());
        birthDate = user.getBirthDate();
        gender = user.getGender();
        address = user.getAddress();
        city = user.getCity();
        country = user.getCountry();

        return new UserView(id, name, authorities);
    }

    @Override
    public UserView fromInstitutionToUserView(Institution institution) {
        if (institution == null) {
            return null;
        }

        String id = null;
        String name = null;
        Set<Role> authorities = null;

        id = institution.getId();
        name = institution.getName();
        authorities = grantedAuthorityCollectionToRoleSet(institution.getAuthorities());

        return new UserView(id, name, authorities);
    }

    @Override
    public User toUser(SignUpRequest signUpRequest) {
        if (signUpRequest == null) {
            return null;
        }

        User user = new User();

        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setPassword(signUpRequest.getPassword());

        return user;
    }

    @Override
    public Institution toInstitution(SignUpRequest signUpRequest) {
        if (signUpRequest == null) {
            return null;
        }

        Institution institution = new Institution();

        institution.setEmail(signUpRequest.getEmail());
        institution.setName(signUpRequest.getName());
        institution.setPassword(signUpRequest.getPassword());

        return institution;
    }

    protected Role grantedAuthorityToRole(GrantedAuthority grantedAuthority) {
        if (grantedAuthority == null) {
            return null;
        }

        String authority = null;

        authority = grantedAuthority.getAuthority();

        return new Role(authority);
    }

    protected Set<Role> grantedAuthorityCollectionToRoleSet(Collection<? extends GrantedAuthority> collection) {
        if (collection == null) {
            return null;
        }

        Set<Role> set = new LinkedHashSet<>(Math.max((int) (collection.size() / .75f) + 1, 16));
        for (GrantedAuthority grantedAuthority : collection) {
            set.add(grantedAuthorityToRole(grantedAuthority));
        }

        return set;
    }
}
