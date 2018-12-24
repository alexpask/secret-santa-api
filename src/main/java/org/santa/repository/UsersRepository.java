package org.santa.repository;

import org.santa.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link User} objects.
 */
@Repository
public interface UsersRepository extends JpaRepository<User, String> {

    User getUserByUsername(String username);

    boolean existsByUsername(String username);
}
