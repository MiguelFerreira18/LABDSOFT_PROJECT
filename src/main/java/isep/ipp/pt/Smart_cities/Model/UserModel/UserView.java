package isep.ipp.pt.Smart_cities.Model.UserModel;

import lombok.Data;

import java.util.Set;

@Data
public class UserView {
    String id;
    String name;
    Set<Role> authorities;

    public UserView(String id, String name, Set<Role> authorities) {
        this.id = id;
        this.name = name;
        this.authorities = authorities;
    }

}
