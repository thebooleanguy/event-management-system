package lk.nibm.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JwtRequestFilter is a Spring Security filter that processes incoming HTTP requests to check for
 * JWT tokens in the Authorization header. It validates the token, extracts the username, and sets
 * the authentication context if the token is valid.
 */
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Constructs a JwtRequestFilter with the given UserDetailsService and JwtUtil.
     *
     * @param userDetailsService The service to load user details.
     * @param jwtUtil The utility class for JWT operations.
     */
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Filters incoming requests to check for the presence of a JWT token in the Authorization header.
     * If a valid token is found, it extracts the username and sets the authentication context.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param chain The filter chain to pass the request and response to the next filter.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException If an I/O error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Extract the Authorization header from the request
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extract JWT token from header
            try {
                // Extract username from the JWT token
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("Could not extract username from JWT token", e);
            }
        }

        // If the username is not null and no authentication is set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Load user details using the extracted username
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Validate the JWT token
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    // Create and set authentication token in the security context
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.debug("Authentication successful for user: {}", username);
                } else {
                    logger.warn("JWT token validation failed for user: {}", username);
                }
            } catch (Exception e) {
                logger.error("Authentication failed for user: {}", username, e);
            }
        }

        // Proceed with the next filter in the chain
        chain.doFilter(request, response);
    }

}
