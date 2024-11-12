package isep.ipp.pt.Smart_cities.Model.UserModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Mod10Check;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Institution implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    @Email
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must contain only letters and numbers")
    private String name;

    @ElementCollection
    private Set<Role> authorities = new HashSet<>();

    private String password;

    @Size(min = 0, max = 5)
    private float rating;

    public Institution() {
    }
    public Institution(String email, String password) {
        this.email = email;
        this.authorities = new HashSet<>();
        this.password = password;
        this.rating = 0;
    }

    public Institution(String email, String password, float rating) {
        this.email = email;
        this.authorities = new HashSet<>();
        this.password = password;
        this.rating = rating;
    }

    public Institution(String id, String email, Set<Role> authorities, String password, float rating) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
        this.password = password;
        this.rating = rating;
    }

    public void setPassword(String password, PasswordEncoder encoder) {
        if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,128}$")) {
            this.password = encoder.encode(password);
        } else {
            throw new IllegalArgumentException("Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character");
        }
    }

    public void addAuthority(Role role) {
        authorities.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
