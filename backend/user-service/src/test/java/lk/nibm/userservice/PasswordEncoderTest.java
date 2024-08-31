package lk.nibm.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderTest {

    @Test
    public void testPasswordEncoding() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "myPassword";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);

        boolean matches = encoder.matches(rawPassword, encodedPassword);
        assertTrue(matches, "The encoded password should match the raw password.");
    }
}
