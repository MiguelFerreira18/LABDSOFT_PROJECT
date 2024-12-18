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

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserView toUserView(User user) {
        if (user == null) {
            return null;
        }

        String id = user.getId();
        String name = user.getName();
        Set<Role> authorities = grantedAuthorityCollectionToRoleSet(user.getAuthorities());

        return new UserView(id, name, authorities);
    }

    @Override
    public UserView fromInstitutionToUserView(Institution institution) {
        if (institution == null) {
            return null;
        }

        String id = institution.getId();
        String name = institution.getName();
        Set<Role> authorities = grantedAuthorityCollectionToRoleSet(institution.getAuthorities());

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
  
        if (signUpRequest.getPreferredCategories() != null) {
            user.setPreferredCategories(signUpRequest.getPreferredCategories());
        }

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

        String authority = grantedAuthority.getAuthority();
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
