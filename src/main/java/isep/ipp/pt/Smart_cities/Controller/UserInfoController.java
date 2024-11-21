package isep.ipp.pt.Smart_cities.Controller;


import isep.ipp.pt.Smart_cities.Authentication.AuthenticationApi;
import isep.ipp.pt.Smart_cities.Model.UserInfoModel.UserInfo;
import isep.ipp.pt.Smart_cities.Model.UserInfoModel.UserInfoView;
import isep.ipp.pt.Smart_cities.Service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import io.micrometer.core.ipc.http.HttpSender.Response;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.Date;

@RestController
@RequestMapping("userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationApi.class);


    @GetMapping("info")
    ResponseEntity<UserInfoView> getUserInfo(@RequestParam final String email) {
        UserInfo userInfo = userInfoService.getUserInfoByEmail(email);
        UserInfoView userInfoView = new UserInfoView(userInfo.getBirthDate(), userInfo.getGender(), userInfo.getAddress(), userInfo.getCity(), userInfo.getCountry());
        System.out.println(userInfoView.toString());
        return ResponseEntity.ok(userInfoView);
    }
}
