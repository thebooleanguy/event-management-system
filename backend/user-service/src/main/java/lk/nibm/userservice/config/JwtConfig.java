package lk.nibm.userservice.config;

import lk.nibm.userservice.security.JwtRequestFilter;
import lk.nibm.userservice.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * JwtConfig is a Spring configuration class responsible for defining and providing
 * beans related to JWT (JSON Web Token) processing and security.
 *
 * This class is used to configure JWT-related components such as the utility class for
 * handling JWT operations and the filter for processing JWT requests.
 */
@Configuration
public class JwtConfig {

    /**
     * Provides a bean for the JwtUtil class.
     *
     * @return A new instance of JwtUtil.
     */
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    /**
     * Provides a bean for the JwtRequestFilter class. The @Lazy annotation is used to
     * initialize the UserDetailsService bean only when it is needed, avoiding potential
     * circular dependency issues.
     *
     * @param userDetailsService The UserDetailsService bean, lazily initialized.
     * @param jwtUtil The JwtUtil bean.
     * @return A new instance of JwtRequestFilter configured with UserDetailsService and JwtUtil.
     */
    @Bean
    public JwtRequestFilter jwtRequestFilter(@Lazy UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        return new JwtRequestFilter(userDetailsService, jwtUtil);
    }
}
