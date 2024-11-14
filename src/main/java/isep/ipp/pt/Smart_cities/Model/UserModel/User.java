package isep.ipp.pt.Smart_cities.Model.UserModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Setter
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    @Email
    private String email;

    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must contain only letters and numbers")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> authorities = new HashSet<>();

    private String password;


    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.authorities = new HashSet<>();
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.authorities.add(role);
    }

    public void setPassword(String password, PasswordEncoder encoder) {
        // Verify the follows these conditions: at least 8 characters, one uppercase, one lowercase, one number and one special character

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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
