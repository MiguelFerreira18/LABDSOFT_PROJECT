package isep.ipp.pt.Smart_cities.Service;

import isep.ipp.pt.Smart_cities.Model.UserInfoModel.UserInfo;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.UserInfoRepo;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    public UserInfoRepo userInfoRepo;

    @Autowired
    public UserRepo userRepo;

    public UserInfo getUserInfo(String id) {
        return userInfoRepo.findByUserId(id).get();
    }

    public UserInfo getUserInfoByEmail(String email) {
        System.out.println(email);
        User user = userRepo.findByEmail(email).get();
        return userInfoRepo.findByUserId(user.getId()).get();
    }
}
