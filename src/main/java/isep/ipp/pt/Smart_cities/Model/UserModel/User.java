package isep.ipp.pt.Smart_cities.Model.UserModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Entity
@ToString
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @Column(unique = true)
    @Email
    protected String email;

    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must contain only letters and numbers")
    protected String name;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    protected Set<Role> authorities = new HashSet<>();

    protected String password;

    protected LocalDateTime lastLoginAt = LocalDateTime.now();

    public User() {
        this.authorities = new HashSet<>();

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.authorities = new HashSet<>();
    }

    public User(String email,String username, String password, Role role) {
        this.email = email;
        this.name = username;
        this.password = password;
        this.authorities.add(role);
    }
    public User(String email,String username, String password) {
        this.email = email;
        this.name = username;
        this.password = password;
        this.authorities = new HashSet<>();
    }

    public User(String id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.authorities = new HashSet<>();
        this.password = password;
    }

    public User(String id, String email, String name, Set<Role> authorities, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.authorities = authorities;
        this.password = password;
    }

    public User(String id, String email, String name, Set<Role> authorities, String password, LocalDateTime lastLoginAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.authorities = authorities;
        this.password = password;
        this.lastLoginAt = lastLoginAt;
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

    @PrePersist
    public void ensureId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
