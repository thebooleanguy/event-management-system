package lk.nibm.userservice.service;

import lk.nibm.userservice.model.User;
import lk.nibm.userservice.model.User.RoleEnum;
import lk.nibm.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service for managing user-related operations including registration and authentication.
 */
@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with encoded password and assigned role.
     *
     * @param user The user information to register.
     * @return The registered user.
     */
    public User registerUser(User user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ensure role is not null; assign default role if none provided
        if (user.getRole() == null) {
            user.setRole(RoleEnum.USER); // Default role if none provided
        }

        // Save user
        return userRepository.save(user);
    }

    /**
     * Finds a user by email.
     *
     * @param email The email of the user to find.
     * @return The found user.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Finds a user by ID.
     *
     * @param id The ID of the user to find.
     * @return An Optional containing the user if found, or empty if not.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Updates the name of a user by their email.
     *
     * @param email The email of the user.
     * @param newName The new name to set for the user.
     * @return True if the update was successful, otherwise false.
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
     * Updates the role of a user by their email.
     *
     * @param email The email of the user.
     * @param newRole The new role to set for the user.
     * @return True if the update was successful, otherwise false.
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
     * Deletes a user by their email.
     *
     * @param email The email of the user to delete.
     * @return True if the deletion was successful, otherwise false.
     */
    public boolean deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }


}
