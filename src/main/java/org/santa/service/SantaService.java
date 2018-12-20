package org.santa.service;

import org.santa.model.dtos.CreateSantaRequest;
import org.santa.model.entities.SecretSanta;

/**
 * Provides service to manage secret santa.
 */
public interface SantaService {

    /**
     *
     * @param username Registered user to create the secret santa list under.
     * @param createSantaRequest Creates a secret santa list based on form data.
     * @return Secret Santa list.
     */
    SecretSanta create(String username, CreateSantaRequest createSantaRequest);

    /**
     * Pulls back secret santa list based on a username.
     *
     * @param username
     * @return
     */
    SecretSanta getbyUsername(String username);
}
