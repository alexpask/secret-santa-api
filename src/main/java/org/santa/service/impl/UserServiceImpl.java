package org.santa.service.impl;

import org.santa.model.LoginRequest;
import org.santa.model.RegistrationRequest;
import org.santa.model.Token;
import org.santa.model.User;
import org.santa.model.enums.Role;
import org.santa.repository.UsersRepository;
import org.santa.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;
    private final UsersRepository usersRepository;

    public UserServiceImpl(
            PasswordEncoder encoder,
            UsersRepository usersRepository) {

        this.encoder = encoder;
        this.usersRepository = usersRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User register(RegistrationRequest registrationRequest) {

        return usersRepository.save(
                User.builder()
                        .username(registrationRequest.getUsername())
                        .password(encoder.encode(registrationRequest.getPassword()))
                        .email(registrationRequest.getEmail())
                        .role(Role.USER)
                        .build());
    }

    @Override
    public Token generateToken(LoginRequest loginRequest)
            throws Exception {

        User user =
                usersRepository.getUserByUsername(loginRequest.getUsername());

        if (user == null ||
                !loginRequest.getUsername().equals(user.getUsername()) ||
                !encoder.matches(loginRequest.getPassword(), user.getPassword())) {

            throw new Exception();
        }

        UUID token = UUID.randomUUID();

        user.setToken(token.toString());

        usersRepository.save(user);

        return new Token(token.toString());
    }
}
