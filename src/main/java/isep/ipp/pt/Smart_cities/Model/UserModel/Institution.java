package isep.ipp.pt.Smart_cities.Model.UserModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Mod10Check;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@Entity
public class Institution extends User  {

    @Min(value = 0, message = "Rating must be a positive number")
    @Max(value = 5, message = "Rating must be a number between 0 and 5")
    private float rating;

    public Institution() {
        super();
    }

    public Institution(float rating) {
        this.rating = rating;
    }

    public Institution(String email, String password, float rating) {
        super(email, password);
        this.rating = rating;
    }

    public Institution(String email, String username, String password, Role role, float rating) {
        super(email, username, password, role);
        this.rating = rating;
    }

    public Institution(String email, String username, String password, float rating) {
        super(email, username, password);
        this.rating = rating;
    }

    public Institution(String id, String email, String name, String password, float rating) {
        super(id, email, name, password);
        this.rating = rating;
    }

    public Institution(String id, String email, String name, Set<Role> authorities, String password, float rating) {
        super(id, email, name, authorities, password);
        this.rating = rating;
    }

    public Institution(String id, String email, String name, Set<Role> authorities, String password, LocalDateTime lastLoginAt, float rating) {
        super(id, email, name, authorities, password, lastLoginAt);
        this.rating = rating;
    }


}
