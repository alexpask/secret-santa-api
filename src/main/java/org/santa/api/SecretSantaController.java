package org.santa.api;

import org.santa.model.dtos.CreateSantaRequest;
import org.santa.model.entities.SecretSanta;
import org.santa.service.SantaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Provides endpoints for managing secret santa lists.
 */
@RestController
@RequestMapping("/api/santa")
public class SecretSantaController {

    private final SantaService santaService;

    public SecretSantaController(SantaService santaService) {

        this.santaService = santaService;
    }

    @PostMapping
    public SecretSanta createSanta(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody @Valid CreateSantaRequest createSantaRequest) {

        return santaService.create(user.getUsername(), createSantaRequest);
    }

    @GetMapping
    public SecretSanta getSanta(@AuthenticationPrincipal UserDetails user) {

        return santaService.getbyUsername(user.getUsername());
    }
}
