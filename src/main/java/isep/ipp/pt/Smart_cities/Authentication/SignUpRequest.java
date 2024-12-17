package isep.ipp.pt.Smart_cities.Authentication;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must contain only letters and numbers")
    String name;
    @NotNull
    @Email(message = "Invalid email format")
    String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,128}$", message = "Password must contain at least one letter and one number")
    String password;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,128}$", message = "Password must contain at least one letter and one number")
    String repeatPassword;
    String pushToken;
    @NotNull
    Types type;

    @Size(max = 100, message = "Preferred location can have at most 100 characters")
    String preferredLocation;

    @Size(max = 5, message = "You can choose up to 5 categories")
    Set<String> preferredCategories;
}
