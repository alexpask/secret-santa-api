package org.santa.provider;

import org.santa.model.UserDetailsImpl;
import org.santa.repository.UsersRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Token authentication provider. Finds {@link org.santa.model.User} from repository
 * via token and builds a {@link UserDetails}  object.
 */
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UsersRepository usersRepository;

    public TokenAuthenticationProvider(UsersRepository usersRepository) {

        this.usersRepository = usersRepository;
    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        // NOOP
    }

    @Override
    protected UserDetails retrieveUser(
            String username,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        Object token = authentication.getCredentials();

        return Optional.ofNullable(token)
                .map(String::valueOf)
                .map(usersRepository::getUserByToken)
                .map(user -> new UserDetailsImpl(user.getUsername(), user.getPassword(), user.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
