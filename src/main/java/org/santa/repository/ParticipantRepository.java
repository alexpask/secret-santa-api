package org.santa.repository;

import org.santa.model.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Participant}
 */
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String> {
}
