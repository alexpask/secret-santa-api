package org.santa.service.impl;

import org.santa.model.RegistrationRequest;
import org.santa.model.User;
import org.santa.repository.UsersRepository;
import org.santa.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                        .build());
    }
}
