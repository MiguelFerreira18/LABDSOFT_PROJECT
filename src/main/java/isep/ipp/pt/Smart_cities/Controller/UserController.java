package isep.ipp.pt.Smart_cities.Controller;

import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.UserModel.UserView;
import isep.ipp.pt.Smart_cities.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}