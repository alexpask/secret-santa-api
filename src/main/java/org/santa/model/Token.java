package org.santa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains a bearer token for the FE to use
 * to access protected resources.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String token;
}
