package org.santa.service.impl;

import org.santa.model.dtos.LoginRequest;
import org.santa.model.dtos.RegistrationRequest;
import org.santa.model.dtos.Token;
import org.santa.model.entities.User;
import org.santa.model.enums.Role;
import org.santa.repository.UsersRepository;
import org.santa.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Token generateToken(LoginRequest loginRequest) {

        User user =
                usersRepository.getUserByUsername(loginRequest.getUsername());

        if (user == null ||
                !loginRequest.getUsername().equals(user.getUsername()) ||
                !encoder.matches(loginRequest.getPassword(), user.getPassword())) {

            throw new UsernameNotFoundException("User not found");
        }

        UUID token = UUID.randomUUID();

        user.setToken(token.toString());

        usersRepository.save(user);

        return new Token(token.toString());
    }
}
