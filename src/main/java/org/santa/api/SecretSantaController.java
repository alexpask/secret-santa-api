package org.santa.api;

import org.santa.exception.SantaException;
import org.santa.model.dtos.CreateSantaRequest;
import org.santa.model.dtos.ErrorResponse;
import org.santa.model.entities.SecretSanta;
import org.santa.service.SantaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
            @RequestBody @Valid CreateSantaRequest createSantaRequest) {

        return santaService.create(user.getUsername(), createSantaRequest);
    }

    @GetMapping("/api/santa")
    public SecretSanta getSanta(@AuthenticationPrincipal UserDetails user) {

        return santaService.getbyUsername(user.getUsername());
    }

    @ExceptionHandler(SantaException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse santaException(SantaException se) {

        return new ErrorResponse(singletonList(se.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse validationError(MethodArgumentNotValidException manve) {

        List<String> errors = manve
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .collect(toList());

        return new ErrorResponse(errors);
    }
}
