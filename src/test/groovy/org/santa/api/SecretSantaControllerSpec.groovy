package org.santa.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.santa.model.dtos.CreateSantaRequest
import org.santa.model.entities.SecretSanta
import org.santa.service.SantaService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@Ignore("need to get spring security with mockmvc...")
class SecretSantaControllerSpec extends Specification {

    def CREATE_SECRET_SANTA_ENDPOINT = "/api/santa/"

    def santaService = Mock(SantaService)
    def santaAdvice = new SantaControllerAdvice()
    def MAPPER = new ObjectMapper()

    MockMvc server

    void setup() {

        server = standaloneSetup(new SecretSantaController(santaService))
                .setControllerAdvice(santaAdvice)
                .build()
    }

    def "should create list of secret santa participants"() {

        given:

        def santaList = new CreateSantaRequest(participants: [
                new CreateSantaRequest.Participant("test1", "test1@email.com"),
                new CreateSantaRequest.Participant("test2", "test2@email.com"),
                new CreateSantaRequest.Participant("test3", "test3@email.com")
        ])

        when:

        server.perform(post(CREATE_SECRET_SANTA_ENDPOINT)
                .content(MAPPER.writeValueAsString(santaList))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("alex")))
                .andExpect(status().isOk())

        then:

        1 * santaService.create("alex", santaList) >> new SecretSanta()
    }
}
