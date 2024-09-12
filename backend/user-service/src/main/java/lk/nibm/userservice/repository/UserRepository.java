package lk.nibm.userservice.repository;

import lk.nibm.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods to perform CRUD operations and custom queries.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to find.
     * @return the {@link User} object with the specified email, or null if no user is found.
     */
    User findByEmail(String email);
}
