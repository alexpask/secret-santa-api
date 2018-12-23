package org.santa.service;

import org.santa.model.dtos.Token;
import org.santa.model.entities.User;

/**
 * Service to manage security tokens.
 */
public interface TokenService {

    /**
     * Issue a security token based on a users details.
     *
     * @param user User to issue token to.
     * @return Issed token.
     */
    Token issueToken(User user);

    /**
     * Validate a token or return it's user.
     *
     * @param token Token to validate.
     * @return User of token.
     */
    User verifyToken(String token);
}
