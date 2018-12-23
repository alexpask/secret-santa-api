package org.santa.service.impl

import org.santa.model.dtos.RegistrationRequest
import org.santa.model.entities.User
import org.santa.model.enums.Role
import org.santa.repository.UsersRepository
import org.santa.service.TokenService
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    def userRepository = Mock(UsersRepository)
    def tokenService = Mock(TokenService)
    def encoder = Mock(PasswordEncoder)
    def userService =
            new UserServiceImpl(encoder, tokenService, userRepository)

    def "should save a user from registration request"() {

        given: "a registration request"

        def username = "username"
        def email = "email"
        def password = "password"
        def encodedPassword = "encodedPassword"

        def registration = new RegistrationRequest(
                username:  username,
                email: email,
                password: password)

        def user = new User(
                username: username,
                email: email,
                role: Role.USER,
                password: encodedPassword)

        when:

        userService.register(registration)

        then:

        1 * userRepository.save(user)

        1 * encoder.encode(_ as String) >> encodedPassword

    }
}
