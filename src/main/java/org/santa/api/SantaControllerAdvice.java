package org.santa.api;

import org.santa.exception.SantaException;
import org.santa.exception.UserException;
import org.santa.model.dtos.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Provides error handling for application.
 */
@RestControllerAdvice
public class SantaControllerAdvice {

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

    @ExceptionHandler({SantaException.class, UserException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse santaException(RuntimeException se) {

        return new ErrorResponse(singletonList(se.getMessage()));
    }
}
