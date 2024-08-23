package lk.nibm.userservice.service;

import lk.nibm.userservice.model.Role;
import lk.nibm.userservice.model.User;
import lk.nibm.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for managing user-related operations including registration and authentication.
 */
@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with encoded password and managed roles.
     *
     * @param user The user information to register.
     * @return The registered user.
     */
    public User registerUser(User user) {
        // Save or retrieve roles
        if (user.getRoles() != null) {
            Set<Role> managedRoles = user.getRoles().stream()
                    .map(role -> roleService.createOrUpdateRole(role.getName()))
                    .collect(Collectors.toSet());
            user.setRoles(managedRoles);
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
     * Authenticates a user by checking if the raw password matches the encoded password.
     *
     * @param email        The email of the user.
     * @param rawPassword  The raw password to check.
     * @return True if authentication is successful, otherwise false.
     */
    public boolean authenticate(String email, String rawPassword) {
        User user = findByEmail(email);
        if (user != null) {
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }
}
