package org.santa.api;

import org.santa.model.dtos.LoginRequest;
import org.santa.model.dtos.RegistrationRequest;
import org.santa.model.dtos.Token;
import org.santa.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller to manage users of the application.
 */
@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    /**
     * Registers a user with the application.
     *
     * @param registrationRequest User registration details
     * @return Bearer token of registered user
     * @throws Exception thrown if there is an error in sign up
     */
    @PostMapping("/register")
    public Token register(@Valid @RequestBody RegistrationRequest registrationRequest)
            throws Exception {

        userService.register(registrationRequest);

        return login(
                new LoginRequest(
                        registrationRequest.getUsername(),
                        registrationRequest.getPassword()));
    }

    /**
     * Given a username and password return an access token.
     *
     * @param loginRequest Username and password for login
     * @return Bearer token of user
     * @throws Exception thrown if there is an error in login
     */
    @PostMapping("/login")
    public Token login(@Valid @RequestBody LoginRequest loginRequest)
            throws Exception {

        return userService.generateToken(loginRequest);
    }

    /**
     * Testing endpoint. Returns principal details.
     */
    @GetMapping("/principal")
    public UserDetails principal(@AuthenticationPrincipal UserDetails user) {

        return user;
    }
}
