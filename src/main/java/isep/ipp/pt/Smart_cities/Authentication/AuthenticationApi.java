package isep.ipp.pt.Smart_cities.Authentication;

import groovy.util.logging.Slf4j;
import isep.ipp.pt.Smart_cities.Mapper.UserMapperImpl;
import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Model.UserModel.UserView;
import isep.ipp.pt.Smart_cities.Service.InstitutionService;
import isep.ipp.pt.Smart_cities.Service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Slf4j
@RestController
@RequestMapping("auth/public")
public class AuthenticationApi {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private UserMapperImpl userMapper;

    private static final String ISSUER = "example.com";
    private static final Long EXPIRATION_TIME = 36000L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationApi.class);

    @PostMapping("login")
    public ResponseEntity<UserView> login(@RequestBody @Valid final SignInRequest request) {
        try {


            Authentication authentication = authenticate(request);
            Object principal = authentication.getPrincipal();
            if (principal == null) {
                LOGGER.warn("Authentication successful but principal is null for user: {}", request.email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return buildAuthenticationResponse(authentication, principal);

        } catch (BadCredentialsException e) {
            // Identifiable data is logged here, ense why its showing the encripted value
            LOGGER.warn("Failed login attempt for user: {}", request.email);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            LOGGER.error("Unexpected error during login for user: {}", request.email, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    private Authentication authenticate(SignInRequest request) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email, request.password));
    }

    private ResponseEntity<UserView> buildAuthenticationResponse(Authentication authentication, Object principal) {
        String scope = extractScope(authentication);
        JwtClaimsSet claims = buildClaims(principal, scope);
        String token = generateToken(claims);

        if (principal instanceof User user) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(userMapper.toUserView(user));
        } else if (principal instanceof Institution institution) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(userMapper.fromInstitutionToUserView(institution));
        } else {
            LOGGER.warn("Unsupported principal type: {}", principal.getClass().getName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    private String extractScope(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(joining(" "));
    }

    private JwtClaimsSet buildClaims(Object principal, String scope) {
        Instant now = Instant.now();
        JwtClaimsSet.Builder baseClaimsBuilder = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRATION_TIME))
                .claim("role", scope);

        if (principal instanceof User user) {
            return baseClaimsBuilder
                    .subject(format("User,%s,%s", user.getId(), user.getUsername()))
                    .claim("type", "User")
                    .claim("uuid", user.getId())
                    .claim("email", user.getEmail())
                    .build();
        } else if (principal instanceof Institution institution) {
            return baseClaimsBuilder
                    .subject(format("Institution,%s,%s", institution.getId(), institution.getUsername()))
                    .claim("type", "Institution")
                    .claim("uuid", institution.getId())
                    .claim("email", institution.getEmail())
                    .claim("rating", institution.getRating())
                    .build();
        } else {
            throw new IllegalArgumentException("Unsupported principal type: " +
                    (principal != null ? principal.getClass().getName() : "null"));
        }
    }

    private String generateToken(JwtClaimsSet claims) {
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @PostMapping("signup")
    public ResponseEntity<UserView> signup(@RequestBody @Valid final SignUpRequest request) {
        if (request.password == null || !request.password.equals(request.repeatPassword)) {
            LOGGER.warn("Password and repeat password do not match for user: {}", request.name);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {

            if (request.getType() == Types.USER) {
                User newUser = userMapper.toUser(request);
                return userService.saveUser(newUser)
                        .map(savedUser -> {
                            LOGGER.info("New user registered successfully: {}", savedUser.getEmail());
                            return ResponseEntity.status(HttpStatus.CREATED)
                                    .body(userMapper.toUserView(savedUser));
                        })
                        .orElseGet(() -> {
                            LOGGER.warn("Failed to register new user: {}", newUser.getEmail());
                            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                        });

            } else if (request.getType() == Types.INSTITUTION) {
                Institution newInstitution = userMapper.toInstitution(request);
                return institutionService.saveInstitution(newInstitution)
                        .map(savedInstitution -> {
                            LOGGER.info("New institution registered successfully: {}", savedInstitution.getUsername());
                            return ResponseEntity.status(HttpStatus.CREATED)
                                    .body(userMapper.fromInstitutionToUserView(savedInstitution));
                        })
                        .orElseGet(() -> {
                            LOGGER.warn("Failed to register new institution: {}", newInstitution.getUsername());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        });

            } else {
                LOGGER.warn("Invalid registration type requested: {}", request.getType());
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception ex) {
            LOGGER.error("Error during registration for username: {}", request.getName(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    private Optional<SignUpRequest> decryptSignUpRequest(SignUpRequest encryptedData) {
//        try {
//            SignUpRequest decryptedRequest = new SignUpRequest();
//            decryptedRequest.setName(encryptionUtil.decrypt(encryptedData.getName()).orElseThrow(() -> new Exception("Error decrypting name")));
//            decryptedRequest.setEmail(encryptionUtil.decrypt(encryptedData.getEmail()).orElseThrow(() -> new Exception("Error decrypting email")));
//            decryptedRequest.setPassword(encryptionUtil.decrypt(encryptedData.getPassword()).orElseThrow(() -> new Exception("Error decrypting password")));
//            decryptedRequest.setRepeatPassword(encryptionUtil.decrypt(encryptedData.getRepeatPassword()).orElseThrow(() -> new Exception("Error decrypting repeat password")));
//            decryptedRequest.setType(encryptedData.getType());
//            return Optional.of(decryptedRequest);
//
//        } catch (Exception ex) {
//            LOGGER.error("Error decrypting sign-up request", ex);
//            return Optional.empty();
//        }
//    }



}
