package org.santa.repository;

import org.santa.model.entities.SecretSanta;
import org.santa.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link SecretSanta} lists
 */
@Repository
public interface SecretSantaRepository extends JpaRepository<SecretSanta, Long> {

    SecretSanta getByCreator(User creator);

    boolean existsByCreator(User creator);
}
