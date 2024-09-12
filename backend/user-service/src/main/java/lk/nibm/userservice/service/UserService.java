package lk.nibm.userservice.service;

import lk.nibm.userservice.model.User;
import lk.nibm.userservice.model.User.RoleEnum;
import lk.nibm.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing user-related operations including registration, authentication, and user management.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // --- Standard CRUD Methods ---

    /**
     * Registers a new user. Encodes the password and sets a default role if not provided.
     *
     * @param user the user to register.
     * @return the saved user.
     */
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(RoleEnum.USER);
        }
        logger.info("Registering new user: {}", user.getEmail());
        return userRepository.save(user);
    }

    /**
     * Finds all users.
     *
     * @return a list of all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find.
     * @return an {@link Optional} containing the user if found, otherwise empty.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Updates a user by their ID.
     *
     * @param id the ID of the user to update.
     * @param user the user object with updated details.
     * @return the updated user.
     */
    public User updateById(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     * @return true if the user was found and deleted, false otherwise.
     */
    public boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Custom Methods ---

    /**
     * Finds a user by their email address.
     *
     * @param email the email of the user to find.
     * @return the {@link User} object with the specified email, or null if no user is found.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Updates the user's name based on their email address.
     *
     * @param email the email of the user to update.
     * @param newName the new name to set.
     * @return true if the user was found and updated, false otherwise.
     */
    public boolean updateUserNameByEmail(String email, String newName) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setName(newName);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Updates the user's role based on their email address.
     *
     * @param email the email of the user to update.
     * @param newRole the new role to set.
     * @return true if the user was found and updated, false otherwise.
     */
    public boolean updateUserRoleByEmail(String email, RoleEnum newRole) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setRole(newRole);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Deletes a user based on their email address.
     *
     * @param email the email of the user to delete.
     * @return true if the user was found and deleted, false otherwise.
     */
    public boolean deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    /**
     * Updates the user's details.
     *
     * @param user the user object with updated details.
     */
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Log the attempt to load a user based on the provided email address.
        logger.debug("Attempting to load user by email: {}", email);

        // Retrieve the user from the database using the provided email address.
        User user = userRepository.findByEmail(email);

        // Check if the user was found in the database.
        if (user == null) {
            // Log a warning and throw an exception if no user is found with the given email.
            logger.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Log the successful retrieval of the user.
        logger.debug("User found: {}", user.getEmail());

        // Return a UserDetails object, which Spring Security uses for authentication and authorization.
        // This object contains the user's email, password, and authorities (roles).
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Username (email)
                user.getPassword(), // Password
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) // Authorities (roles)
        );
    }

}
