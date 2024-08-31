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
import java.util.Optional;

/**
 * Service for managing user-related operations including registration and authentication.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        logger.debug("User found: {}", user.getEmail());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

    public User registerUser(User user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole(RoleEnum.USER);
        }
        logger.info("Registering new user: {}", user.getEmail());
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean updateUserNameByEmail(String email, String newName) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setName(newName);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean updateUserRoleByEmail(String email, RoleEnum newRole) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setRole(newRole);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
