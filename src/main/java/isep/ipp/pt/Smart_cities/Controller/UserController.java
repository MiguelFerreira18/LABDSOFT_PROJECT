package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Dto.UserDto;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.UserModel.UserView;
import isep.ipp.pt.Smart_cities.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getuser")
    public ResponseEntity<UserView> getUser(@RequestParam String id) {

        User user = userService.findById(id);
        UserView userView = userService.toUserView(user);

        return ResponseEntity.ok(userView);
    }

    @GetMapping("/info/{email}")
    private UserDto getUserInformation(@PathVariable String email){
        User user = userService.findUserByEmail(email);
        return new UserDto(user);
    }


}