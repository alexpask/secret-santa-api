package org.santa.service;

import org.santa.model.RegistrationRequest;
import org.santa.model.User;

public interface UserService {

    /**
     * Registers a user to the repository
     *
     * @param registrationRequest Request to create user
     * @return Created user
     */
    User register(RegistrationRequest registrationRequest);
}
