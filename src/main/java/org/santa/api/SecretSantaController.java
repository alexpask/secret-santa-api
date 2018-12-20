package org.santa.api;

import org.santa.exception.SantaException;
import org.santa.model.dtos.CreateSantaRequest;
import org.santa.model.dtos.ErrorResponse;
import org.santa.model.entities.SecretSanta;
import org.santa.service.SantaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides endpoints for managing secret santa lists.
 */
@RestController
public class SecretSantaController {

    private final SantaService santaService;

    public SecretSantaController(SantaService santaService) {

        this.santaService = santaService;
    }

    @PostMapping("/api/santa")
    public SecretSanta createSanta(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody CreateSantaRequest createSantaRequest) {

        return santaService.create(user.getUsername(), createSantaRequest);
    }

    @GetMapping("/api/santa")
    public SecretSanta getSanta(@AuthenticationPrincipal UserDetails user) {

        return santaService.getbyUsername(user.getUsername());
    }

    @ExceptionHandler(SantaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse santaException(SantaException se) {
        return new ErrorResponse(se.getMessage());
    }
}
