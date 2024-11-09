package isep.ipp.pt.Smart_cities.Authentication;

import lombok.Data;

@Data
public class SignUpRequest {
    String name;
    String email;
    String password;
    String repeatPassword;
    Types type;
}
