package lk.nibm.userservice.config;

import lk.nibm.userservice.security.JwtRequestFilter;
import lk.nibm.userservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig is a Spring Security configuration class that sets up security filters,
 * authentication mechanisms, and authorization rules for the application.
 *
 * This class configures HTTP security, including JWT-based authentication, password encoding,
 * and manages session creation policy.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor-based dependency injection for JwtUtil and UserDetailsService.
     *
     * @param jwtUtil The JwtUtil component used for JWT operations.
     * @param userDetailsService The UserDetailsService used to load user details.
     */
    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, @Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures HTTP security settings, including request authorization, session management,
     * and JWT filter integration.
     *
     * @param http The HttpSecurity object used to configure security settings.
     * @return A SecurityFilterChain object that defines the security filter chain.
     * @throws Exception If there is an error during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity; ensure this is acceptable for your use case
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Allow public access to registration and login endpoints
                        .requestMatchers("/api/users/admin/**").hasRole("ADMIN") // Restrict admin endpoints to users with 'ADMIN' role
                        .anyRequest().authenticated() // All other requests must be authenticated
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session management to support JWT-based authentication
                .addFilterBefore(new JwtRequestFilter(userDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class); // Add JWT filter before the standard username/password authentication filter

        return http.build(); // Build and return the configured SecurityFilterChain
    }

    /**
     * Provides a PasswordEncoder bean that uses BCrypt for password hashing.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an AuthenticationManager bean from the AuthenticationConfiguration.
     *
     * @param authenticationConfiguration The AuthenticationConfiguration used to get the AuthenticationManager.
     * @return The AuthenticationManager instance.
     * @throws Exception If there is an error during retrieval.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
