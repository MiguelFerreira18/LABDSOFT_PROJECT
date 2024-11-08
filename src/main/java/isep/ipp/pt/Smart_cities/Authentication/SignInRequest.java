package isep.ipp.pt.Smart_cities.Authentication;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignInRequest {
    @NotNull
    String email;
    @NotNull
    String password;
}
