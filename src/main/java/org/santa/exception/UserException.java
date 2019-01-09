package org.santa.exception;

/**
 * Thrown on user related errors.
 */
public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }
}
