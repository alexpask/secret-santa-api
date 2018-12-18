package org.santa.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Creates a registered user.
 */
@Data
public class RegistrationRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @Email
    private String email;
}
