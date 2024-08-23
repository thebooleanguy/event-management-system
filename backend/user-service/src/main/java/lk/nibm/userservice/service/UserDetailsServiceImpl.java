package lk.nibm.userservice.service;

import lk.nibm.userservice.model.User;
import lk.nibm.userservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to load user-specific data.
 * Implements Spring Security's UserDetailsService interface.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load a user by their email and convert it to Spring Security's UserDetails.
     *
     * @param email the email of the user to load.
     * @return a UserDetails object with the user's information.
     * @throws UsernameNotFoundException if the user with the given email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Convert user's single role to a Spring Security GrantedAuthority object
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // Return a Spring Security User with the user's email, password, and authority
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(authority)  // Using List.of() to create a singleton list with the single authority
        );
    }
}
