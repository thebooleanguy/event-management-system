package lk.nibm.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lk.nibm.userservice.dto.LoginRequest;
import lk.nibm.userservice.model.User;
import lk.nibm.userservice.security.JwtUtil;
import lk.nibm.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for user-related operations including registration, login, and status checking.
 */
@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // --- Standard CRUD Operations ---

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Registers a new user.
     *
     * @param user The user information to register.
     * @return The registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return The user if found, otherwise a not found response.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a user by their ID.
     *
     * @param id The ID of the user to update.
     * @param user The user object with updated details.
     * @return The updated user.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateById(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return A response indicating success or failure.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        boolean deleted = userService.deleteById(id);
        return deleted ? ResponseEntity.ok("User deleted successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // --- Custom Methods ---

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user.
     * @return The user if found, otherwise a not found response.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * Logs in a user and generates a JWT token.
     *
     * @param loginRequest The login credentials.
     * @return A response containing the JWT token and user details.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());

            String jwt = jwtUtil.generateToken(userDetails);

            User user = userService.findByEmail(loginRequest.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            logger.error("An error occurred during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    /**
     * Updates the name of a user by their email.
     *
     * @param email The email of the user.
     * @param newName The new name to set for the user.
     * @return A response indicating success or failure.
     */
    @PutMapping("/update-name")
    public ResponseEntity<String> updateUserName(@RequestParam String email,
                                                 @RequestParam String newName) {
        boolean updated = userService.updateUserNameByEmail(email, newName);
        return updated ? ResponseEntity.ok("User name updated successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    /**
     * Updates the role of a user by their email.
     *
     * @param email The email of the user.
     * @param newRole The new role to set for the user.
     * @return A response indicating success or failure.
     */
    @PutMapping("/update-role")
    public ResponseEntity<String> updateUserRole(@RequestParam String email,
                                                 @RequestParam User.RoleEnum newRole) {
        boolean updated = userService.updateUserRoleByEmail(email, newRole);
        return updated ? ResponseEntity.ok("User role updated successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    /**
     * Deletes a user by their email.
     *
     * @param email The email of the user to delete.
     * @return A response indicating success or failure.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserByEmail(@RequestParam String email) {
        boolean deleted = userService.deleteUserByEmail(email);
        return deleted ? ResponseEntity.ok("User deleted successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    /**
     * Checks if the user is logged in.
     *
     * @param request The HTTP request.
     * @return A response indicating the login status.
     */
    @GetMapping("/status")
    public ResponseEntity<String> checkStatus(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.ok("User is logged in: " + authentication.getName());
        } else {
            return ResponseEntity.ok("User is not logged in");
        }
    }

    /**
     * Logs out the user by invalidating the session.
     *
     * @param session The HTTP session to invalidate.
     * @return A response indicating logout success.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Retrieves the profile of the currently logged-in user.
     *
     * @param request The HTTP request to get the current user details.
     * @return The user profile if found, otherwise a not found response.
     */
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Updates the profile of the currently logged-in user.
     *
     * @param request The HTTP request to get the current user details.
     * @param updatedUser The user data to update.
     * @return A response indicating success or failure.
     */
    @PutMapping("/profile")
    public ResponseEntity<String> updateUserProfile(HttpServletRequest request,
                                                    @RequestBody User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            User currentUser = userService.findByEmail(email);
            if (currentUser != null) {
                currentUser.setName(updatedUser.getName());
                currentUser.setEmail(updatedUser.getEmail()); // Consider if you want to allow email update
                currentUser.setPassword(updatedUser.getPassword()); // Ensure proper password hashing
                currentUser.setRole(updatedUser.getRole());
                userService.updateUser(currentUser);
                return ResponseEntity.ok("Profile updated successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
