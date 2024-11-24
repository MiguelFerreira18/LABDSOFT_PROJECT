package isep.ipp.pt.Smart_cities.Model.UserModel;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

@Value
@AllArgsConstructor
public class Role implements GrantedAuthority {

    public static final String ADMIN = "Admin";
    public static final String  INSTITUTION= "Institution";
    public static final String USER = "User";
    String authority;
}
