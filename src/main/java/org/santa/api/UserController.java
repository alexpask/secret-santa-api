package org.santa.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.santa.model.dtos.Availability;
import org.santa.model.dtos.LoginRequest;
import org.santa.model.dtos.RegistrationRequest;
import org.santa.model.dtos.Token;
import org.santa.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Api(description = "Controller to manage users of the application")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @ApiOperation(
            value = "Registers user and returns access token",
            notes = "Registers a user with the application"
    )
    @PostMapping(
            value = "/register",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Token register(@RequestBody @Valid RegistrationRequest registrationRequest)
            throws Exception {

        userService.register(registrationRequest);

        return login(
                new LoginRequest(
                        registrationRequest.getUsername(),
                        registrationRequest.getPassword()));
    }

    @ApiOperation(
            value = "Returns access token",
            notes = "Authenticates username and password"
    )
    @PostMapping(
            value = "/login",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Token login(@RequestBody @Valid LoginRequest loginRequest)
            throws Exception {

        return userService.generateToken(loginRequest);
    }

    @ApiOperation(
            value = "Returns username availability",
            notes = "Checks if username has already been taken"
    )
    @GetMapping(
            value = "/available/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Availability usernameAvailable(@PathVariable("username") String username) {

        return new Availability(username, userService.checkAvailability(username));
    }

    @ApiOperation(
            value = "Returns user details",
            notes = "Uses bearer token to fetch user details",
            authorizations = @Authorization(value = "jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = UserDetails.class, message = "Successfully retrieved user details"),
            @ApiResponse(code = 403, message = "Unable to retrieve user details, normally needs the token is invalid")
    })
    @GetMapping(
            value = "/principal",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDetails principal(@AuthenticationPrincipal UserDetails user) {

        return user;
    }
}
