package isep.ipp.pt.Smart_cities.Mapper;

import isep.ipp.pt.Smart_cities.Authentication.SignUpRequest;
import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.UserModel.UserView;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


public interface UserMapper {

    public UserView toUserView(User user);
    public UserView fromInstitutionToUserView(Institution institution);

    public User toUser(SignUpRequest signUpRequest);
    public Institution toInstitution(SignUpRequest signUpRequest);

}
