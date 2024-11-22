package isep.ipp.pt.Smart_cities.Authentication;

import isep.ipp.pt.Smart_cities.Model.UserModel.UserView;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import isep.ipp.pt.Smart_cities.Service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthenticationApiTest {
    private static UserService staticUserService;
    @Autowired
    private AuthenticationApi authenticationApi;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;




    @BeforeAll
    static void init(@Autowired UserService userService) {
        staticUserService = userService;
    }


    @ParameterizedTest
    @CsvSource({
            "NormalUser, AnyNormalUser1@gmail.com, Password$123, Password$123,USER",
            "goodname, myEmail@hotmail.com, IsAPassword%123, IsAPassword%123,USER",
            "nonya, businessEmail@business.pt, AnyNormalPassword@123, AnyNormalPassword@123,USER"
    })
    void testSignupWithValidData(String name, String email, String password, String repeatPassword,Types type) {
        SignUpRequest request = new SignUpRequest();
        request.setType(type);
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setRepeatPassword(repeatPassword);

        ResponseEntity<UserView> response = authenticationApi.signup(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        UserView userView = response.getBody();
        assertNotNull(userView);
        assertEquals(name, userView.getName());
    }
    @ParameterizedTest
    @CsvSource({
            "anyName, email@gmail.com,Password$123,Passwor$12345",
            "goodname, myEmail@hotmail.com,IsAPassword%123,APassword%123",
            "nonya, businessEmail@business.pt,AnyNormal$Password123,AnyDifferenteNormal$Password123"
    })
    void testSignUpWithDiferentPasswords(String name, String email, String password, String repeatPassword) {
        SignUpRequest request = new SignUpRequest();
        request.setType(Types.USER);
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setRepeatPassword(repeatPassword);

        ResponseEntity<UserView> response = authenticationApi.signup(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    @ParameterizedTest
    @CsvSource({
            "anyName, emailgmail.com,Password$123,Password$123",
            "goodname, myEmail@hotmailcom,IsAPassword%123,APassword%123",
            "nonya, businessEmail@.pt,AnyNormalPassword@123,AnyNormal@Password123"
    })
    void testSignUpWithInvalidEmail(String name, String email, String password, String repeatPassword) {
        SignUpRequest request = new SignUpRequest();
        request.setType(Types.USER);
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setRepeatPassword(repeatPassword);

        ResponseEntity<UserView> response = authenticationApi.signup(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    @ParameterizedTest
    @CsvSource({
            "anyName, emailgmail.com,Password+123,Password123",
            "goodname, myEmail@hotmailcom,IsAPassword%123,APassword%123",
            "nonya, businessEmail@.pt,AnyNormalPassword..123,AnyNormalPassword123"
    })
    void testSignUpWithBathEmailFormat(String name, String email, String password, String repeatPassword) {
        SignUpRequest request = new SignUpRequest();
        request.setType(Types.USER);
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setRepeatPassword(repeatPassword);

        ResponseEntity<UserView> response = authenticationApi.signup(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    @ParameterizedTest
    @CsvSource({
            "anyName, email@gmail.com,Pa+1,Pa+1",
            "goodname, myEmail@hotmail.com,Bck.12,Bck.12",
            "nonya, businessEmail@business.pt,Any{}2,Any{}2"
    })
    void testPasswordNotSmallerThan8Characters(String name, String email, String password, String repeatPassword) {
        SignUpRequest request = new SignUpRequest();
        request.setType(Types.USER);
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setRepeatPassword(repeatPassword);

        ResponseEntity<UserView> response = authenticationApi.signup(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    @ParameterizedTest
    @CsvSource({
            "justabignamewithnosense, emailgmaill.com,Password+123,Password123",
            "justabignamewithnosense123, myEmail@hotmailcom,IsAPassword%123,APassword%123",
            "justabignamewithnosense123, businessEmail@.pt,AnyNormalPassword..123,AnyNormalPassword123"
    })
    void testSignUpWithInvalidNameSize(String name, String email, String password, String repeatPassword) {
        SignUpRequest request = new SignUpRequest();
        request.setType(Types.USER);
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setRepeatPassword(repeatPassword);

        ResponseEntity<UserView> response = authenticationApi.signup(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "emailNew@gmail.com,Password$123",
            "myNewEmail@hotmail.com,IsAPassword%123",
            "businessNewEmail@business.pt,AnyNormalPassword@123"
    })
    void testSignInWithGoodData(String email, String password) {
        SignUpRequest nRequest = new SignUpRequest();
        nRequest.setType(Types.USER);
        nRequest.setName("AnyName");
        nRequest.setEmail(email);
        nRequest.setPassword(password);
        nRequest.setRepeatPassword(password);

        ResponseEntity<UserView> nResponse = authenticationApi.signup(nRequest);
        assertEquals(HttpStatus.CREATED, nResponse.getStatusCode());


        SignInRequest request = new SignInRequest();
        request.setEmail(email);
        request.setPassword(password);
        ResponseEntity<UserView> response = authenticationApi.login(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "emailgmaill.com,Password$123",
            "myEmail@hotmailcom,IsAPassword%123",
            "businessEmail@.pt,AnyNormalPassword@123"
    })
    void testSignInWithInvalidEmail(String email, String password) {
        SignInRequest request = new SignInRequest();
        request.setEmail(email);
        request.setPassword(password);
        ResponseEntity<UserView> response = authenticationApi.login(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "email@gmail.com,Password123",
            "myEmail@hotmail.com,IsAPassword123",
            "businessEmail@business.pt,passowrdwithoutuppercase..123"
    })
    void testSignInWithInvalidPassword(String email, String password) {
        SignInRequest request = new SignInRequest();
        request.setEmail(email);
        request.setPassword(password);
        ResponseEntity<UserView> response = authenticationApi.login(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "anyName, email@gmail.com,Pa+1",
            "goodname, myEmail@hotmail.com,Bck.12",
            "nonya, businessEmail@business.pt,Any{}2"
    })
    void testSignInWithInvalidPasswordSize(String name, String email, String password) {
        SignInRequest request = new SignInRequest();
        request.setEmail(email);
        request.setPassword(password);
        ResponseEntity<UserView> response = authenticationApi.login(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    



}