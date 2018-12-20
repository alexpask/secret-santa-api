package org.santa.service;

import org.santa.model.LoginRequest;
import org.santa.model.RegistrationRequest;
import org.santa.model.Token;
import org.santa.model.User;

public interface UserService {

    /**
     * Registers a user to the repository
     *
     * @param registrationRequest Request to create user
     * @return Created user
     */
    User register(RegistrationRequest registrationRequest);

    /**
     * Given a valid {@link LoginRequest} generate a token.
     * @param loginRequest Login request to validate
     * @return token for FE to use
     * @throws Exception
     */
    Token generateToken(LoginRequest loginRequest) throws Exception;
}
