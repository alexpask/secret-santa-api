package org.santa.service.impl

import org.santa.exception.UserException
import org.santa.model.dtos.LoginRequest
import org.santa.model.dtos.RegistrationRequest
import org.santa.model.dtos.Token
import org.santa.model.entities.User
import org.santa.model.enums.Role
import org.santa.repository.UsersRepository
import org.santa.service.TokenService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Unroll

class UserServiceImplSpec extends Specification {

    def userRepository = Mock(UsersRepository)
    def tokenService = Mock(TokenService)
    def encoder = Mock(PasswordEncoder)
    def userService =
            new UserServiceImpl(encoder, tokenService, userRepository)

    def username = "username"
    def email = "email"
    def password = "password"
    def encodedPassword = "encodedPassword"

    def loginRequest = new LoginRequest(username: username, password: password)

    def "should save a user from registration request"() {

        given: "a registration request"

        def registration = new RegistrationRequest(
                username: username,
                email: email,
                password: password)

        def user = new User(
                username: username,
                email: email,
                role: Role.USER,
                password: encodedPassword)

        when: "attempt to register user is made"

        userService.register(registration)

        then: "user is saved"

        1 * userRepository.save(user)

        1 * encoder.encode(_ as String) >> encodedPassword
    }

    def "should throw exception when user cannot be saved"() {

        given:

        def registration = new RegistrationRequest(
                username: username,
                email: email,
                password: password)

        when:

        userService.register(registration)

        then:

        1 * encoder.encode(_ as String) >> encodedPassword

        1 * userRepository.save(_ as User) >> {
            throw new DataIntegrityViolationException("DataIntegrityViolationException")
        }

        and:

        thrown UserException
    }

    def "should generate token based on user's password authentication"() {

        when:

        def token = userService.generateToken(loginRequest)

        then:

        1 * userRepository.getUserByUsername(username) >>
                new User(username: username, password: encodedPassword)

        1 * tokenService.issueToken(_ as User) >> new Token("token1234")

        1 * encoder.matches(password, encodedPassword) >> true

        token.token == "token1234"
    }

    def "should throw exception when authentication fails"() {

        when:

        userService.generateToken(loginRequest)

        then:

        1 * userRepository.getUserByUsername(username) >> null

        thrown UsernameNotFoundException
    }

    def "should thrown exception when password does not match"() {

        when:

        userService.generateToken(loginRequest)

        then:

        1 * userRepository.getUserByUsername(username) >> {
            new User(username: username, password: encodedPassword)
        }

        1 * encoder.matches(password, encodedPassword) >> false

        thrown UsernameNotFoundException
    }

    @Unroll
    def "should check availability when username exists is #exists"(
            boolean exists, boolean available) {

        given:

        1 * userRepository.existsByUsername(username) >> exists

        when:

        def result = userService.checkAvailability(username)

        then:

        result == available

        where:

        exists | available
        false  | true
        true   | false
    }
}
