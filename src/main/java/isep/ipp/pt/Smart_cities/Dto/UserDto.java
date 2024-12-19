package isep.ipp.pt.Smart_cities.Dto;

import isep.ipp.pt.Smart_cities.Model.UserModel.Role;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String email;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }
}
