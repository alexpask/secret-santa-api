package org.santa.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.santa.exception.UserException
import org.santa.model.dtos.LoginRequest
import org.santa.model.dtos.RegistrationRequest
import org.santa.model.dtos.Token
import org.santa.model.entities.User
import org.santa.service.UserService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static java.lang.String.format
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class UserControllerSpec extends Specification {

    def REGISTER_ENDPOINT = "/api/users/register"
    def LOGIN_ENDPOINT = "/api/users/login"
    def CHECK_AVAILABILITY_ENDPOINT = "/api/users/available/%s"

    def MAPPER = new ObjectMapper()

    def userService = Mock(UserService)
    def controllerAdvice = new SantaControllerAdvice()

    def token = "123456789"
    def username = "alexp"
    def password = "password1"

    MockMvc server

    void setup() {

        server = standaloneSetup(new UserController(userService))
                .setControllerAdvice(controllerAdvice)
                .build()
    }

    def "should register user"() {

        given: "registration request"

        def registration = new RegistrationRequest(
                username: username,
                email: "test@example.com",
                password: password
        )

        when: "endpoint is called"

        server.perform(post(REGISTER_ENDPOINT)
                .content(MAPPER.writeValueAsString(registration))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.token', is(token)))

        then: "user is registered"

        1 * userService.register(registration) >> new User()

        and: "user is logged in"

        1 * userService.generateToken(_ as LoginRequest) >> new Token(token)
    }

    def "should return error when user already exists"() {

        given: "a user is already registered"

        def registration = new RegistrationRequest(
                username: username,
                email: "test@example.com",
                password: password)

        when: "attempt to register the same user is made"

        server.perform(post(REGISTER_ENDPOINT)
                .content(MAPPER.writeValueAsString(registration))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        then: "error is returned"

        1 * userService.register(registration) >> { throw new UserException("error") }
    }

    def "should authenticate a user"() {

        given: "a user login request"

        def login = new LoginRequest(
                username: username,
                password: password)

        when: "attempt to login is made"

        server.perform(post(LOGIN_ENDPOINT)
                .content(MAPPER.writeValueAsString(login))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.token', is(token)))

        then: "a auth token is returned"

        1 * userService.generateToken(login) >> new Token(token)
    }

    @Unroll
    def "should return true when username availability is #available"(boolean available) {

        given:

        1 * userService.checkAvailability(username) >> available

        expect: "call to check availability is made"

        server.perform(get(format(CHECK_AVAILABILITY_ENDPOINT, username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.available', is(available)))

        where:

        available | _
        true      | _
        false     | _
    }
}
