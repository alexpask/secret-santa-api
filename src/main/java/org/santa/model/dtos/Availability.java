package org.santa.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Returns whether a username is available for registration.
 */
@Data
@AllArgsConstructor
public class Availability {

    private String username;
    private boolean available;
}
