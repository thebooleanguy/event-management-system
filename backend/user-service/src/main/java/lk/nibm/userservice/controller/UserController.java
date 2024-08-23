package lk.nibm.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lk.nibm.userservice.model.User;
import lk.nibm.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for user-related operations including registration, login, and status checking.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     *
     * @param user The user information.
     * @return The registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    /**
     * Logs in a user and creates a session.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @param request  The HTTP request for session management.
     * @return A response with a message and session ID if successful, or an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email,
                                                     @RequestParam String password,
                                                     HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("sessionId", session.getId());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }
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
        session.invalidate(); // Invalidate the session
        return ResponseEntity.ok("Logged out successfully");
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

}
