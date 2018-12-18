package org.santa.api;

import org.santa.model.RegistrationRequest;
import org.santa.model.User;
import org.santa.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegistrationRequest registrationRequest) {

        return userService.register(registrationRequest);
    }
}
