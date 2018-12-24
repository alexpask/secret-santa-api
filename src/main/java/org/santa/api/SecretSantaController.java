package org.santa.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.santa.model.dtos.CreateSantaRequest;
import org.santa.model.entities.SecretSanta;
import org.santa.service.SantaService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/santa")
@Api(description = "Provides endpoints for managing secret santa lists")
public class SecretSantaController {

    private final SantaService santaService;

    public SecretSantaController(SantaService santaService) {

        this.santaService = santaService;
    }

    @ApiOperation(
            value = "Creates list of secret santa participants",
            authorizations = @Authorization(value = "jwt")
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SecretSanta createSanta(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody @Valid CreateSantaRequest createSantaRequest) {

        return santaService.create(user.getUsername(), createSantaRequest);
    }

    @ApiOperation(
            value = "Returns list of secret santa participants",
            authorizations = @Authorization(value = "jwt"))
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SecretSanta getSanta(@AuthenticationPrincipal UserDetails user) {

        return santaService.getbyUsername(user.getUsername());
    }
}
