package org.santa.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.santa.model.RegistrationRequest
import org.santa.model.User
import org.santa.service.UserService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class UserControllerSpec extends Specification {

    def MAPPER = new ObjectMapper()

    def userService = Mock(UserService)

    MockMvc server

    void setup() {

        server = standaloneSetup(new UserController(userService))
                .build()
    }

    def "should register user"() {

        given: "registration request"

        def registration = new RegistrationRequest(
                username: "test",
                email: "test@example.com",
                password: "password"
        )

        when: "endpoint is called"

        server.perform(post("/api/users/register")
                .content(MAPPER.writeValueAsString(registration))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        then: "user is registered"

        1 * userService.register(registration) >> new User()
    }
}
